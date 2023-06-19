package inherit;

import org.junit.jupiter.api.*;

import java.util.logging.Logger;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public interface TestLifecycleLogger {

    static final Logger logger = Logger.getLogger(TestLifecycleLogger.class.getName());

    @BeforeAll
    default void beforeAllTests() {
        logger.info("Before all tests");
    }

    @AfterAll
    default void afterAllTests() {
        logger.info("After all tests");
    }

    @BeforeEach
    default void beforeEach(TestInfo testInfo) {
        logger.info(() -> String.format("About to execute [%s]", testInfo.getDisplayName()));
    }

    @AfterEach
    default void afterEach(TestInfo testInfo) {
        logger.info(() -> String.format("Finished executing [%s]", testInfo.getDisplayName()));
    }

}
