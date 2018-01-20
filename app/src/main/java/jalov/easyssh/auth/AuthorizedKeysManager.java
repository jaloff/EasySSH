package jalov.easyssh.auth;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Singleton;

import jalov.easyssh.RootManager;
import jalov.easyssh.settings.SshdConfig;

/**
 * Created by jalov on 2018-01-17.
 */

@Singleton
public class AuthorizedKeysManager {
    private SshdConfig sshdConfig;
    private List<AuthorizedKey> keys;

    public AuthorizedKeysManager(SshdConfig sshdConfig) {
        this.sshdConfig = sshdConfig;
        keys = loadAuthorizedKeys();
    }

    private List<AuthorizedKey> loadAuthorizedKeys() {
        String path = sshdConfig.get("AuthorizedKeysFile");
        File file = new File(path);
        Optional<InputStream> inputStream = RootManager.getFileInputStream(file);
        if(inputStream.isPresent()) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream.get()))) {
                return reader.lines().map(l -> l.split("\\s+"))
                        .filter(a -> a.length >= 3)
                        .map(a -> new AuthorizedKey(KeyType.getKey(a[0]), a[1], a[2]))
                        .collect(Collectors.toList());
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return null;
    }

    public void saveAuthorizedKeys() {

    }

    public List<AuthorizedKey> getKeys() {
        return keys;
    }
}
