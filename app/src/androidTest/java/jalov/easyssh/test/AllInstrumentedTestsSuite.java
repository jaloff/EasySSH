package jalov.easyssh.test;

import org.junit.runner.RunWith;
import org.junit.runners.Suite;

import jalov.easyssh.server.SshdServerInstrumentedTest;
import jalov.easyssh.utils.ProcessUtilsInstrumentedTest;
import jalov.easyssh.utils.RootManagerInstrumentedTest;

/**
 * Created by jalov on 2018-02-27.
 */

@RunWith(Suite.class)
@Suite.SuiteClasses({RootManagerInstrumentedTest.class, ProcessUtilsInstrumentedTest.class, SshdServerInstrumentedTest.class})
public class AllInstrumentedTestsSuite {
}
