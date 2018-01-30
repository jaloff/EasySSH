package jalov.easyssh;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import jalov.easyssh.dependencies.AuthorizedKeysModule;
import jalov.easyssh.dependencies.BuildersModule;
import jalov.easyssh.dependencies.ResourcesModule;
import jalov.easyssh.dependencies.ServerModule;
import jalov.easyssh.dependencies.SettingsModule;

/**
 * Created by jalov on 2018-01-17.
 */

@Singleton
@Component(modules = {SettingsModule.class, AuthorizedKeysModule.class, ResourcesModule.class, ServerModule.class, BuildersModule.class})
public interface AppComponent {
    @Component.Builder
    interface Builder {
        @BindsInstance Builder application(App application);
        AppComponent build();
    }
    void inject(App app);
}
