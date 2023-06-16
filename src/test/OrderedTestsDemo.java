import org.junit.jupiter.api.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class OrderedTestsDemo {

    @Test
    @Order(1)
    void first() {
    }

    @Test
    @Order(2)
    void second() {
    }

    @Test
    @Order(3)
    void third() {

    }

}
