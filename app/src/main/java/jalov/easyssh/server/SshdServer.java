package jalov.easyssh.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import jalov.easyssh.AppNotification;
import jalov.easyssh.ProcessInfo;
import jalov.easyssh.RootManager;
import jalov.easyssh.settings.Settings;

/**
 * Created by jalov on 2018-01-22.
 */

public class SshdServer extends SshServer {
    final String TAG = this.getClass().getName();
    public static final String SSHD_APP_NAME = "/system/bin/sshd";
    private final String START_SSH = "start-ssh";
    private final String STOP_SSH = "pkill -f " + SSHD_APP_NAME + "*";
    private boolean running;
    private Settings settings;
    private AppNotification appNotification;

    public SshdServer(Settings settings, AppNotification appNotification) {
        this.settings = settings;
        this.appNotification = appNotification;
        this.running = getSshdProcessInfo().isPresent();
    }

    @Override
    public void start() {
        if (!running) {
            RootManager.su(START_SSH);
            running = true;
            appNotification.show();
            notifyListeners();
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

    private Optional<ProcessInfo> getSshdProcessInfo() {
        Optional<ProcessInfo> processInfo = Optional.empty();
        try {
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream dos = new DataOutputStream(su.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(su.getInputStream()));

            dos.writeBytes("ps | grep " + SSHD_APP_NAME + "\n");
            dos.writeBytes("exit\n");
            dos.flush();
            su.waitFor();

            if (reader.ready()) {
                String line = reader.readLine();
                String[] cols = line.split("\\s+");
                processInfo = Optional.of(new ProcessInfo(cols[0], cols[1], cols[8]));
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return processInfo;
    }
}
