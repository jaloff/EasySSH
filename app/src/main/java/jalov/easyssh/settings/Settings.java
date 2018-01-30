package jalov.easyssh.settings;

import android.content.ComponentName;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;

import javax.inject.Singleton;

import jalov.easyssh.server.StartOnBootReceiver;
import jalov.easyssh.R;

/**
 * Created by jalov on 2018-01-19.
 */

@Singleton
public class Settings {
    private SshdConfig sshdConfig;
    private SharedPreferences sharedPreferences;
    private Context context;
    private String portKey;
    private String runOnAppStartKey;
    private String sftpKey;
    private String runOnBootKey;
    private final String PID_FILEPATH_KEY = "PidFile";

    public Settings(SshdConfig sshdConfig, SharedPreferences sharedPreferences, Resources resources, Context context) {
        this.sshdConfig = sshdConfig;
        this.sharedPreferences = sharedPreferences;
        this.context = context;
        this.portKey = resources.getString(R.string.port_key);
        this.runOnAppStartKey = resources.getString(R.string.run_on_app_start_key);
        this.sftpKey = resources.getString(R.string.sftp_key);
        this.runOnBootKey = resources.getString(R.string.run_on_boot_key);
    }

    public void enableSftp() {
        sshdConfig.addOrUpdate("Subsystem", "sftp internal-sftp");
    }

    public void disableSftp() {
        sshdConfig.remove("Subsystem");
    }

    public void setRunOnBoot(boolean runOnBoot) {
        final int state = runOnBoot ? PackageManager.COMPONENT_ENABLED_STATE_ENABLED : PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
        ComponentName component = new ComponentName(context, StartOnBootReceiver.class);
        PackageManager packageManager = context.getPackageManager();
        packageManager.setComponentEnabledSetting(component, state, PackageManager.DONT_KILL_APP);
    }

    public void setPort(String port) {
        sshdConfig.addOrUpdate(portKey, port);
    }

    public String getPort() {
        return sshdConfig.get(portKey);
    }

    public boolean runOnAppStart() {
        return sharedPreferences.getBoolean(runOnAppStartKey, false);
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

    public String getSshPidFilePath() {
        return sshdConfig.get(PID_FILEPATH_KEY);
    }
}
