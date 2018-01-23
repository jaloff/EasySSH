package jalov.easyssh.settings;

import android.content.Context;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceFragmentCompat;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import jalov.easyssh.R;


/**
 * Created by jalov on 2018-01-16.
 */

public class SettingsFragment extends PreferenceFragmentCompat {
    @Inject
    Settings settings;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.app_preferences);

        // Port preference
        String portKey = settings.getPortKey();
        EditTextPreference portPreference = (EditTextPreference) findPreference(portKey);
        portPreference.setSummary(settings.getPort());
        portPreference.setOnPreferenceChangeListener((preference, o) -> {
            String port = o.toString();
            portPreference.setSummary(port);
            settings.setPort(port);
            return true;
        });

        // SFTP preference
        String sftpKey = settings.getSftpKey();
        SwitchPreference sftpPreference = (SwitchPreference) findPreference(sftpKey);
        sftpPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean value = (Boolean) newValue;
            if(value) {
                settings.enableSftp();
            } else {
                settings.disableSftp();
            }
            return true;
        });

        // SFTP preference
        String runOnBootKey = settings.getRunOnBootKey();
        SwitchPreference runOnBootPreference = (SwitchPreference) findPreference(runOnBootKey);
        runOnBootPreference.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean value = (Boolean) newValue;
            settings.setRunOnBoot(value, getContext());
            return true;
        });
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }
}
