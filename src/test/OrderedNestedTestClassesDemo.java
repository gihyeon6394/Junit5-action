import org.junit.jupiter.api.*;

@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class OrderedNestedTestClassesDemo {

    @Nested
    @Order(1)
    class FirstTest {
        @Test
        void test1() {
        }
    }

    @Nested
    @Order(2)
    class SecondTest {
        @Test
        void test2() {
        }
    }
}
