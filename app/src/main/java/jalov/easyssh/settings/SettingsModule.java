package jalov.easyssh.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.preference.PreferenceManager;

import javax.inject.Inject;
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
    static SshdConfig provideSshdConfig() {
        return new SshdConfig();
    }

    @Provides
    @Singleton
    @Inject
    Settings provideSettings(SshdConfig sshdConfig, SharedPreferences sharedPreferences, Resources resources, Context context) {
        return new Settings(sshdConfig, sharedPreferences, resources, context);
    }

    @Provides
    @Singleton
    @Inject
    SharedPreferences provideSharedPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context);
    }

}
