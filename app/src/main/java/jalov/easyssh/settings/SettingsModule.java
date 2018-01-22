package jalov.easyssh.settings;

import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jalov.easyssh.App;

/**
 * Created by jalov on 2018-01-17.
 */

@Module
public class SettingsModule {

    @Provides
    @Singleton
    static SshdConfig provideSshdConfig() {
        return new SshdConfig();
    }

    @Provides
    @Singleton
    @Inject
    Settings provideSettings(SshdConfig sshdConfig, SharedPreferences sharedPreferences, Resources resources) {
        return new Settings(sshdConfig, sharedPreferences, resources);
    }

    @Provides
    @Singleton
    @Inject
    SharedPreferences provideSharedPreferences(App app) {
        return PreferenceManager.getDefaultSharedPreferences(app.getApplicationContext());
    }

}
