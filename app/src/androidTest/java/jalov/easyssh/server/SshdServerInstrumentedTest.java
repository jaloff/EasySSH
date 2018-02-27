package jalov.easyssh.server;

import android.support.test.runner.AndroidJUnit4;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;

import jalov.easyssh.main.AppNotification;
import jalov.easyssh.settings.SshdConfig;
import jalov.easyssh.utils.Logger;
import jalov.easyssh.utils.ProcessUtils;
import jalov.easyssh.utils.RootManager;
import jalov.easyssh.utils.ScriptBuilder;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

/**
 * Created by jalov on 2018-02-19.
 */
@RunWith(AndroidJUnit4.class)
public class SshdServerInstrumentedTest {
    private SshdServer server;
    private AppNotification appNotification;
    private Logger logger;
    private SshdConfig sshdConfig;
    private SshServer.StatusChangeListener statusChangeListener;

    @Before
    public void setUp() throws Exception {
        killServer();
        appNotification = mock(AppNotification.class);
        logger = mock(Logger.class);
        sshdConfig = mock(SshdConfig.class);
        statusChangeListener = mock(SshServer.StatusChangeListener.class);

        when(logger.registerSshServerInputStream(any(InputStream.class))).thenReturn(true);
        when(sshdConfig.getSshdOptions()).thenReturn(" -p 22 ");

        server = new SshdServer(appNotification, sshdConfig, logger);
        server.addOnStatusChangeListener(statusChangeListener);
    }

    @After
    public void tearDown() throws Exception {
        killServer();
    }

    @Test
    public void startShouldStartSshdServer() throws Exception {
        assertEquals(false, isServerRunning());
        assertEquals(false, server.isRunning());

        server.start();

        assertEquals(true, server.isRunning());
        Thread.sleep(1000); // Wait for sshd process
        assertEquals(true, isServerRunning());

        verify(appNotification, times(1)).show();
        verifyNoMoreInteractions(appNotification);
        verify(logger, times(1)).registerSshServerInputStream(any(InputStream.class));
        verify(sshdConfig, times(1)).getSshdOptions();
        verifyNoMoreInteractions(sshdConfig);
        verify(statusChangeListener, times(1)).onStatusChange(false);
        verify(statusChangeListener, times(1)).onStatusChange(true);
        verifyNoMoreInteractions(statusChangeListener);
    }

    @Test
    public void stopShouldStopSshdServer() throws Exception {
        server.start();

        assertEquals(true, server.isRunning());
        Thread.sleep(1000); // Wait for sshd process
        assertEquals(true, isServerRunning());

        server.stop();

        assertEquals(false, server.isRunning());
        assertEquals(false, isServerRunning());

        verify(appNotification, times(1)).show();
        verify(appNotification, times(1)).hide();
        verifyNoMoreInteractions(appNotification);
        verify(sshdConfig, times(1)).getSshdOptions();
        verifyNoMoreInteractions(sshdConfig);
        verify(statusChangeListener, times(2)).onStatusChange(false);
        verify(statusChangeListener, times(1)).onStatusChange(true);
        verifyNoMoreInteractions(statusChangeListener);
    }

    private void killServer() {
        if (isServerRunning()) {
            String script = new ScriptBuilder().killProcess(SshdServer.SSHD_APP_NAME)
                    .build();
            RootManager.run(script);
        }
    }

    private boolean isServerRunning() {
        return ProcessUtils.isProcessRunning(SshdServer.SSHD_APP_NAME);
    }
}