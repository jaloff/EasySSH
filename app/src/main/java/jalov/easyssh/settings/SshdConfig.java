package jalov.easyssh.settings;

import javax.inject.Singleton;

/**
 * Created by jalov on 2018-01-16.
 */

@Singleton
public class SshdConfig {
    public static final String TAG = "SettingsManager";
    public static final String SSHD_CONFIG_PATH = "/data/ssh/sshd_config";
    public static final String RSA_HOSTKEY_PATH = "/data/ssh/ssh_host_rsa_key";
    public static final String DSA_HOSTKEY_PATH = "/data/ssh/ssh_host_dsa_key";
    public static final String AUTHORIZED_KEYS_PATH = "/data/ssh/authorized_keys";
}
