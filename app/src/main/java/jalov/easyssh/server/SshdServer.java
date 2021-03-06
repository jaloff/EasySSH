package jalov.easyssh.server;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import jalov.easyssh.main.AppNotification;
import jalov.easyssh.settings.SshdConfig;
import jalov.easyssh.utils.Logger;
import jalov.easyssh.utils.ProcessUtils;
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
    private Logger logger;

    public SshdServer(AppNotification appNotification, SshdConfig sshdConfig, Logger logger) {
        super();
        this.sshdConfig = sshdConfig;
        this.logger = logger;
        this.appNotification = appNotification;
        this.running = isSshdProcessRunning();
    }

    @Override
    public void start() {
        if (!running) {
            String startScript = new ScriptBuilder()
                    .createDsaHostkeyIfNotExist(SshdConfig.DSA_HOSTKEY_PATH)
                    .createRsaHostkeyIfNotExist(SshdConfig.RSA_HOSTKEY_PATH)
                    .createSshdConfigIfNotExist(SshdConfig.SSHD_CONFIG_PATH)
                    .createAuthorizedKeysFileIfNotExist(SshdConfig.AUTHORIZED_KEYS_PATH)
                    .runSshd(SSHD_APP_NAME, SshdConfig.SSHD_CONFIG_PATH,
                            SshdConfig.AUTHORIZED_KEYS_PATH, SshdConfig.DSA_HOSTKEY_PATH,
                            SshdConfig.RSA_HOSTKEY_PATH, sshdConfig.getSshdOptions())
                    .build();

            Optional<InputStream> inputStream = RootManager.runAsync(startScript);
            if (inputStream.isPresent()) {
                running = logger.registerSshServerInputStream(inputStream.get()); // Handle ssh process logs
                if (running) {
                    appNotification.show();
                    notifyListeners();
                }
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
            Optional<InputStream> inputStream = RootManager.run(stopScript);
            try {
                if (inputStream.isPresent() && inputStream.get().available() == 0) {
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
        return ProcessUtils.isProcessRunning(SSHD_APP_NAME);
    }
}
