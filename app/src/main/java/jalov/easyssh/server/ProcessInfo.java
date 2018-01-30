package jalov.easyssh.server;

/**
 * Created by jalov on 2018-01-16.
 */

public class ProcessInfo {
    private String user;
    private String PID;
    private String name;

    public ProcessInfo(String user, String PID, String name) {
        this.user = user;
        this.PID = PID;
        this.name = name;
    }

    public String getUser() {
        return user;
    }

    public String getPID() {
        return PID;
    }

    public String getName() {
        return name;
    }
}
