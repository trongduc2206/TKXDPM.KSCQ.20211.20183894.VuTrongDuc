package controller;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.order.Order;
import views.screen.popup.PopupScreen;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

public class PlaceRushOrderController extends PlaceOrderController{
    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceRushOrderController.class.getName());
    // Vu Trong Duc - 20183894
    @Override
    public void validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException {
        if(!validateName(info.get("name"))){
            throw new InterruptedException("Name is invalid");
        }
        if(!validateAddress(info.get("address"))){
            throw new InterruptedException("Address is invalid");
        }
        if(!validatePhoneNumber(info.get("phone"))){
            throw new InterruptedException("Phone number is invalid");
        }
        if(!validateProvince(info.get("province"))) {
            throw new InterruptedException("Province does not support rush delivery. Please get back to Cart Screen to unselect rush order");
        }
        if(!validateDeliveryDate(info.get("date"))) {
            throw new InterruptedException("Delivery date must be after today");
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

    public boolean validateDeliveryDate(String date){
        if(date == null) {
            return false;
        }
        if(LocalDate.now().isAfter(LocalDate.parse(date)) || LocalDate.now().isEqual(LocalDate.parse(date))) {
            return false;
        }
        return true;
    }

    @Override
    public int calculateShippingFee(Order order) {
        Random rand = new Random();
        int fees = (int) (((rand.nextFloat() * 10) / 100) * order.getAmount()) + 10000* order.getlstOrderMedia().size();
        LOGGER.info("Order Amount: " + order.getAmount() + " -- Shipping Fees: " + fees);
        return fees;
    }
}
