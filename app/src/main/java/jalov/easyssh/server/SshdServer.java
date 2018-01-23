package jalov.easyssh.server;

import android.content.Context;
import android.content.Intent;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

import jalov.easyssh.ProcessInfo;

/**
 * Created by jalov on 2018-01-22.
 */

public class SshdServer implements SshServer {
    final String TAG = this.getClass().getName();
    public static final String SSHD_APP_NAME = "/system/bin/sshd";
    private Optional<ProcessInfo> sshdProcessInfo;
    private Context context;
    private Intent intent;

    public SshdServer(Context context) {
        this.context = context;
        this.intent = new Intent(context, SshServerService.class);
        this.sshdProcessInfo = getSshdProcessInfo();
    }

    @Override
    public void start() {
        if(!sshdProcessInfo.isPresent()) {
            context.startService(intent);
        }
    }

    @Override
    public void stop() {
        if(sshdProcessInfo.isPresent()) {
            context.stopService(intent);
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
