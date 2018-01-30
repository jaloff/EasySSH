package jalov.easyssh.main;

import android.app.NotificationManager;
import android.content.Context;
import android.content.res.Resources;
import android.support.v4.app.NotificationCompat;

import jalov.easyssh.R;

/**
 * Created by jalov on 2018-01-24.
 */

public class AppNotification {
    private final String NOTIFICATION_CHANNEL_ID = "ssh_server";
    private final int NOTIFICATION_ID = 10;
    private Context context;
    private NotificationManager notificationManager;
    private boolean showed = false;

    public AppNotification(Context context) {
        this.context = context;
        this.notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
    }

    public void show() {
        if(!showed) {
            Resources res = context.getResources();
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NOTIFICATION_CHANNEL_ID)
                    .setSmallIcon(R.drawable.ic_info_black_24dp)
                    .setContentTitle(res.getString(R.string.notification_title))
                    .setContentText(res.getString(R.string.notification_text))
                    .setOngoing(true);

            notificationManager.notify(NOTIFICATION_ID, builder.build());
            showed = true;
        }
    }

    public void hide() {
        if(showed) {
            notificationManager.cancel(NOTIFICATION_ID);
            showed = false;
        }
    }
}
