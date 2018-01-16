package jalov.easyssh;

import java.io.DataOutputStream;
import java.io.IOException;

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
}
