package jalov.easyssh.dependencies;

import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import jalov.easyssh.server.StartOnBootReceiver;
import jalov.easyssh.auth.AuthorizedKeysActivity;
import jalov.easyssh.main.MainActivity;
import jalov.easyssh.server.SshServerService;
import jalov.easyssh.settings.SettingsActivity;
import jalov.easyssh.settings.SettingsFragment;

/**
 * Created by jalov on 2018-01-18.
 */

@Module
public abstract class BuildersModule {

    @ContributesAndroidInjector
    abstract MainActivity bindMainActivity();

    @ContributesAndroidInjector
    abstract SettingsActivity bindSettingsActivity();

    @ContributesAndroidInjector
    abstract SettingsFragment bindSettingsFragment();

    @ContributesAndroidInjector
    abstract AuthorizedKeysActivity bindAuthorizedKeysActivity();

    @ContributesAndroidInjector
    abstract SshServerService bindSshServerService();

    @ContributesAndroidInjector
    abstract StartOnBootReceiver bindStartOnBootReceiver();
}

