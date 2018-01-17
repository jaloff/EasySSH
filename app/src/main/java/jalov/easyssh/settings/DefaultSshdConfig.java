package jalov.easyssh.settings;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jalov on 2018-01-16.
 */

enum DefaultSshdConfig {
    INSTANCE;

    private Map<String,String> config = new HashMap<>();

    private void init() {
        config.put("port", "22");
        config.put("AuthorizedKeysFile", "/data/ssh/authorized_keys");
        config.put("PasswordAuthentication", "no");
        config.put("PidFile", "/data/ssh/sshd.pid");
        config.put("Subsystem", "sftp internal-sftp");
    }

    public Map<String,String> getConfig() {
        if(config.isEmpty()) {
            init();
        }
        return config;
    }
}
