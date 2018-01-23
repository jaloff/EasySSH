package jalov.easyssh.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import jalov.easyssh.RootManager;
import jalov.easyssh.settings.Settings;

/**
 * Created by jalov on 2018-01-23.
 */

public class SshServerService extends Service {
    private final String NOTIFICATION_ID = "ssh_server";
    private final String START_SSH = "start-ssh";
    private final String KILL_COMMAND = "kill -9 ";
    @Inject
    Settings settings;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();

        // Start SSH
        RootManager.su(START_SSH);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Kill SSH process
        File file = new File(settings.getSshPidFilePath());
        Optional<InputStream> inputStream = RootManager.getFileInputStream(file);
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream.get()))) {
            RootManager.su(KILL_COMMAND + reader.readLine());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
