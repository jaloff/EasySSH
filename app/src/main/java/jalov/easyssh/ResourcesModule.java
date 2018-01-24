package jalov.easyssh;

import android.content.Context;
import android.content.res.Resources;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jalov on 2018-01-22.
 */

@Module
public class ResourcesModule {
    @Provides
    @Singleton
    @Inject
    Resources provideResources(App app) {
        return app.getResources();
    }

    @Provides
    @Singleton
    @Inject
    Context provideApplicationContext(App app) {
        return app.getApplicationContext();
    }

    @Provides
    @Singleton
    @Inject
    AppNotification provideAppNotification(Context context) {
        return new AppNotification(context);
    }
}
