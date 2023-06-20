import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Named.named;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class CustomDisplayNameTest {

    private Logger logger = Logger.getLogger(CustomDisplayNameTest.class.getName());

    @DisplayName("Display name of container")
    @ParameterizedTest(name = "{index} ==> the age of ''{0}'' is {1}")
    @CsvSource({"Karina, 22", "Minzi, 18"})
    void testWithCustomDisplayNames(String name, int age) {
        assertTrue(age > 0);
        logger.info("name: " + name + ", age: " + age);
    }

    @DisplayName("A parameterized test with custom display name")
    @ParameterizedTest(name = "{index}: {0}")
    @MethodSource("namedArguments")
    void testWithNamedArguments(int age) {
        logger.info("age : " + age);
        assertTrue(age > 0);
    }

    static Stream<Arguments> namedArguments() {
        return Stream.of(
                arguments(named("karina age", 22))
                , arguments(named("Minzi age", 18))
        );
    }
}
