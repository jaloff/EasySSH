package jalov.easyssh.utils;

import android.support.test.runner.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

import static org.junit.Assert.assertEquals;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class RootManagerInstrumentedTest {
    private final String TEXT = "Hello World!";
    private final String TEMP_DIR = "/data/local/tmp/";

    @Test
    public void shouldExecuteCommandAsRoot() throws Exception {
        Optional<InputStream> inputStream = RootManager.run("echo " + String.format("\"%s\"", TEXT));

        String result = readInputStream(inputStream.get());
        assertEquals(TEXT + "\n", result);
    }

    private String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder container = new StringBuilder();
        while(inputStream.available() > 0) {
            container.append(Character.toChars(inputStream.read()));
        }
        inputStream.close();
        return container.toString();
    }
}
