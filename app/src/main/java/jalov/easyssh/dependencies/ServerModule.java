package jalov.easyssh.dependencies;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jalov.easyssh.main.AppNotification;
import jalov.easyssh.server.SshServer;
import jalov.easyssh.server.SshdServer;
import jalov.easyssh.settings.SshdConfig;
import jalov.easyssh.utils.Logger;

/**
 * Created by jalov on 2018-01-22.
 */

@Module
public class ServerModule {

    @Provides
    @Singleton
    @Inject
    SshServer providesSshServer(AppNotification appNotification, SshdConfig sshdConfig, Logger logger) {
        return new SshdServer(appNotification, sshdConfig, logger);
    }
}
