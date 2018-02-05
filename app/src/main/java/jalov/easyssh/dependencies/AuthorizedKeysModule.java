package jalov.easyssh.dependencies;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import jalov.easyssh.auth.AuthorizedKeysManager;

/**
 * Created by jalov on 2018-01-20.
 */

@Module
public class AuthorizedKeysModule {

    @Provides
    @Singleton
    @Inject
    AuthorizedKeysManager provideAuthorizedKeysManager() {
        return new AuthorizedKeysManager();
    }
}
