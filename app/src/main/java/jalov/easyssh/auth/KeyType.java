package jalov.easyssh.auth;

/**
 * Created by jalov on 2018-01-16.
 */

public enum KeyType {
    RSA, DSA;

    public static KeyType getKey(String string) {
        if(string.compareTo("ssh-rsa") == 0) {
            return RSA;
        } else if(string.compareTo("ssh-dss") == 0) {
            return DSA;
        }
        throw new IllegalArgumentException("Can't parse string: " + string);
    }
}
