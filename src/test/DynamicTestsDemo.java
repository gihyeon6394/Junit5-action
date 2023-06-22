import org.junit.jupiter.api.*;
import org.junit.jupiter.api.function.ThrowingConsumer;
import org.junit.jupiter.params.ParameterizedTest;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class DynamicTestsDemo {

    private final Idol karina = new Idol.Builder("카리나", 20).isLeader(1).build();
    private final Idol giselle = new Idol.Builder("지젤", 22).build();
    private final Idol minzi = new Idol.Builder("민지", 19).build();
    private final Idol hani = new Idol.Builder("하니", 20).build();


//    @TestFactory
//    List<String> dynamicTestsWithInvalidReturnType() {
//        return Arrays.asList("Karina!!");
//    }

    @TestFactory
    DynamicNode simpleDynamicTest() {
        return DynamicTest.dynamicTest("karina age", () -> assertTrue(karina.getAge() > 19));
    }

    @TestFactory
    Collection<DynamicTest> dynamicTestsFromCollection() {
        return Arrays.asList(DynamicTest.dynamicTest("1 karina age", () -> assertTrue(karina.getAge() > 19)), DynamicTest.dynamicTest("2 giselle age", () -> assertTrue(giselle.getAge() > 19)));
    }

    @TestFactory
    Stream<DynamicTest> dynamicTestFromStream() {
        return Stream.of("karina", "giselle", "minzi", "hani").map(name -> DynamicTest.dynamicTest(name, () -> assertTrue(name.length() > 0)));
    }

    @TestFactory
    Stream<DynamicTest> generateRandomNumberOfTestsFromIterator() {

        Iterator<Integer> inputGenerator = new Iterator<Integer>() {
            Random rnd = new Random();
            int current;

            @Override
            public boolean hasNext() {
                current = rnd.nextInt(100);
                return current % 7 != 0;
            }

            @Override
            public Integer next() {
                return current;
            }
        };

        Function<Integer, String> displayNameGenerator = (input) -> "input: " + input;
        ThrowingConsumer<Integer> testExecutor = (input) -> assertTrue(input % 7 != 0);
        return DynamicTest.stream(inputGenerator, displayNameGenerator, testExecutor);
    }


}
