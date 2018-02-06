package jalov.easyssh.server;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import jalov.easyssh.main.AppNotification;
import jalov.easyssh.settings.SshdConfig;
import jalov.easyssh.utils.RootManager;
import jalov.easyssh.utils.ScriptBuilder;

/**
 * Created by jalov on 2018-01-22.
 */

public class SshdServer extends SshServer {
    private final String TAG = this.getClass().getName();
    public static final String SSHD_APP_NAME = "/system/bin/sshd";
    private boolean running;
    private AppNotification appNotification;
    private SshdConfig sshdConfig;

    public SshdServer(AppNotification appNotification, SshdConfig sshdConfig) {
        super();
        this.sshdConfig = sshdConfig;
        this.appNotification = appNotification;
        this.running = isSshdProcessRunning();
    }

    @Override
    public void start() {
        if (!running) {
            String startScript = new ScriptBuilder()
                    .createDsaHostkeyIfNotExist()
                    .createRsaHostkeyIfNotExist()
                    .createSshdConfigIfNotExist()
                    .createAuthorizedKeysFileIfNotExist()
                    .runSshd(sshdConfig.getSshdOptions())
                    .findProcess(SSHD_APP_NAME)
                    .build();

            Optional<InputStream> inputStream = RootManager.su(startScript);
            try {
                if (inputStream.isPresent() && inputStream.get().available() > 0) {
                    running = true;
                    appNotification.show();
                    notifyListeners();
                } else {
                    Log.d(TAG, "start: Unable to start SSH server");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void stop() {
        if (running) {
            String stopScript = new ScriptBuilder()
                    .killProcess(SSHD_APP_NAME)
                    .findProcess(SSHD_APP_NAME)
                    .build();
            Optional<InputStream> inputStream = RootManager.su(stopScript);
            try {
                if(inputStream.isPresent() && inputStream.get().available() == 0) {
                    running = false;
                    appNotification.hide();
                    notifyListeners();
                } else {
                    Log.d(TAG, "stop: Unable to stop SSH server");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean isRunning() {
        return running;
    }

    private boolean isSshdProcessRunning() {
        try {
            String script = new ScriptBuilder()
                    .findProcess(SSHD_APP_NAME)
                    .build();
            Optional<InputStream> inputStream = RootManager.su(script);
            if (inputStream.isPresent()) {
                return inputStream.get().available() > 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }
}
