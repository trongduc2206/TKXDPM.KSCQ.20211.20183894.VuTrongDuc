package controller.PlaceOrderControllerTest;

import controller.PlaceOrderController;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValidateAddressTest {
    //Vu Trong Duc - 20183894
    private PlaceOrderController placeOrderController;
    @BeforeEach
    void setUp() {
        placeOrderController = new PlaceOrderController();
    }
    @ParameterizedTest
    @CsvSource({
            "So 227 Van Cao, true",
            "So @ 227 Van Cao, false",
            ", false"
    })
    void validateAddress(String address, boolean expected){
        //when
        boolean rs = placeOrderController.validateAddress(address);
        //then
        assertEquals(expected, rs);
    }
}
