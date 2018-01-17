package jalov.easyssh.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.SwitchPreference;
import android.support.annotation.Nullable;

import jalov.easyssh.R;

/**
 * Created by jalov on 2018-01-16.
 */

public class SettingsFragment extends PreferenceFragment {
    private SharedPreferences sharedPreferences;
    private SettingsManager settingsManager;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
}
