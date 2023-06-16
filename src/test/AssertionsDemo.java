import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;

import static java.time.Duration.ofMillis;
import static java.time.Duration.ofMinutes;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Assert static methods demo")
public class AssertionsDemo {

    private final Calculator cal = new Calculator();
    private final Idol karina = new Idol.Builder("카리나", 20).isLeader(1).build();

    @Test
    @DisplayName("Standard assertions")
    void standardAssertions() {
        assertEquals(2, cal.add(1, 1));
        assertEquals(4, cal.multiply(2, 2), "The optional failure message is now the last parameter");
        assertEquals("카리나", karina.getMemberName(), () -> karina.toString() + "의 이름이 카리나인지 확인");
    }

    @Test
    @DisplayName("Grouped assertions (Failures)")
    void groupAssertions() {

        assertAll("karina", () -> assertEquals("카리나", karina.getMemberName()), () -> assertEquals(20, karina.getAge()));

    }

    @Test
    void dependentAssertions() {

        assertAll("karina variables", () -> {
            String name = karina.getMemberName();
            assertNotNull(name);
            assertAll("name conventions rule", () -> assertEquals(3, name.length()), () -> assertTrue(name.startsWith("카")));

        }, () -> {
            int age = karina.getAge();
            assertAll("age conventions rule", () -> assertTrue(age > 0), () -> assertTrue(age < 30));
        });
    }

    @Test
    void exceptionTesting() {
        Exception exception = assertThrows(ArithmeticException.class, () -> cal.divide(1, 0));
        assertEquals("/ by zero", exception.getMessage());
    }

    @Test
    @DisplayName("timeoutNotExceeded")
    void timeoutNotExceeded() {
        String actualResult = assertTimeout(ofMinutes(2), () -> {
            return "a result";
        });
        assertEquals("a result", actualResult);
    }

    @Test
    @DisplayName("timeoutNotExceededWithMethod")
    void timeoutNotExceededWithMethod() {
        String actualGreeting = assertTimeout(ofMinutes(2), AssertionsDemo::greeting);
        assertEquals("Hello, World!", actualGreeting);
    }

    private static String greeting() {
        return "Hello, World!";
    }

    @Test
    @DisplayName("timeoutExceeded")
    void timeoutExceeded() {
        assertTimeout(ofMillis(10), () -> {
            Thread.sleep(100);
        });
    }

    @Test
    @DisplayName("third-party library")
    void thirdPartyLib() {

        assertThat(karina.getAge(), is(equalTo(20)));

    }

    @Test
    void timeoutExceededWithPreemptiveTermination() {
        // The following assertion fails with an error message similar to:
        // execution timed out after 10 ms
        assertTimeoutPreemptively(ofMillis(10), () -> {
            // Simulate task that takes more than 10 ms.
            new CountDownLatch(1).await();
        });
    }

}
