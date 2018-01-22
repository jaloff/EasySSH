package jalov.easyssh;

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
}
