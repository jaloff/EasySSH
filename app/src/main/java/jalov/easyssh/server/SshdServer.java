package jalov.easyssh.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

import jalov.easyssh.AppNotification;
import jalov.easyssh.ProcessInfo;
import jalov.easyssh.RootManager;
import jalov.easyssh.settings.Settings;

/**
 * Created by jalov on 2018-01-22.
 */

public class SshdServer implements SshServer {
    final String TAG = this.getClass().getName();
    public static final String SSHD_APP_NAME = "/system/bin/sshd";
    private final String START_SSH = "start-ssh";
    private final String KILL_COMMAND = "kill -9 ";
    private Optional<ProcessInfo> sshdProcessInfo;
    private Settings settings;
    private AppNotification appNotification;

    public SshdServer(Settings settings, AppNotification appNotification) {
        this.settings = settings;
        this.appNotification = appNotification;
        this.sshdProcessInfo = getSshdProcessInfo();
    }

    @Override
    public void start() {
        if(!sshdProcessInfo.isPresent()) {
            RootManager.su(START_SSH);
            appNotification.show();
        }
    }

    @Override
    public void stop() {
        if(sshdProcessInfo.isPresent()) {
            File file = new File(settings.getSshPidFilePath());
            Optional<InputStream> inputStream = RootManager.getFileInputStream(file);
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream.get()))) {
                RootManager.su(KILL_COMMAND + reader.readLine());
                appNotification.hide();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void restart() {
        stop();
        start();
    }

    @Override
    public boolean isRunning() {
        sshdProcessInfo = getSshdProcessInfo();
        return sshdProcessInfo.isPresent();
    }

    public Optional<ProcessInfo> getSshdProcessInfo() {
        Optional<ProcessInfo> processInfo = Optional.empty();
        try {
            Process su = Runtime.getRuntime().exec("su");
            DataOutputStream dos = new DataOutputStream(su.getOutputStream());
            BufferedReader reader = new BufferedReader(new InputStreamReader(su.getInputStream()));

            dos.writeBytes("ps | grep " + SSHD_APP_NAME + "\n");
            dos.writeBytes("exit\n");
            dos.flush();
            su.waitFor();

            if(reader.ready()) {
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
