package jalov.easyssh.server;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import jalov.easyssh.ProcessInfo;
import jalov.easyssh.RootManager;

/**
 * Created by jalov on 2018-01-22.
 */

public class SshdServer implements SshServer {
    final String TAG = this.getClass().getName();
    public static final String START_SSH = "start-ssh";
    public static final String SSHD_APP_NAME = "/system/bin/sshd";
    private Optional<ProcessInfo> sshdProcessInfo;

    @Override
    public void start() {
        if(!sshdProcessInfo.isPresent()) {
            RootManager.su(START_SSH);
        }
    }

    @Override
    public void stop() {
        if(sshdProcessInfo.isPresent()) {
            RootManager.su("kill -9 " + sshdProcessInfo.get().getPID());
        }
    }

    @Override
    public void restart() {
        stop();
        start();
    }

    @Override
    public boolean isRunning() {
        sshdProcessInfo = getSshdProcessesInfo();
        return sshdProcessInfo.isPresent();
    }

    public Optional<ProcessInfo> getSshdProcessesInfo() {
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
