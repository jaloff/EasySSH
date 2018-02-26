package jalov.easyssh.utils;

import android.os.Handler;
import android.os.Looper;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;
import org.powermock.api.mockito.PowerMockito;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AndroidMockUtil {

    private AndroidMockUtil() {}
    /**
     * Mocks main thread handler post() and postDelayed() for use in Android unit tests
     *
     * To use this:
     * <ol>
     *     <li>Call this method in an {@literal @}Before method of your test.</li>
     *     <li>Place Looper.class in the {@literal @}PrepareForTest annotation before your test class.</li>
     *     <li>any class under test that needs to call {@code new Handler(Looper.getMainLooper())} should be placed
     *     in the {@literal @}PrepareForTest annotation as well.</li>
     * </ol>
     *
     * @throws Exception
     */
    public static void mockMainThreadHandler() throws Exception {
        PowerMockito.mockStatic(Looper.class);
        Looper mockMainThreadLooper = mock(Looper.class);
        when(Looper.getMainLooper()).thenReturn(mockMainThreadLooper);
        Handler mockMainThreadHandler = mock(Handler.class);
        Answer<Boolean> handlerPostAnswer = new Answer<Boolean>() {
            @Override
            public Boolean answer(InvocationOnMock invocation) throws Throwable {
                Runnable runnable = invocation.getArgument(0);
                Long delay = 0L;
                if (invocation.getArguments().length > 1) {
                    delay = invocation.getArgument(1);
                }
                if (runnable != null) {
                    mainThread.schedule(runnable, delay, TimeUnit.MILLISECONDS);
                }
                return true;
            }
        };
        doAnswer(handlerPostAnswer).when(mockMainThreadHandler).post(any(Runnable.class));
        doAnswer(handlerPostAnswer).when(mockMainThreadHandler).postDelayed(any(Runnable.class), anyLong());
        PowerMockito.whenNew(Handler.class).withArguments(mockMainThreadLooper).thenReturn(mockMainThreadHandler);
    }

    private final static ScheduledExecutorService mainThread = Executors.newSingleThreadScheduledExecutor();

}
