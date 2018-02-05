package jalov.easyssh.utils;

import jalov.easyssh.server.SshdServer;
import jalov.easyssh.settings.SshdConfig;

/**
 * Created by jalov on 2018-02-04.
 */

public class Scripts {
    static final String IF = "if [ ! -f ";
    static final String FI = "fi;";
    static final String IF_CONDITION_END = " ];";
    public static final String BEGIN = "sh -c '";
    public static final String END = ";'";
    public static final String READ_FILE = "cat ";
    public static final String KEYGEN_PATH = "/system/bin/ssh-keygen";
    public static final String PUB_KEY_SUFFIX = ".pub";
    public static final String CREATE_AUTHORIZED_KEYS_FILE_IF_NOT_EXIST
            = IF + SshdConfig.AUTHORIZED_KEYS_PATH + IF_CONDITION_END +
            "then echo \"\" > " + SshdConfig.AUTHORIZED_KEYS_PATH + ";" +
            "chmod 600 " + SshdConfig.AUTHORIZED_KEYS_PATH + ";" +
            FI;
    public static final String CREATE_DSA_HOSTKEY_IF_NOT_EXIST
            = IF + SshdConfig.DSA_HOSTKEY_PATH + IF_CONDITION_END +
            "then " + KEYGEN_PATH + " -t dsa -f " + SshdConfig.DSA_HOSTKEY_PATH + " -N \"\";" +
            "chmod 600 " + SshdConfig.DSA_HOSTKEY_PATH + ";" +
            "chmod 644 " + SshdConfig.DSA_HOSTKEY_PATH + PUB_KEY_SUFFIX + ";" +
            FI;
    public static final String CREATE_RSA_HOSTKEY_IF_NOT_EXIST
            = IF + SshdConfig.RSA_HOSTKEY_PATH + IF_CONDITION_END +
            "then " + KEYGEN_PATH + " -t rsa -f " + SshdConfig.RSA_HOSTKEY_PATH + " -N \"\";" +
            "chmod 600 " + SshdConfig.RSA_HOSTKEY_PATH + ";" +
            "chmod 644 " + SshdConfig.RSA_HOSTKEY_PATH + PUB_KEY_SUFFIX + ";" +
            FI;
    public static final String CREATE_SSHD_CONFIG_IF_NOT_EXIST
            = IF + SshdConfig.SSHD_CONFIG_PATH + IF_CONDITION_END +
            "then echo \"\" > " + SshdConfig.SSHD_CONFIG_PATH + ";" +
            "chmod 644 " + SshdConfig.SSHD_CONFIG_PATH + ";" +
            FI;
    public static final String RUN_SSHD = SshdServer.SSHD_APP_NAME +
            " -f " + SshdConfig.SSHD_CONFIG_PATH +
            " -h " + SshdConfig.DSA_HOSTKEY_PATH +
            " -h " + SshdConfig.RSA_HOSTKEY_PATH +
            " -o AuthorizedKeysFile=" + SshdConfig.AUTHORIZED_KEYS_PATH +
            " -o PasswordAuthentication=no";

    private Scripts(){}
}
