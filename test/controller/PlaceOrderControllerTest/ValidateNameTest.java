package controller.PlaceOrderControllerTest;

import controller.PlaceOrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import utils.Strategy1;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidateNameTest {
    //Vu Trong Duc - 20183894
    private PlaceOrderController placeOrderController;
    @BeforeEach
    void setUp() {
//        Strategy1 strategy1 = new Strategy1();
        placeOrderController = new PlaceOrderController();
    }
    @ParameterizedTest
    @CsvSource({
            "Duc, true",
            "Vu Trong Duc, true",
            ",false",
            "Vu1 Duc, false",
            "VU@ Duc, false",
            "Vu1,false",
            "Vu@, false",
            "Vu!@ 1Trong, false"
    })
    void validateName(String name, boolean expected){
        //when
        boolean rs = placeOrderController.validateName(name);
        //then
        assertEquals(expected, rs);
    }
}
