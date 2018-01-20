package jalov.easyssh.auth;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jalov.easyssh.settings.SshdConfig;

/**
 * Created by jalov on 2018-01-20.
 */

@Module
public class AuthorizedKeysModule {

    @Provides
    @Singleton
    @Inject
    AuthorizedKeysManager proviAuthorizedKeysManager(SshdConfig sshdConfig) {
        return new AuthorizedKeysManager(sshdConfig);
    }
}
