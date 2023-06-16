import anno.TestOnMac;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.condition.*;

public class ConditionTest {

    @Test
    @EnabledOnOs(OS.MAC)
    void onlyOnMacOS() {

    }

    @TestOnMac
    void testOnMac() {

    }

    @Test
    @EnabledOnOs({OS.LINUX, OS.MAC})
    void onLinuxOrMac() {

    }

    @Test
    @EnabledOnOs(architectures = "x86_64")
    void onX86() {

    }

    @Test
    @EnabledOnOs(value = OS.MAC, architectures = "aarch64")
    void onNewMac() {

    }

    @Test
    @EnabledOnJre({JRE.JAVA_8, JRE.JAVA_9})
    void onlyOnJava89() {

    }

    @Test
    @EnabledForJreRange(min = JRE.JAVA_9)
    void onlyFromJava9() {

    }

}
