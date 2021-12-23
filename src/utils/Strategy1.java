package utils;

import controller.PlaceOrderController;
import entity.order.Order;

import java.util.logging.Logger;

public class Strategy1 implements  ShippingFeeCalculator{
    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(Strategy1.class.getName());
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
    @Override
    public int calculateShippingFee(Order order) {
        int fees = 0;
        int actualWeight = order.getTotalWeight();
        int altWeight = order.getTotalAlternativeWeight();
        int weightToGetFee = actualWeight + altWeight;
        int amount = order.getAmount();
        if(amount > 100000){
            LOGGER.info("Order amount > 100000 -- Free shipping fee");
            return fees;
        }
        if(order.getDeliveryInfo().containsKey("province")){
            String province = String.valueOf(order.getDeliveryInfo().get("Province"));
            if(province.equals("Hà Nội") || province.equals("Hồ Chí Minh")){
                if(weightToGetFee >= 3){
                    fees = CITY_FIRST_FEE + NEXT_FEE * (int) ( (weightToGetFee - 3)/0.5);
                } else {
                    fees = CITY_FIRST_FEE * weightToGetFee;
                }
            } else {
                if(weightToGetFee >= 3){
                    fees = OTHER_FIRST_FEE + NEXT_FEE * (int) ( (weightToGetFee - 3)/0.5);
                } else {
                    fees = OTHER_FIRST_FEE * weightToGetFee;
                }
            }
        }
        LOGGER.info("Order Amount: " + order.getAmount() + " -- Shipping Fees: " + fees);
        return fees;
    }
}
