package jalov.easyssh.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Created by jalov on 2018-02-19.
 */

public class ProcessUtils {

    public static boolean isProcessRunning(String processName) {
            try {
            String script = new ScriptBuilder()
                    .findProcess(processName)
                    .build();
            Optional<InputStream> inputStream = RootManager.run(script);
            if (inputStream.isPresent()) {
                return inputStream.get().available() > 0;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
