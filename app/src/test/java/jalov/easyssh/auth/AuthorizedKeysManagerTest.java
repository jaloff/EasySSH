package jalov.easyssh.auth;

import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import static org.junit.Assert.assertEquals;

/**
 * Created by jalov on 2018-03-01.
 */
public class AuthorizedKeysManagerTest {
    @Test
    public void readAuthorizedKeysFromInputStream() throws Exception {
        String key1Type = "ssh-rsa";
        String key1Body = "key1";
        String key1Comment = "comment1";
        String key2Type = "ssh-dsa";
        String key2Body = "key2";
        String key2Comment = "comment2";
        String data = key1Type + " " + key1Body + " " + key1Comment + "\n" +
                key2Type + " " + key2Body + " " + key2Comment;
        InputStream inputStream = new ByteArrayInputStream(data.getBytes());

        AuthorizedKeysManager manager = new AuthorizedKeysManager();
        List<AuthorizedKey> keys = manager.readAuthorizedKeysFromInputStream(inputStream);
        AuthorizedKey key1 = keys.get(0);
        AuthorizedKey key2 = keys.get(1);

        assertEquals(2, keys.size());
        assertEquals(KeyType.getKey(key1Type), key1.getType());
        assertEquals(key1Body, key1.getKey());
        assertEquals(key1Comment, key1.getComment());
        assertEquals(KeyType.getKey(key2Type), key2.getType());
        assertEquals(key2Body, key2.getKey());
        assertEquals(key2Comment, key2.getComment());
    }

}