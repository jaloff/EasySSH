package jalov.easyssh.utils;

/**
 * Created by jalov on 2018-02-06.
 */

public class ScriptBuilder {
    private StringBuilder script;

    public ScriptBuilder() {
        script = new StringBuilder();
    }

    public String build() {
        return script.toString();
    }

    public ScriptBuilder readFile(String filePath) {
        script.append(Scripts.READ_FILE)
                .append(filePath)
                .append(";");
        return this;
    }

    public ScriptBuilder findProcess(String processName) {
        script.append(Scripts.FIND_PROCESS)
                .append(processName)
                .append(";");
        return this;
    }

    public ScriptBuilder killProcess(String processName) {
        script.append("pkill -f ")
                .append(processName)
                .append("*;");
        return this;
    }

    public ScriptBuilder createDsaHostkeyIfNotExist() {
        script.append(Scripts.CREATE_DSA_HOSTKEY_IF_NOT_EXIST);
        return this;
    }

    public ScriptBuilder createRsaHostkeyIfNotExist() {
        script.append(Scripts.CREATE_RSA_HOSTKEY_IF_NOT_EXIST);
        return this;
    }

    public ScriptBuilder createSshdConfigIfNotExist() {
        script.append(Scripts.CREATE_SSHD_CONFIG_IF_NOT_EXIST);
        return this;
    }

    public ScriptBuilder createAuthorizedKeysFileIfNotExist() {
        script.append(Scripts.CREATE_AUTHORIZED_KEYS_FILE_IF_NOT_EXIST);
        return this;
    }

    public ScriptBuilder runSshd(String sshdOptions) {
        script.append(Scripts.RUN_SSHD)
                .append(sshdOptions)
                .append(";");
        return this;
    }
}
