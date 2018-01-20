package jalov.easyssh;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import jalov.easyssh.auth.AuthorizedKeysModule;
import jalov.easyssh.settings.SettingsModule;

/**
 * Created by jalov on 2018-01-17.
 */

@Singleton
@Component(modules = {SettingsModule.class, AuthorizedKeysModule.class, BuildersModule.class})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance Builder application(App application);
        AppComponent build();
    }
    void inject(App app);
}
