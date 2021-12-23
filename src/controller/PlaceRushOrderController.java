package controller;

import entity.cart.Cart;
import entity.cart.CartMedia;
import entity.order.Order;
import utils.Strategy1;
import views.screen.popup.PopupScreen;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;
import java.util.logging.Logger;

/**
 * Class xu ly logic dat hang giao hang nhanh
 * @author Vu Trong Duc - 20183894
 * @since 12/10/2021
 * @version 1.0
 */
public class PlaceRushOrderController extends PlaceOrderController{
    /**
     * Just for logging purpose
     */
    private static Logger LOGGER = utils.Utils.getLogger(PlaceRushOrderController.class.getName());
    // Vu Trong Duc - 20183894

    public PlaceRushOrderController(Strategy1 strategy1){
        super(strategy1);
    }

    /**
     * Method kiem tra tinh hop le thong tin giao hang
     * @param info thong tin don giao hang nhanh
     * @throws InterruptedException
     * @throws IOException
     */
    @Override
    public void validateDeliveryInfo(HashMap<String, String> info) throws InterruptedException, IOException {
        // kiem tra tinh hop le cua ten, dia chi, so dien thoai
        super.validateDeliveryInfo(info);
        // kiem tra tinh hop le cua tinh/thanh pho
        if(!validateProvince(info.get("province"))) {
            throw new InterruptedException("Province does not support rush delivery. Please get back to Cart Screen to unselect rush order");
        }
        // kiem tra tinh hop le cua ngay giao hang
        if(!validateDeliveryDate(info.get("date"))) {
            throw new InterruptedException("Delivery date must be after today");
        }
    }
    //Vu Trong Duc - 20183894

    /**
     * Method kiem tra tinh hop le cua thong tin tinh/thanh pho
     * @param province ten tinh/thanh pho
     * @return tinh hop le cua thong tin (dang boolean)
     */
    public boolean validateProvince(String province){
        //kiem tra ten tinh/thanh pho khac null hoac rong
        if(province == null || province.isEmpty()){
            return false;
        }
        //kiem tra ten tinh/thanh pho la Ha Noi
        if(!province.equals("Hà Nội")) {
            return false;
        }
        return true;
    }
    //Vu Trong Duc - 20183894
    /**
     * Method kiem tra tinh hop le cua thong tin ngay giao hang
     * @param date ngay giao hang
     * @return tinh hop le cua ngay giao hang (dang boolean)
     */
    public boolean validateDeliveryDate(String date){
        //kiem tra ngay giao hang khac null
        if(date == null) {
            return false;
        }
        //kiem tra ngay giao hang sau ngay hien tai
        if(LocalDate.now().isAfter(LocalDate.parse(date)) || LocalDate.now().isEqual(LocalDate.parse(date))) {
            return false;
        }
        return true;
    }
    //Vu Trong Duc - 20183894
    /**
     * Method tinh phi van chuyen cho don giao hang nhanh
     * @param order don hang
     * @return phi van chuyen (dang int)
     */
    @Override
    public int calculateShippingFee(Order order) {
        // tinh phi van chuyen
        int fees = super.calculateShippingFee(order);
        // tinh phi giao hang nhanh cong them
        int rushOrderFee = 10000 * order.getlstOrderMedia().size();
        // tinh tong phi
        int totalFees = fees + rushOrderFee;
        LOGGER.info("Order Amount: " + order.getAmount() + " -- Shipping Fees: " + totalFees);
        return totalFees;
    } // Vu Trong Duc - 20183894
}
