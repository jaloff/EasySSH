package jalov.easyssh.server;

/**
 * Created by jalov on 2018-01-22.
 */

public interface SshServer {
    void start();
    void stop();
    void restart();
    boolean isRunning();
}
