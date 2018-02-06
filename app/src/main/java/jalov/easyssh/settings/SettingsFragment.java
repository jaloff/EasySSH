package jalov.easyssh.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v4.app.DialogFragment;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import jalov.easyssh.R;
import jalov.easyssh.server.SshServer;


/**
 * Created by jalov on 2018-01-16.
 */

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener{
    @Inject
    Settings settings;
    @Inject
    SshServer server;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_preferences);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);

        // Port preference
        String portKey = settings.getPortKey();
        ValidatedEditTextPreference portPreference = (ValidatedEditTextPreference) findPreference(portKey);
        String portErrorMessage = getResources().getString(R.string.port_validation_error_text);
        portPreference.addValidator(new Validator<>(s -> s.matches("^[1-9][\\d]{0,4}$"), portErrorMessage));

        // SFTP preference
        String sftpKey = settings.getSftpKey();
        SwitchPreference sftpPreference = (SwitchPreference) findPreference(sftpKey);

        // Run on boot preference
        String runOnBootKey = settings.getRunOnBootKey();
        SwitchPreference runOnBootPreference = (SwitchPreference) findPreference(runOnBootKey);
        runOnBootPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean value = (Boolean) newValue;
            settings.setRunOnBoot(value);
            return true;
        });
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }

    @Override
    public void onDisplayPreferenceDialog(Preference preference) {
        DialogFragment fragment = null;

        if(preference instanceof ValidatedEditTextPreference) {
            fragment = ValidatedPreferenceDialogFragmentCompat.newInstance(preference.getKey());
        }

        if(fragment != null) {
            fragment.setTargetFragment(this, 0);;
            fragment.show(getFragmentManager(), "android.support.v7.preference.PreferenceFragment.DIALOG");
        } else {
            super.onDisplayPreferenceDialog(preference);
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        // Restart server on config change
        if(key.compareTo(settings.getPortKey()) == 0 || key.compareTo(settings.getSftpKey()) == 0) {
            server.restart();
        }
    }
}
