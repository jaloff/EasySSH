package jalov.easyssh.test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import jalov.easyssh.utils.BaseFileInstrumentedTest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by jalov on 2018-03-01.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({TestUtilsInstrumentedTest.FileExistTest.class, TestUtilsInstrumentedTest.DeleteFileTest.class})
public class TestUtilsInstrumentedTest {

    public static class FileExistTest extends BaseFileInstrumentedTest {
        @After
        public void tearDown() throws Exception {
            CONTEXT.deleteFile(FILE_NAME);
        }

        @Test
        public void fileExistShouldReturnTrueIfFileExist() throws Exception {
            assertEquals(false, FILE.exists());
            createFile(FILE_NAME);
            assertTrue(TestUtils.fileExist(FILE.getPath()));
        }

        @Test
        public void fileExistShouldReturnFalseIfFileNotExist() throws Exception {
            assertEquals(false, FILE.exists());
            assertFalse(TestUtils.fileExist(FILE.getPath()));
        }
    }

    public static class DeleteFileTest extends BaseFileInstrumentedTest {
        @Before
        public void setUp() throws Exception {
            createFile(FILE_NAME);
        }

        @After
        public void tearDown() throws Exception {
            CONTEXT.deleteFile(FILE_NAME);
        }

        @Test
        public void deleteFileShouldDeleteFile() throws Exception {
            assertEquals(true, FILE.exists());
            TestUtils.deleteFile(FILE.getPath());
            assertEquals(false, FILE.exists());
        }
    }
}
