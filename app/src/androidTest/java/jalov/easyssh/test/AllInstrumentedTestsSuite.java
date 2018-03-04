package jalov.easyssh.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import jalov.easyssh.server.SshdServerInstrumentedTest;
import jalov.easyssh.utils.ProcessUtilsInstrumentedTest;
import jalov.easyssh.utils.RootManagerInstrumentedTest;
import jalov.easyssh.utils.ScriptBuilderInstrumentedTest;

/**
 * Created by jalov on 2018-02-27.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({RootManagerInstrumentedTest.class, TestUtilsInstrumentedTest.class,
        ProcessUtilsInstrumentedTest.class, ScriptBuilderInstrumentedTest.class,
        SshdServerInstrumentedTest.class})
public class AllInstrumentedTestsSuite {
}
