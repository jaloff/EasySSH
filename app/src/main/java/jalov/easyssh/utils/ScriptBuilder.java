package jalov.easyssh.utils;

/**
 * Created by jalov on 2018-02-06.
 */

public class ScriptBuilder {
    static final String IF = "if [ ! -f ";
    static final String FI = "fi;";
    static final String IF_CONDITION_END = " ];";
    static final String STDERR_TO_STDOUT = " 2>&1";
    public static final String KEYGEN_PATH = "/system/bin/ssh-keygen";
    public static final String PUB_KEY_SUFFIX = ".pub";
    public static final String SERVER_START_MARKER = "run-ssh-server";

    private StringBuilder script;

    public ScriptBuilder() {
        script = new StringBuilder();
    }

    public String build() {
        return script.toString();
    }

    public ScriptBuilder readFile(String filePath) {
        script.append("cat ")
                .append(filePath)
                .append(";");
        return this;
    }

    public ScriptBuilder findProcess(String processName) {
        script.append("ps | grep ")
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

    public ScriptBuilder createDsaHostkeyIfNotExist(String path) {
        script.append(IF).append(path).append(IF_CONDITION_END)
                .append("then ").append(KEYGEN_PATH).append(" -t dsa -f ").append(path).append(" -N \"\";")
                .append("chmod 600 ").append(path).append(";")
                .append("chmod 644 ").append(path).append(PUB_KEY_SUFFIX).append(";")
                .append(FI);
        return this;
    }

    public ScriptBuilder createRsaHostkeyIfNotExist(String path) {
        script.append(IF).append(path).append(IF_CONDITION_END)
                .append("then ").append(KEYGEN_PATH).append(" -t rsa -f ").append(path).append(" -N \"\";")
                .append("chmod 600 ").append(path).append(";")
                .append("chmod 644 ").append(path).append(PUB_KEY_SUFFIX).append(";")
                .append(FI);
        return this;
    }

    public ScriptBuilder createSshdConfigIfNotExist(String path) {
        script.append(IF).append(path).append(IF_CONDITION_END)
                .append("then echo \"\" > ").append(path).append(";")
                .append("chmod 644 ").append(path).append(";")
                .append(FI);
        return this;
    }

    public ScriptBuilder createAuthorizedKeysFileIfNotExist(String path) {
        script.append(IF).append(path).append(IF_CONDITION_END)
                .append("then echo \"\" > ").append(path).append(";")
                .append("chmod 600 ").append(path).append(";")
                .append(FI);
        return this;
    }

    public ScriptBuilder runSshd(String sshdPath, String configPath, String keysPath,
                                 String dsaHostkeyPath, String rsaHostkeyPath, String sshdOptions) {
        script.append("echo ").append(SERVER_START_MARKER).append(";")
                .append(sshdPath)
                .append(" -f ").append(configPath)
                .append(" -h ").append(dsaHostkeyPath)
                .append(" -h ").append(rsaHostkeyPath)
                .append(" -o AuthorizedKeysFile=").append(keysPath)
                .append(" -o PasswordAuthentication=no")
                .append(sshdOptions)
                .append(STDERR_TO_STDOUT)
                .append(";");
        return this;
    }
}
