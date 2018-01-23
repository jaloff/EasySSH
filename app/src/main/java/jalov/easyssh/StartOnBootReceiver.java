package jalov.easyssh;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import javax.inject.Inject;

import dagger.android.AndroidInjection;
import jalov.easyssh.server.SshServer;

/**
 * Created by jalov on 2018-01-23.
 */

public class StartOnBootReceiver extends BroadcastReceiver {
    @Inject
    SshServer server;

    @Override
    public void onReceive(Context context, Intent intent) {
        AndroidInjection.inject(this, context);
        if(intent.getAction().equalsIgnoreCase(Intent.ACTION_BOOT_COMPLETED)) {
            server.start();
        }
    }
}
