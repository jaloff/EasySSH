package jalov.easyssh.auth;

import org.junit.Assert;
import org.junit.Test;

/**
 * Created by jalov on 2018-03-01.
 */
public class AuthorizedKeyTest {
    @Test
    public void toStringShouldReturnKeyInOpenSSHFormat() throws Exception {
        String validFormat = "ssh-rsa body comment";
        AuthorizedKey key = new AuthorizedKey(KeyType.RSA, "body", "comment");
        Assert.assertEquals(validFormat, key.toString());
    }

}