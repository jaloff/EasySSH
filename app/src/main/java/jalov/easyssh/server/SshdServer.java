package jalov.easyssh.server;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import jalov.easyssh.main.AppNotification;
import jalov.easyssh.settings.Settings;
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
    private Settings settings;

    public SshdServer(AppNotification appNotification, Settings settings) {
        super();
        this.appNotification = appNotification;
        this.settings = settings;
        this.running = isSshdProcessRunning();
    }

    @Override
    public void start() {
        if (!running) {
            StringBuilder runScript = new StringBuilder(Scripts.RUN_SSHD).append(" -p ").append(settings.getPort());
            if (settings.isSftpEnabled()) {
                runScript.append(" -o Subsystem=\"sftp internal-sftp\"");
            }
            RootManager.su(Scripts.BEGIN +
                    Scripts.CREATE_AUTHORIZED_KEYS_FILE_IF_NOT_EXIST +
                    Scripts.CREATE_SSHD_CONFIG_IF_NOT_EXIST +
                    Scripts.CREATE_DSA_HOSTKEY_IF_NOT_EXIST +
                    Scripts.CREATE_RSA_HOSTKEY_IF_NOT_EXIST +
                    runScript +
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
