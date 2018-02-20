package jalov.easyssh.settings;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by jalov on 2018-01-16.
 */

@Singleton
public class SshdConfig {
    public static final String TAG = SshdConfig.class.getName();
    public static final String SSHD_CONFIG_PATH = "/data/ssh/sshd_config";
    public static final String RSA_HOSTKEY_PATH = "/data/ssh/ssh_host_rsa_key";
    public static final String DSA_HOSTKEY_PATH = "/data/ssh/ssh_host_dsa_key";
    public static final String AUTHORIZED_KEYS_PATH = "/data/ssh/authorized_keys";
    private final String OPTION_TEMPLATE = " -o %s=\"%s\"";
    private Settings settings;

    @Inject
    public SshdConfig(Settings settings) {
        this.settings = settings;
    }

    public String getSshdOptions() {
        List<Option> options = new ArrayList<>();

        options.add(new Option("Port", settings.getPort()));
        if (settings.isSftpEnabled()) {
            options.add(new Option("Subsystem", "sftp internal-sftp"));
        }

        return options.stream()
                .map(o -> String.format(OPTION_TEMPLATE, o.key, o.value))
                .collect(Collectors.joining("", "", " -e -D "));
    }

    private class Option {
        String key;
        String value;

        Option(String key, String value) {
            this.key = key;
            this.value = value;
        }
    }
}
