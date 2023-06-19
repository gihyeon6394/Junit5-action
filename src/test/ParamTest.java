import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;

import java.time.temporal.ChronoUnit;
import java.util.EnumSet;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;
import static org.junit.jupiter.params.provider.EnumSource.Mode.EXCLUDE;
import static org.junit.jupiter.params.provider.EnumSource.Mode.MATCH_ALL;

/**
 * @ParameterizedTest 예시
 */
public class ParamTest {

    private Logger logger = Logger.getLogger(ParamTest.class.getName());

    @ParameterizedTest
    @ValueSource(strings = {"karina", "minzi", "hani"})
    void test1(String name) {
        assertTrue(name.length() < 7);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void testWithValueSource(int argument) {
        assertTrue(argument > 0 && argument < 4);
    }

    @ParameterizedTest
    @NullAndEmptySource // @NullSource + @EmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void nullEmptyAndBlankStrings(String text) {
        assertTrue(text == null || text.trim().isEmpty());
    }

    @ParameterizedTest
    @EnumSource
    void testWithEnumSourceWithAutoDetection(ChronoUnit unit) {
        logger.info(unit.toString());
        assertNotNull(unit);
    }

    @ParameterizedTest
    @EnumSource(names = {"DAYS", "HOURS"})
    void testWithEnumSourceInclude(ChronoUnit unit) {
        logger.info(unit.toString());
        assertTrue(EnumSet.of(ChronoUnit.DAYS, ChronoUnit.HOURS).contains(unit));
    }

    @ParameterizedTest
    @EnumSource(mode = EXCLUDE, names = {"ERAS", "FOREVER"})
    void testEnumSourceExclude(ChronoUnit unit) {
        logger.info(unit.toString());
        assertFalse(EnumSet.of(ChronoUnit.ERAS, ChronoUnit.FOREVER).contains(unit));
    }

    @ParameterizedTest
    @EnumSource(mode = MATCH_ALL, names = "^.*DAYS$")
    void testWithEnumSourceRegex(ChronoUnit unit) {
        assertTrue(unit.name().endsWith("DAYS"));
    }

    @ParameterizedTest
    @MethodSource("idolProvider")
    void testWithExplicitLocalMethodSource(String argument) {
        logger.info(argument);
        assertNotNull(argument);
    }

    static Stream<String> idolProvider() {
        return Stream.of("karina", "minzi", "hani");
    }

    @ParameterizedTest
    @MethodSource
    void testWithDefaultLocalMethodSource(String argument) {
        logger.info(argument);
        assertNotNull(argument);
    }

    static Stream<String> testWithDefaultLocalMethodSource() {
        return Stream.of("karina", "minzi", "hani");
    }

    @ParameterizedTest
    @MethodSource("idolGroupProvider")
    void testWithMultiArgMethodSource(String groupName, int num, List<String> memberList) {
        assertNotNull(groupName);
        assertTrue(num >= 1);
        assertTrue(memberList.size() > 0);
    }

    static Stream<Arguments> idolGroupProvider() {
        return Stream.of(Arguments.of("NewJeans", 1, List.of("hani", "minzi")), Arguments.of("IVE", 2, List.of("Rei")), Arguments.of("IU", 3, List.of("solo")));
    }

    @ParameterizedTest
    @MethodSource("IdolProvider#idolGroupProvider")
    void testWithExternalMethodSource(String groupName, int num, List<String> memberList) {
        assertNotNull(groupName);
        assertTrue(num >= 1);
        assertTrue(memberList.size() > 0);
    }

    class IdolProvider {
        static Stream<Arguments> idolGroupProvider() {
            return Stream.of(Arguments.of("NewJeans", 1, List.of("hani", "minzi")), Arguments.of("IVE", 2, List.of("Rei")), Arguments.of("IU", 3, List.of("solo")));
        }
    }

    @ParameterizedTest
    @MethodSource("range")
    void testWIthRangeMethodSource(int argument) {
        logger.info("argument : " + argument);
        assertNotEquals(9, argument);
    }

    static IntStream range() {
        return IntStream.range(0, 20).skip(10);
    }

    @RegisterExtension // IntegerResolver 확장
    static final IntegerResolver integerResolver = new IntegerResolver();

    @ParameterizedTest
    @MethodSource("factoryMethodWithArguments")
    void testWithFactoryMethodWithArguments(String argument) {
        assertTrue(argument.startsWith("2"));
    }

    static Stream<Arguments> factoryMethodWithArguments(int quantity) {
        return Stream.of(arguments(quantity + " apples"), arguments(quantity + " lemons")

        );
    }

    static class IntegerResolver implements ParameterResolver {


        @Override
        public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
            return parameterContext.getParameter().getType() == int.class;
        }

        @Override
        public Object resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
            return 2;
        }
    }


    @ParameterizedTest
    @CsvSource({"karina, Aespa", "minzi, NewJeans", "hani, NewJeans"})
    void testWithCsvSource(String name, String group) {
        logger.info(name + " " + group);
        assertNotNull(name);
        assertNotNull(group);
    }

    @ParameterizedTest(name = "[{index}] {arguments}")
    @CsvSource(useHeadersInDisplayName = true, textBlock = """
            name, group
            karina, Aespa
            minzi, NewJeans
            hani, NewJeans
            """)
    void testWithCsvSourceWithHeader(String name, String group) {
        logger.info(name + " " + group);
        assertNotNull(name);
        assertNotNull(group);
    }

    @ParameterizedTest
    @CsvSource(useHeadersInDisplayName = true, delimiter = '|', quoteCharacter = '"', textBlock = """
            name| group
            "karina"| "Aespa"
            "minzi"| "NewJeans"
            "hani"| "NewJeans"
            """)
    void testWithCsvSourceWithDelimiterAndQuote(String name, String group) {
        logger.info(name + " " + group);
        assertNotNull(name);
        assertNotNull(group);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "resource/Idol.csv", numLinesToSkip = 1)
    void testWithCsvFileSource(String name, String group) {
        logger.info(name + " " + group);
        assertNotNull(name);
        assertNotNull(group);
    }

    @ParameterizedTest
    @ArgumentsSource(IdolArgumentProvider.class)
    void testWithArgumentsSource(String name) {
        logger.info(name);
        assertNotNull(name);
    }


    static class IdolArgumentProvider implements ArgumentsProvider {

        @Override
        public Stream<? extends Arguments> provideArguments(ExtensionContext extensionContext) {
            return Stream.of("karina", "minzi", "hani").map(Arguments::of);
        }
    }
}
