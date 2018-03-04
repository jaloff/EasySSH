package jalov.easyssh.auth;

/**
 * Created by jalov on 2018-01-16.
 */

public enum KeyType {
    RSA("ssh-rsa"), DSA("ssh-dsa");

    private String string;

    KeyType(String string) {
        this.string = string;
    }

    public static KeyType getKey(String string) {
        if(string.compareTo(RSA.getString()) == 0) {
            return RSA;
        } else if(string.compareTo(DSA.getString()) == 0) {
            return DSA;
        }
        throw new IllegalArgumentException("Can't parse string: " + string);
    }

    public String getString() {
        return string;
    }
}
