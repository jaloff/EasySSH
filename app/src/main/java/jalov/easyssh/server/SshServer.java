package jalov.easyssh.server;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jalov on 2018-01-22.
 */

public abstract class SshServer {
    private final String TAG = this.getClass().getName();
    protected List<StatusChangeListener> listeners = new ArrayList<>();

    public abstract void start();
    public abstract void stop();
    public abstract boolean isRunning();

    public void restart() {
        stop();
        start();
    }

    public StatusChangeListener addOnStatusChangeListener(StatusChangeListener listener) {
        listeners.add(listener);
        listener.onStatusChange(isRunning());
        return listener;
    }

    public void removeOnStatusChangeListener(StatusChangeListener listener) {
        listeners.remove(listener);
    }

    void notifyListeners() {
        listeners.forEach(l -> l.onStatusChange(isRunning()));
    }

    /**
     * Created by jalov on 2018-01-24.
     */

    @FunctionalInterface
    public interface StatusChangeListener {
        void onStatusChange(boolean status);
    }
}
