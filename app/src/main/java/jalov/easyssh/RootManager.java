package jalov.easyssh;

import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Created by jalov on 2018-01-16.
 */

public class RootManager {

    public static void su(String command) {
        try {
            Process ps = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(ps.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void saveFile(String path, String fileContent) {
        su("echo '" + fileContent + "' > " +path);
    }

    public static Optional<InputStream> getFileInputStream(File file) {
        Optional<InputStream> inputStream = Optional.empty();
        try {
            Process ps = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(ps.getOutputStream());
            os.writeBytes("cat " + file.getAbsolutePath() + "\n");
            os.writeBytes("exit\n");
            os.flush();
            ps.waitFor();

            inputStream = Optional.of(ps.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return inputStream;
    }
}
