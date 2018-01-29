package jalov.easyssh.server;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jalov.easyssh.AppNotification;

/**
 * Created by jalov on 2018-01-22.
 */

@Module
public class ServerModule {

    @Provides
    @Singleton
    @Inject
    SshServer providesSshServer(AppNotification appNotification) {
        return new SshdServer(appNotification);
    }
}
