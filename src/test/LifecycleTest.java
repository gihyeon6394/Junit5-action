import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class LifecycleTest {

    private int flag = 0;

    @Test
    void plus1() {
        flag++;
        System.out.println("flag : " + flag);
    }

    @Test
    void plus2() {
        flag++;
        System.out.println("flag : " + flag);
    }
}
