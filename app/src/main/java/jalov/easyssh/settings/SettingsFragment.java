package jalov.easyssh.settings;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v14.preference.SwitchPreference;
import android.support.v7.preference.EditTextPreference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;

import javax.inject.Inject;

import dagger.android.support.AndroidSupportInjection;
import jalov.easyssh.R;


/**
 * Created by jalov on 2018-01-16.
 */

public class SettingsFragment extends PreferenceFragmentCompat {
    private SharedPreferences sharedPreferences;
    @Inject
    SettingsManager settingsManager;

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getContext());
        settingsManager = new SettingsManager();
        addPreferencesFromResource(R.xml.app_preferences);

        // Port preference
        String portKey = getResources().getString(R.string.p_port);
        EditTextPreference etp = (EditTextPreference) findPreference(portKey);
        etp.setSummary(sharedPreferences.getString(portKey, DefaultSshdConfig.INSTANCE.getConfig().get(portKey)));
        etp.setOnPreferenceChangeListener((preference, o) -> {
            etp.setSummary(o.toString());
            settingsManager.updateConfig(portKey, o.toString());
            return true;
        });

        // SFTP preference
        String sftpKey = getResources().getString(R.string.sftp_key);
        SwitchPreference sp = (SwitchPreference) findPreference(sftpKey);
        sp.setOnPreferenceChangeListener((preference, newValue) -> {
            boolean value = (Boolean) newValue;
            if(value) {
                settingsManager.updateConfig("Subsystem", "sftp internal-sftp");
            } else {
                settingsManager.removeFromConfig("Subsystem");
            }
            return true;
        });
    }

    @Override
    public void onAttach(Context context) {
        AndroidSupportInjection.inject(this);
        super.onAttach(context);
    }
}
