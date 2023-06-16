import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;
import static org.junit.jupiter.api.Assumptions.assumingThat;

public class AssumptionsDemo {

    private final Idol karina = new Idol.Builder("카리나", 20).isLeader(1).build();

    @Test
    @DisplayName("test only on ci server")
    void testOnlyCiServer() {
        assumeTrue("CI".equals(System.getenv("ENV")));
    }

    @Test
    @DisplayName("test only on dev server")
    void testOnlyDevServer() {
        assumeTrue("DEV".equals(System.getenv("ENV")),
                () -> "Aborting test: not on developer workstation");
    }

    @Test
    @DisplayName("CI server test and All environment test")
    void testInAllEnvironments() {
        assumingThat("CI".equals(System.getenv("ENV")),
                () -> {
                    // perform only in CI server
                    assertEquals(20, karina.getAge());
                });

        assertEquals("카리나", karina.getMemberName());
    }

    @Test
    @Disabled("Disabled until ready for production server")
    void testProduction() {

    }
}
