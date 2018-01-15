import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import ru.yandex.qatools.allure.annotations.Title;

import static utils.Mock.*;

public class Tests {

    private Steps steps = new Steps();

    @BeforeClass
    public static void beforeAll(){ startWireMock(); }

    @AfterClass
    public static void afterAll(){
        stopWireMock();
    }

    @Title("1. Проверка метода добавления товаров")
    @Test
    public void t1() {
        steps.t1s1();
        steps.t1s2();
        steps.t1s3();
        steps.t1s4();
        steps.t1s5();
        steps.t1s6();
        steps.t1s7();
        steps.t1s8();
        steps.t1s9();
        steps.t1s10();
        steps.t1s11();
    }

    @Title("2. Проверка метода получения данных о товаре")
    @Test
    public void t2() {
        steps.t2s1();
        steps.t2s2();
        steps.t2s3();
        steps.t2s4();
    }

}
