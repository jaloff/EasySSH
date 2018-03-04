package jalov.easyssh.utils;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.util.Optional;

import jalov.easyssh.test.TestUtils;

import static org.junit.Assert.assertEquals;

@RunWith(AndroidJUnit4.class)
public class RootManagerInstrumentedTest {
    private final String TEXT = "Hello World!";

    @Test
    public void shouldExecuteCommandAsRoot() throws Exception {
        Optional<InputStream> inputStream = RootManager.run("echo " + String.format("\"%s\"", TEXT));

        String result = TestUtils.readInputStream(inputStream.get());
        assertEquals(TEXT + "\n", result);
    }
}
