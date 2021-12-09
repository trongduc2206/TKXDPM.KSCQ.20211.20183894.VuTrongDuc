package controller;

import entity.cart.Cart;
import entity.cart.CartMedia;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.logging.Logger;

public class PlaceRushOrderController extends PlaceOrderController{
    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceRushOrderController.class.getName());
    // Vu Trong Duc - 20183894
    @Override
    public void validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException {
        if(validateName(info.get("name"))){
            throw new InterruptedException("Name is invalid");
        }
        if(validateAddress(info.get("address"))){
            throw new InterruptedException("Address is invalid");
        }
        if(validatePhoneNumber(info.get("phone"))){
            throw new InterruptedException("Phone number is invalid");
        }
        if(validateProvince(info.get("province"))) {
            throw new InterruptedException("Province does not support rush delivery");
        }
    }

    public boolean validateProvince(String province){
        if(province == null || province.isEmpty()){
            return false;
        }
        if(!province.equals("Hà Nội")) {
            return false;
        }
        return true;
    }
}
