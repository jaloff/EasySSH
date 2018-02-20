package jalov.easyssh.utils;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import jalov.easyssh.BuildConfig;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;


/**
 * Created by jalov on 2018-02-19.
 */
@RunWith(AndroidJUnit4.class)
public class ProcessUtilsInstrumentedTest {
    private final String NON_EXISTENT_PROCESS_NAME = "Random&#^7*(AppNAME139-9963@$!7854";
    private final String ALWAYS_RUNNING_PROCESS = BuildConfig.APPLICATION_ID; // This application package name

    @Test
    public void isProcessRunningShouldReturnTrueIfProcessIsRunning() throws Exception {
        boolean running = ProcessUtils.isProcessRunning(ALWAYS_RUNNING_PROCESS);
        assertTrue(running);
    }

    @Test
    public void isProcessRunningShouldReturnFalseIfProcessIsNotRunning() throws Exception {
        boolean running = ProcessUtils.isProcessRunning(NON_EXISTENT_PROCESS_NAME);
        assertFalse(running);
    }
}