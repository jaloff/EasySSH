package jalov.easyssh.settings;

import android.content.SharedPreferences;
import android.content.res.Resources;

import javax.inject.Singleton;

import jalov.easyssh.R;

/**
 * Created by jalov on 2018-01-19.
 */

@Singleton
public class Settings {
    private SshdConfig sshdConfig;
    private SharedPreferences sharedPreferences;
    private String portKey;
    private String runOnAppStartKey;
    private String sftpKey;

    public Settings(SshdConfig sshdConfig, SharedPreferences sharedPreferences, Resources resources) {
        this.sshdConfig = sshdConfig;
        this.sharedPreferences = sharedPreferences;
        this.portKey = resources.getString(R.string.port_key);
        this.runOnAppStartKey = resources.getString(R.string.run_on_app_start_key);
        this.sftpKey = resources.getString(R.string.sftp_key);
    }

    public void enableSftp() {
        sshdConfig.addOrUpdate("Subsystem", "sftp internal-sftp");
    }

    public void disableSftp() {
        sshdConfig.remove("Subsystem");
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
}
