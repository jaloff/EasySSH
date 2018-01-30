package jalov.easyssh.dependencies;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jalov.easyssh.auth.AuthorizedKeysManager;
import jalov.easyssh.settings.SshdConfig;

/**
 * Created by jalov on 2018-01-20.
 */

@Module
public class AuthorizedKeysModule {

    @Provides
    @Singleton
    @Inject
    AuthorizedKeysManager provideAuthorizedKeysManager(SshdConfig sshdConfig) {
        return new AuthorizedKeysManager(sshdConfig);
    }
}
