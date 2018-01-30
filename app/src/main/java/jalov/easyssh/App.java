package jalov.easyssh;

import android.app.Activity;
import android.app.Application;
import android.app.Service;
import android.content.BroadcastReceiver;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;
import dagger.android.HasBroadcastReceiverInjector;
import dagger.android.HasServiceInjector;

/**
 * Created by jalov on 2018-01-17.
 */

public class App extends Application implements HasActivityInjector, HasServiceInjector, HasBroadcastReceiverInjector {
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidActivityInjector;
    @Inject
    DispatchingAndroidInjector<Service> dispatchingAndroidServiceInjector;
    @Inject
    DispatchingAndroidInjector<BroadcastReceiver> dispatchingAndroidBroadcastReceiverInjector;

    @Override
    public void onCreate() {
        super.onCreate();
        jalov.easyssh.DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this);
    }
    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidActivityInjector;
    }

    @Override
    public AndroidInjector<Service> serviceInjector() {
        return dispatchingAndroidServiceInjector;
    }

    @Override
    public AndroidInjector<BroadcastReceiver> broadcastReceiverInjector() {
        return dispatchingAndroidBroadcastReceiverInjector;
    }
}
