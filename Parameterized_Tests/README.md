# Parameterized Tests

### `@ValueSource`

- 리터럴 값을 담는 1차배열 가능
    - `short`, `byte`, `int`, `long`, `float`, `double`, `char`, `boolean`, `java.lang.String`, `java.lang.Class`
- `@AutoCloseable` 구현체를 사용하려면 `@ParameterizedTest(autoCloseArguments = false)`를 통해 비활성화해서 재사용 가능

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParamTest {

    @ParameterizedTest
    @ValueSource(strings = {"karina", "minzi", "hani"})
    void test1(String name) {
        assertTrue(name.length() < 7);
    }

    @ParameterizedTest
    @NullAndEmptySource // @NullSource + @EmptySource
    @ValueSource(strings = {" ", "   ", "\t", "\n"})
    void nullEmptyAndBlankStrings(String text) {
        assertTrue(text == null || text.trim().isEmpty());
    }
}
```

<img src="img_6.png"  width="30%"/>

### `@EnumSource`

- Enum 상수 주입

```java

public class ParamTest {

    private Logger logger = Logger.getLogger(ParamTest.class.getName());

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

}
```

### `@MethodSource`

- 1개 이상의 팩터리 메서드를 참조

```java
import org.junit.jupiter.api.extension.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

public class ParamTest {

    private Logger logger = Logger.getLogger(ParamTest.class.getName());

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
        return Stream.of(
                Arguments.of("NewJeans", 1, List.of("hani", "minzi")),
                Arguments.of("IVE", 2, List.of("Rei")),
                Arguments.of("IU", 3, List.of("solo"))
        );
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
            return Stream.of(
                    Arguments.of("NewJeans", 1, List.of("hani", "minzi")),
                    Arguments.of("IVE", 2, List.of("Rei")),
                    Arguments.of("IU", 3, List.of("solo"))
            );
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
        return Stream.of(
                arguments(quantity + " apples"),
                arguments(quantity + " lemons")

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
}
```

### `@CsvSource`

- CSV 포맷의 문자열을 주입

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;

/**
 * @ParameterizedTest 예시
 */
public class ParamTest {

    private Logger logger = Logger.getLogger(ParamTest.class.getName());

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
}
```

### `@CsvFileSource`

- CSV 파일을 주입

```java

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;

import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ParamTest {

    private Logger logger = Logger.getLogger(ParamTest.class.getName());

    @ParameterizedTest
    @CsvFileSource(resources = "resource/Idol.csv", numLinesToSkip = 1)
    void testWithCsvFileSource(String name, String group) {
        logger.info(name + " " + group);
        assertNotNull(name);
        assertNotNull(group);
    }
}
```

### `@ArgumentsSource`

- `ArgumentsProvider` 구현체를 통해 인자를 주입

```java
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.ArgumentsProvider;
import org.junit.jupiter.params.provider.ArgumentsSource;

import java.util.logging.Logger;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ParamTest {

    private Logger logger = Logger.getLogger(ParamTest.class.getName());


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

```

## Argument Conversion

### Widening Conversion

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ArgumentConversionTest {
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void testWithWideningArgumentConversion(double argument) {
        assertNotNull(argument); // argument = 1.0, 2.0, 3.0
    }
}

```

### Implicit  Conversion

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.temporal.ChronoUnit;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ArgumentConversionTest {

    @ParameterizedTest
    @ValueSource(strings = "SECONDS")
    void testWithImplicitArgumentConversion(ChronoUnit argument) {
        assertNotNull(argument.name()); // argument.Enum.name = SECONDS
    }
}
```

### Explicit Conversion

- `ArgumentConverter` 구현체와 `@ConvertWith` 어노테이션을 이용

```java
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.JavaTimeConversionPattern;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ArgumentConversionTest {

    private Logger logger = Logger.getLogger(ArgumentConversionTest.class.getName());


    @ParameterizedTest
    @EnumSource(ChronoUnit.class)
    void testWithExplicitArgumentConversion(@ConvertWith(ToStringArgumentConverter.class) String argument) {
        logger.info("argument = " + argument); // argument = SECONDS
        assertNotNull(argument); // argument = ChronoUnit
    }

    private static class ToStringArgumentConverter extends SimpleArgumentConverter {
        @Override
        protected Object convert(Object o, Class<?> aClass) throws ArgumentConversionException {
            assertEquals(String.class, aClass, "Can only convert to String");
            if (o instanceof Enum<?>) {
                return ((Enum<?>) o).name();
            }
            return String.valueOf(o);
        }
    }

    @ParameterizedTest
    @ValueSource(strings = {"01.01.2017", "31.12.2017"})
    void testWithExplicitJavaTimeConverter(@JavaTimeConversionPattern("dd.MM.yyyy") LocalDate argument) {
        assertEquals(2017, argument.getYear());
    }
}
```

### Argument Aggregation

- `ArgumentsAccessor` 구현체
- `@interface`으로 커스텀 가능

```java
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.provider.CsvSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ArgumentConversionTest {

    private Logger logger = Logger.getLogger(ArgumentConversionTest.class.getName());

    @ParameterizedTest
    @CsvSource({
            "Karina, 20, 1, 2000-04-11",
            "Minzi, 17, 1, 2004-05-07",
    })
    void testWithArgumentAccessor(ArgumentsAccessor arguments) {
        Idol idol = new Idol.Builder(arguments.getString(0), arguments.getInteger(1))
                .isLeader(arguments.getInteger(2)).birthDate(arguments.get(3, LocalDate.class))
                .build();

        assertNotNull(idol);
        logger.info(idol.toString());
    }

    @ParameterizedTest
    @CsvSource({
            "Karina, 20, 1, 2000-04-11",
            "Minzi, 17, 1, 2004-05-07",
    })
    void testWithArgumentsAggregator(@AggregateWith(IdolAggregator.class) Idol idol) {
        assertNotNull(idol);
        logger.info(idol.toString());
    }


    private static class IdolAggregator implements ArgumentsAggregator {

        @Override
        public Idol aggregateArguments(ArgumentsAccessor argumentsAccessor, ParameterContext parameterContext) throws ArgumentsAggregationException {
            return new Idol.Builder(argumentsAccessor.getString(0), argumentsAccessor.getInteger(1))
                    .isLeader(argumentsAccessor.getInteger(2)).birthDate(argumentsAccessor.get(3, LocalDate.class))
                    .build();
        }
    }

    @ParameterizedTest
    @CsvSource({
            "Karina, 20, 1, 2000-04-11",
            "Minzi, 17, 1, 2004-05-07",
    })
    void testWithArgumentsAggregatorAnnotation(@CsvToIdol Idol idol) {
        assertNotNull(idol);
        logger.info(idol.toString());
    }


    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.PARAMETER)
    @AggregateWith(IdolAggregator.class)
    private @interface CsvToIdol {
    }

}

```

## Customizing Display Name

- `displayName` : 메서드 이름
- `index` : invocation index
- `arguments` : comma-separated 인수
- `argumentsWithNames` : 인수 + 파라미터 이름
- `{0}`, `{1}` : 각 인수

```java
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
```
