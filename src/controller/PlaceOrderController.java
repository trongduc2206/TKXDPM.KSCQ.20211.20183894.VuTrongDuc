package controller;

import java.io.IOException;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.logging.Logger;

import entity.cart.Cart;
import entity.cart.CartMedia;
import common.exception.InvalidDeliveryInfoException;
import entity.invoice.Invoice;
import entity.order.Order;
import entity.order.OrderMedia;
import utils.ShippingFeeCalculator;
import utils.Strategy1;
import views.screen.popup.PopupScreen;

/**
 * This class controls the flow of place order usecase in our AIMS project
 *
 * @author nguyenlm
 */
public class PlaceOrderController extends BaseController {

    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceOrderController.class.getName());

    private ShippingFeeCalculator shippingFeeCalculator;

    /**
     * The shipping fee for first 3kg in Ha Noi or Ho Chi Minh
     */
    private static final int CITY_FIRST_FEE = 22000;
    /**
     * The shipping fee for first 3kg in other province
     */
    private static final int OTHER_FIRST_FEE = 30000;
    /**
     * The shipping fee for each 0.5kg next
     */
    private static final int NEXT_FEE = 2500;

    public PlaceOrderController(Strategy1 strategy1){
        this.shippingFeeCalculator = strategy1;
    }

    /**
     * This method checks the avalibility of product when user click PlaceOrder button
     *
     * @throws SQLException
     */
    public void placeOrder() throws SQLException {
        Cart.getCart().checkAvailabilityOfProduct();
    }

    /**
     * This method creates the new Order based on the Cart
     *
     * @return Order
     * @throws SQLException
     */
    public Order createOrder() throws SQLException {
        Order order = new Order();
        for (Object object : Cart.getCart().getListMedia()) {
            CartMedia cartMedia = (CartMedia) object;
            OrderMedia orderMedia = new OrderMedia(cartMedia.getMedia(),
                    cartMedia.getQuantity(),
                    cartMedia.getPrice());
            order.getlstOrderMedia().add(orderMedia);
        }
        return order;
    }

    /**
     * This method creates the new Invoice based on order
     *
     * @param order
     * @return Invoice
     */
    public Invoice createInvoice(Order order) {
        return new Invoice(order);
    }

    /**
     * This method takes responsibility for processing the shipping info from user
     *
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
    public void processDeliveryInfo(HashMap info) throws InterruptedException, IOException {
        LOGGER.info("Process Delivery Info");
        LOGGER.info(info.toString());
        validateDeliveryInfo(info);
    }

    /**
     * The method validates the info
     *
     * @param info
     * @throws InterruptedException
     * @throws IOException
     */
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
    }

    /**
     * The method validates the phone number
     * @param phoneNumber phone number to validate
     * @return
     */
    public boolean validatePhoneNumber(String phoneNumber) {
        // check the phone number is not null or empty
        if(phoneNumber.isEmpty() || phoneNumber == null) return false;
        // check the phone number has 10 digits
        if (phoneNumber.length() != 10) return false;
        // check the phone number start with 0
        if (!phoneNumber.startsWith("0")) return false;
        // check the phone number contains only number
        try {
            Integer.parseInt(phoneNumber);
        } catch (NumberFormatException e) {
    	    return false;
        }
        return true;
    }

    /**
     * The method validate the name
     * @param name name to validate
     * @return
     */
    public boolean validateName(String name) {
        // check the name is not null or empty
        if(name == null || name.isEmpty() ){
            return false;
        }
        //check the name does not contain special characters or number
        if(!name.matches("^[a-zA-Z\\s]*$")) {
            return false;
        }
        return true;
    }

    public boolean validateAddress(String address) {
        //check the address is not null or empty
        if(address == null || address.isEmpty()) {
            return false;
        }
        // check the address does not contain special characters
        if(!address.matches("^[a-zA-Z0-9\\s]*$")) {
            return false;
        }
        return true;
    }


    /**
     * This method calculates the shipping fees of order
     *
     * @param order
     * @return shippingFee
     */
    public int calculateShippingFee(Order order) { //VU TRONG DUC - 20183894
//        int fees = 0;
//        int actualWeight = order.getTotalWeight();
//        int altWeight = order.getTotalAlternativeWeight();
//        int weightToGetFee = actualWeight + altWeight;
//        int amount = order.getAmount();
//        if(amount > 100000){
//            LOGGER.info("Order amount > 100000 -- Free shipping fee");
//            return fees;
//        }
//        if(order.getDeliveryInfo().containsKey("province")){
//            String province = String.valueOf(order.getDeliveryInfo().get("Province"));
//            if(province.equals("Hà Nội") || province.equals("Hồ Chí Minh")){
//                if(weightToGetFee >= 3){
//                    fees = CITY_FIRST_FEE + NEXT_FEE * (int) ( (weightToGetFee - 3)/0.5);
//                } else {
//                    fees = CITY_FIRST_FEE * weightToGetFee;
//                }
//            } else {
//                if(weightToGetFee >= 3){
//                    fees = OTHER_FIRST_FEE + NEXT_FEE * (int) ( (weightToGetFee - 3)/0.5);
//                } else {
//                    fees = OTHER_FIRST_FEE * weightToGetFee;
//                }
//            }
//        }
//        LOGGER.info("Order Amount: " + order.getAmount() + " -- Shipping Fees: " + fees);
//        return fees;
        return shippingFeeCalculator.calculateShippingFee(order);
    }
}
