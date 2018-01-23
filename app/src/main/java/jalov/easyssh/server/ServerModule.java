package jalov.easyssh.server;

import android.content.Context;

import javax.inject.Inject;
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
    @Inject
    SshServer providesSshServer(Context context) {
        return new SshdServer(context);
    }
}
