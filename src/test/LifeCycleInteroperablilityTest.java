import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.TestInfo;
import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.logging.Logger;

public class LifeCycleInteroperablilityTest {

    private Logger logger = Logger.getLogger(LifeCycleInteroperablilityTest.class.getName());


    @BeforeEach
    void beforeEach(TestInfo testInfo) {
        logger.info("beforeEach() testInfo : " + testInfo.toString());
    }

    @ParameterizedTest
    @ValueSource(strings = "apple")
    void testWithRegularParameterResolver(String argument, TestReporter testReporter) {
        testReporter.publishEntry("argument", argument);
        logger.info("testWithRegularParameterResolver() testReporter : " + testReporter);
    }

    @AfterEach
    void afterEach(TestInfo testInfo) {
        logger.info("AfterEach() testInfo : " + testInfo.toString());

    }
}
