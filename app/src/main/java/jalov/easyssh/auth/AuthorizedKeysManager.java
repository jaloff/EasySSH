package jalov.easyssh.auth;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import javax.inject.Inject;
import javax.inject.Singleton;

import jalov.easyssh.settings.SshdConfig;
import jalov.easyssh.utils.RootManager;
import jalov.easyssh.utils.ScriptBuilder;

/**
 * Created by jalov on 2018-01-17.
 */

@Singleton
public class AuthorizedKeysManager {
    private final String TAG = this.getClass().getName();
    private List<AuthorizedKey> keys;

    @Inject
    public AuthorizedKeysManager() {}

    private List<AuthorizedKey> loadAuthorizedKeys() {
        String script = new ScriptBuilder()
                .createAuthorizedKeysFileIfNotExist()
                .readFile(SshdConfig.AUTHORIZED_KEYS_PATH)
                .build();

        Optional<InputStream> inputStream = RootManager.su(script);
        if (inputStream.isPresent()) {
            return readAuthorizedKeysFromInputStream(inputStream.get());
        }
        Log.d(TAG, "loadAuthorizedKeys: Keys file loading error");
        return new ArrayList<>();
    }

    private void saveAuthorizedKeys() {
        String fileContent;
        fileContent = keys.stream().map(AuthorizedKey::toString).collect(Collectors.joining("\n"));
        RootManager.saveFile(SshdConfig.AUTHORIZED_KEYS_PATH, fileContent);
    }

    public List<AuthorizedKey> getKeys() {
        if(keys == null) {  //  Authorized keys lazy loading
            keys = loadAuthorizedKeys();
        }
        return keys;
    }

    public void addAuthorizedKeysFromInputStream(InputStream inputStream) {
        keys.addAll(readAuthorizedKeysFromInputStream(inputStream));
        saveAuthorizedKeys();
    }

    public void removeAuthorizedKey(int position) {
        keys.remove(position);
        saveAuthorizedKeys();
    }

    private List<AuthorizedKey> readAuthorizedKeysFromInputStream(InputStream inputStream) {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
            return reader.lines().map(l -> l.split("\\s+"))
                    .filter(a -> a.length >= 3)
                    .map(a -> new AuthorizedKey(KeyType.getKey(a[0]), a[1], a[2]))
                    .collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }

        return new ArrayList<>();
    }
}
