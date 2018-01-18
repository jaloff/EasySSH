package jalov.easyssh;

import android.app.Activity;
import android.app.Application;

import javax.inject.Inject;

import dagger.android.AndroidInjector;
import dagger.android.DispatchingAndroidInjector;
import dagger.android.HasActivityInjector;

/**
 * Created by jalov on 2018-01-17.
 */

public class App extends Application implements HasActivityInjector {
    @Inject
    DispatchingAndroidInjector<Activity> dispatchingAndroidInjector;
    @Override
    public void onCreate() {
        super.onCreate();
        DaggerAppComponent.builder()
                .application(this)
                .build()
                .inject(this);
    }
    @Override
    public AndroidInjector<Activity> activityInjector() {
        return dispatchingAndroidInjector;
    }
}
