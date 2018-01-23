package jalov.easyssh.server;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Optional;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import jalov.easyssh.R;
import jalov.easyssh.RootManager;
import jalov.easyssh.settings.Settings;

/**
 * Created by jalov on 2018-01-23.
 */

public class SshServerService extends Service {
    private final String NOTIFICATION_CHANNEL_ID = "ssh_server";
    private final int NOTIFICATION_ID = 10;
    private final String START_SSH = "start-ssh";
    private final String KILL_COMMAND = "kill -9 ";
    private NotificationManager notificationManager;
    @Inject
    Settings settings;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Start SSH
        RootManager.su(START_SSH);
        Resources res = getResources();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setSmallIcon(R.drawable.ic_info_black_24dp)
                .setContentTitle(res.getString(R.string.notification_title))
                .setContentText(res.getString(R.string.notification_text))
                .setOngoing(true);

        notificationManager.notify(NOTIFICATION_ID, builder.build());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // Kill SSH process
        File file = new File(settings.getSshPidFilePath());
        Optional<InputStream> inputStream = RootManager.getFileInputStream(file);
        try(BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream.get()))) {
            RootManager.su(KILL_COMMAND + reader.readLine());
            notificationManager.cancel(NOTIFICATION_ID);
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
