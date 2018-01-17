package jalov.easyssh.settings;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
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

        String key = getResources().getString(R.string.p_port);
        EditTextPreference etp = (EditTextPreference) findPreference(key);
        etp.setSummary(sharedPreferences.getString(key, DefaultSshdConfig.INSTANCE.getConfig().get(key)));
        etp.setOnPreferenceChangeListener((preference, o) -> {
            etp.setSummary(o.toString());
            settingsManager.updateConfig(key, o.toString());
            return true;
        });


    }
}
