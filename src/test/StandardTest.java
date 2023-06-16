import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.fail;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@DisplayName("A Standard Test")
public class StandardTest {
    @BeforeAll
    static void initAll() {
    }


    @BeforeEach
    void init() {
    }

    @Test
    void succeedingTest() {
    }

    @Test
    @DisplayName("😱")
    void failingTest() {
        fail("a failing test");
    }

    @Test
    @Disabled("for demonstration purposes")
    void skippedTest() {
        // not executed
    }

    @Test
    @DisplayName("must failed 2")
    void abortedTest() {
        assumeTrue("abc".contains("Z"));
        fail("test should have been aborted");
    }

    @AfterEach
    void tearDown() {
    }

    @AfterAll
    static void tearDownAll() {
    }
}
