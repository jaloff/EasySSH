package jalov.easyssh.utils;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

/**
 * Created by jalov on 2018-01-16.
 */

public class RootManager {

    public static Optional<InputStream> su(String command) {
        Optional<InputStream> inputStream = Optional.empty();
        try {
            Process ps = Runtime.getRuntime().exec("su");
            DataOutputStream os = new DataOutputStream(ps.getOutputStream());
            inputStream = Optional.of(ps.getInputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            ps.waitFor();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return inputStream;
    }

    public static void saveFile(String path, String fileContent) {
        su("echo '" + fileContent + "' > " +path);
    }

}
