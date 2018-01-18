package jalov.easyssh.settings;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jalov on 2018-01-17.
 */

@Module
public class SettingsModule {

    @Provides
    @Singleton
    static SettingsManager provideSettingsManager() {
        return new SettingsManager();
    }
}
