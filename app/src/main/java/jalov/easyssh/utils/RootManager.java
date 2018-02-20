package jalov.easyssh.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Created by jalov on 2018-01-16.
 */

public class RootManager {

    public static Optional<InputStream> run(String command) {
        return execute(command, true);
    }

    public static Optional<InputStream> runAsync(String command) {
        return execute(command, false);
    }

    public static void saveFile(String path, String fileContent) {
        run("echo '" + fileContent + "' > " +path);
    }

    private static Optional<InputStream> execute(String command, boolean waitForExecution) {
        Optional<InputStream> inputStream = Optional.empty();
        try {
            Process ps = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(ps.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            inputStream = Optional.of(ps.getInputStream());
            if (waitForExecution) {
                ps.waitFor();
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return inputStream;
    }
}
