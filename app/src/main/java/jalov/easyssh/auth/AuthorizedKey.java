package jalov.easyssh.auth;

/**
 * Created by jalov on 2018-01-16.
 */

public class AuthorizedKey {
    private KeyType type;
    private String key;
    private String comment;

    public AuthorizedKey(KeyType type, String key, String comment) {
        this.type = type;
        this.key = key;
        this.comment = comment;
    }

    public KeyType getType() {
        return type;
    }

    public String getKey() {
        return key;
    }

    public String getComment() {
        return comment;
    }
}
