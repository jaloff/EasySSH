package jalov.easyssh.test;

import java.io.IOException;
import java.io.InputStream;

import jalov.easyssh.utils.RootManager;

/**
 * Created by jalov on 2018-03-01.
 */

public class TestUtils {
    public static String readInputStream(InputStream inputStream) throws IOException {
        StringBuilder container = new StringBuilder();
        while(inputStream.available() > 0) {
            container.append(Character.toChars(inputStream.read()));
        }
        inputStream.close();
        return container.toString();
    }

    public static boolean fileExist(String path) throws Exception {
        InputStream inputStream = RootManager.run("test -f " + path + "; echo $?").get();
        if(inputStream.available() == 0) {
            throw new Exception("Empty input stream");
        }

        int result = Character.getNumericValue(inputStream.read());

        return result == 0;
    }

    public static void deleteFile(String path) {
        RootManager.run("rm -f " + path);
    }
}
