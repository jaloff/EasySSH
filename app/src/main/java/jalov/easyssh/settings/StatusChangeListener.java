package jalov.easyssh.settings;

/**
 * Created by jalov on 2018-01-24.
 */

@FunctionalInterface
public interface StatusChangeListener {
    void onStatusChange(boolean status);
}
