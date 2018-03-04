package jalov.easyssh.utils;

import android.content.Context;
import android.support.test.InstrumentationRegistry;

import java.io.File;
import java.io.FileOutputStream;

/**
 * Created by jalov on 2018-03-02.
 */

public abstract class BaseFileInstrumentedTest {
    protected static final String FILE_NAME = "test-file.txt";
    protected static final Context CONTEXT = InstrumentationRegistry.getTargetContext();
    protected static final String FILE_PATH = CONTEXT.getFilesDir().getPath() + File.separator + FILE_NAME;
    protected static final File FILE = new File(FILE_PATH);

    protected static void createFile(String fileName) throws Exception {
        FileOutputStream fos = CONTEXT.openFileOutput(fileName, Context.MODE_PRIVATE);
        fos.write("Hello world!".getBytes());
        fos.close();
    }

}
