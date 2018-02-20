package jalov.easyssh.server;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

import jalov.easyssh.main.AppNotification;
import jalov.easyssh.settings.SshdConfig;
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

            Optional<InputStream> inputStream = RootManager.runAsync(startScript);
            if(inputStream.isPresent()) {   // Handle ssh process logs
                new Thread(() -> {
                    BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream.get()));
                    String line;
                    try {
                        while((line = reader.readLine()) != null){
                            Log.d(TAG, "start: " + line);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }).start();

                running = true;
                appNotification.show();
                notifyListeners();
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
        return ProcessUtils.isProcessRunning(SSHD_APP_NAME);
    }
}
