package jalov.easyssh.settings;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import javax.inject.Inject;
import javax.inject.Singleton;

import jalov.easyssh.server.StartOnBootReceiver;
import jalov.easyssh.R;

/**
 * Created by jalov on 2018-01-19.
 */

@Singleton
public class Settings {
    private SharedPreferences sharedPreferences;
    private Context context;
    private String portKey;
    private String runOnAppStartKey;
    private String sftpKey;
    private String runOnBootKey;
    private String darkThemeKey;
    private final String PID_FILEPATH_KEY = "PidFile";

    @Inject
    public Settings(SharedPreferences sharedPreferences, Resources resources, Context context) {
        this.sharedPreferences = sharedPreferences;
        this.context = context;
        this.portKey = resources.getString(R.string.port_key);
        this.runOnAppStartKey = resources.getString(R.string.run_on_app_start_key);
        this.sftpKey = resources.getString(R.string.sftp_key);
        this.runOnBootKey = resources.getString(R.string.run_on_boot_key);
        this.darkThemeKey = resources.getString(R.string.dark_theme_key);
    }

    public void enableSftp() {
        sharedPreferences.edit().putBoolean(sftpKey, true).apply();
    }

    public void disableSftp() {
        sharedPreferences.edit().putBoolean(sftpKey, false).apply();
    }

    public void setRunOnBoot(boolean runOnBoot) {
        final int state = runOnBoot ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        ComponentName component = new ComponentName(context, StartOnBootReceiver.class);
        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(component, state, PackageManager.DONT_KILL_APP);
    }

    public String getPort() {
        return sharedPreferences.getString(portKey, "22");
    }

    public boolean runOnAppStart() {
        return sharedPreferences.getBoolean(runOnAppStartKey, false);
    }

    public boolean isSftpEnabled() {
        return sharedPreferences.getBoolean(sftpKey, false);
    }

    public String getPortKey() {
        return portKey;
    }

    public String getRunOnAppStartKey() {
        return runOnAppStartKey;
    }

    public String getSftpKey() {
        return sftpKey;
    }

    public String getRunOnBootKey() {
        return runOnBootKey;
    }

    public boolean isDarkThemeEnabled() {
        return sharedPreferences.getBoolean(darkThemeKey, false);
    }

    public String getDarkThemeKey() {
        return darkThemeKey;
    }
}
