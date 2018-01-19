package jalov.easyssh.settings;

import javax.inject.Singleton;

/**
 * Created by jalov on 2018-01-19.
 */

@Singleton
public class Settings {
    private SshdConfig sshdConfig;

    public Settings(SshdConfig sshdConfig) {
        this.sshdConfig = sshdConfig;
    }

    public void enableSftp() {
        sshdConfig.addOrUpdate("Subsystem", "sftp internal-sftp");
    }

    public void disableSftp() {
        sshdConfig.remove("Subsystem");
    }

    public void setPort(String port) {
        sshdConfig.addOrUpdate("port", port);
    }
}
