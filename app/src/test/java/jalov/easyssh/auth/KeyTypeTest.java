package jalov.easyssh.auth;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by jalov on 2018-03-01.
 */
public class KeyTypeTest {
    @Test
    public void getKeyShouldReturnKeyTypeForGivenString() throws Exception {
        assertEquals(KeyType.RSA, KeyType.getKey("ssh-rsa"));
        assertEquals(KeyType.DSA, KeyType.getKey("ssh-dsa"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void getKeyShouldThrowExceptionForUnsupportedKeyString() throws Exception {
        KeyType.getKey("some-key");
    }
}