import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.aggregator.AggregateWith;
import org.junit.jupiter.params.aggregator.ArgumentsAccessor;
import org.junit.jupiter.params.aggregator.ArgumentsAggregationException;
import org.junit.jupiter.params.aggregator.ArgumentsAggregator;
import org.junit.jupiter.params.converter.ArgumentConversionException;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.converter.JavaTimeConversionPattern;
import org.junit.jupiter.params.converter.SimpleArgumentConverter;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class ArgumentConversionTest {

    private Logger logger = Logger.getLogger(ArgumentConversionTest.class.getName());


    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3})
    void testWithWideningArgumentConversion(double argument) {
        assertNotNull(argument); // argument = 1, 2, 3
    }

    @ParameterizedTest
    @ValueSource(strings = "SECONDS")
    void testWithImplicitArgumentConversion(ChronoUnit argument) {
        assertNotNull(argument.name()); // argument.Enum.name = SECONDS
    }


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
