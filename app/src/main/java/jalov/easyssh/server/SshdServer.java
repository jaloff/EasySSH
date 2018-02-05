package jalov.easyssh.server;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import jalov.easyssh.main.AppNotification;
import jalov.easyssh.utils.RootManager;
import jalov.easyssh.utils.Scripts;

/**
 * Created by jalov on 2018-01-22.
 */

public class SshdServer extends SshServer {
    final String TAG = this.getClass().getName();
    public static final String SSHD_APP_NAME = "/system/bin/sshd";
    private final String STOP_SSH = "pkill -f " + SSHD_APP_NAME + "*";
    private boolean running;
    private AppNotification appNotification;

    public SshdServer(AppNotification appNotification) {
        super();
        this.appNotification = appNotification;
        this.running = isSshdProcessRunning();
    }

    @Override
    public void start() {
        if (!running) {
            RootManager.su(Scripts.BEGIN +
                    Scripts.CREATE_AUTHORIZED_KEYS_FILE_IF_NOT_EXIST +
                    Scripts.CREATE_SSHD_CONFIG_IF_NOT_EXIST +
                    Scripts.CREATE_DSA_HOSTKEY_IF_NOT_EXIST +
                    Scripts.CREATE_RSA_HOSTKEY_IF_NOT_EXIST +
                    Scripts.RUN_SSHD +
                    Scripts.END);
            if (isSshdProcessRunning()) {
                running = true;
                appNotification.show();
                notifyListeners();
            } else {
                Log.d(TAG, "start: Unable to start SSH server");
            }
        }
    }

    @Override
    public void stop() {
        if (running) {
            RootManager.su(STOP_SSH);
            running = false;
            appNotification.hide();
            notifyListeners();
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    private boolean isSshdProcessRunning() {
        try {
            Optional<InputStream> inputStream = RootManager.su("ps | grep " + SSHD_APP_NAME);
            if (inputStream.isPresent()) {
                return inputStream.get().available() > 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
