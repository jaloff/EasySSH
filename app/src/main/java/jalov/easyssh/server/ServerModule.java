package jalov.easyssh.server;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by jalov on 2018-01-22.
 */

@Module
public class ServerModule {

    @Provides
    @Singleton
    public static SshServer providesSshServer() {
        return new SshdServer();
    }
}
