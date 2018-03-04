package jalov.easyssh.utils;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import java.io.InputStream;
import java.util.Optional;

import jalov.easyssh.test.TestUtils;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by jalov on 2018-02-28.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({ScriptBuilderInstrumentedTest.ProcessScriptsTest.class,
        ScriptBuilderInstrumentedTest.KeyGenerationScriptsTest.class,
        ScriptBuilderInstrumentedTest.ConfigGenerationScriptsTest.class})
public class ScriptBuilderInstrumentedTest {

    public static class ProcessScriptsTest {
        @Test
        public void findProcessShouldReturnNonEmptyInputStreamIfProcessExist() throws Exception {
            String script = new ScriptBuilder().findProcess(ProcessUtilsInstrumentedTest.ALWAYS_RUNNING_PROCESS).build();
            Optional<InputStream> inputStream = RootManager.run(script);
            assertTrue(inputStream.get().available() > 0);
        }

        @Test
        public void findProcessShouldReturnEmptyInputStreamIfProcessNotExist() throws Exception {
            String script = new ScriptBuilder().findProcess(ProcessUtilsInstrumentedTest.NON_EXISTENT_PROCESS_NAME).build();
            Optional<InputStream> inputStream = RootManager.run(script);
            assertTrue(inputStream.get().available() == 0);
        }

        @Test
        public void killProcessShouldKillProcess() throws Exception {
            final String processName = "sleep$";
            RootManager.runAsync("bash -c 'exec sleep 30'");
            Thread.sleep(1000); // wait for process to start
            assertTrue(ProcessUtils.isProcessRunning(processName));

            String script = new ScriptBuilder().killProcess(processName).build();
            RootManager.run(script);

            assertFalse(ProcessUtils.isProcessRunning(processName));
        }
    }

    public static class KeyGenerationScriptsTest extends BaseFileInstrumentedTest {
        static final String PUB_KEY_PATH = FILE_PATH + ScriptBuilder.PUB_KEY_SUFFIX;
        @Before
        public void setUp() throws Exception {
            clean();
        }

        @After
        public void tearDown() throws Exception {
            clean();
        }

        @Test
        public void createDsaHostkeyIfNotExistShouldGenerateKeyFiles() throws Exception {
            String script = new ScriptBuilder().createDsaHostkeyIfNotExist(FILE_PATH).build();
            scriptShouldGenerateKeyFiles(script);
        }

        @Test
        public void createRsaHostkeyIfNotExistShouldGenerateKeyFiles() throws Exception {
            String script = new ScriptBuilder().createRsaHostkeyIfNotExist(FILE_PATH).build();
            scriptShouldGenerateKeyFiles(script);
        }

        private void clean() {
            TestUtils.deleteFile(PUB_KEY_PATH);
            TestUtils.deleteFile(FILE_PATH);
        }

        private void scriptShouldGenerateKeyFiles(String script) throws Exception {
            assertFalse(TestUtils.fileExist(PUB_KEY_PATH));
            assertFalse(TestUtils.fileExist(FILE_PATH));
            RootManager.run(script);
            assertTrue(TestUtils.fileExist(FILE_PATH));
            assertTrue(TestUtils.fileExist(PUB_KEY_PATH));
        }
    }

    public static class ConfigGenerationScriptsTest extends BaseFileInstrumentedTest {
        @Before
        public void setUp() throws Exception {
            TestUtils.deleteFile(FILE_PATH);
        }

        @After
        public void tearDown() throws Exception {
            TestUtils.deleteFile(FILE_PATH);
        }

        @Test
        public void createSshdConfigIfNotExistShouldCreateEmptyConfigFile() throws Exception {
            String script = new ScriptBuilder().createSshdConfigIfNotExist(FILE_PATH).build();
            scriptShouldCreateEmptyFile(script, FILE_PATH);
        }

        @Test
        public void createAuthorizedKeysFileIfNotExistShouldCreateEmptyFile() throws Exception {
            String script = new ScriptBuilder().createAuthorizedKeysFileIfNotExist(FILE_PATH).build();
            scriptShouldCreateEmptyFile(script, FILE_PATH);
        }

        private void scriptShouldCreateEmptyFile(String script, String path) throws Exception {
            assertFalse(TestUtils.fileExist(path));
            RootManager.run(script);
            assertTrue(TestUtils.fileExist(path));
            // Read created file
            script = new ScriptBuilder().readFile(path).build();
            InputStream inputStream = RootManager.run(script).get();
            // Remove white chars from output
            String fileContent = TestUtils.readInputStream(inputStream).trim();
            assertTrue(fileContent.isEmpty());
        }
    }

}
