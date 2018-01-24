package jalov.easyssh.server;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

/**
 * Created by jalov on 2018-01-23.
 */

public class SshServerService extends Service {
    @Inject
    SshServer server;

    @Override
    public void onCreate() {
        AndroidInjection.inject(this);
        super.onCreate();
        server.start();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
