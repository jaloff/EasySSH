package jalov.easyssh.server;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jalov.easyssh.AppNotification;
import jalov.easyssh.settings.Settings;

/**
 * Created by jalov on 2018-01-22.
 */

@Module
public class ServerModule {

    @Provides
    @Singleton
    @Inject
    SshServer providesSshServer(Settings settings, AppNotification appNotification) {
        return new SshdServer(settings, appNotification);
    }
}
