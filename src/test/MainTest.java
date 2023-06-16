import anno.Fast;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MainTest {


    @Fast
    @Test
    void test1(){
        assertEquals(1, 1);
    }

}
