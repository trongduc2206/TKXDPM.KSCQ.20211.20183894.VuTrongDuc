package views.screen.shipping;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.ResourceBundle;

import controller.PlaceOrderController;
import common.exception.InvalidDeliveryInfoException;
import controller.PlaceRushOrderController;
import entity.invoice.Invoice;
import entity.order.Order;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;
import views.screen.invoice.InvoiceScreenHandler;
import views.screen.popup.PopupScreen;

public class ShippingScreenHandler extends BaseScreenHandler implements Initializable {
	@FXML
	private ImageView aimsImage;

	@FXML
	private Label screenTitle;

	@FXML
	private TextField name;

	@FXML
	private TextField phone;

	@FXML
	private TextField address;

	@FXML
	private TextField instructions;

	@FXML
	private ComboBox<String> province;

	@FXML
	private Label backLabel;

	@FXML
	private DatePicker datePicker;

	@FXML
	private Label deliveryDate;

	private Order order;

	private boolean isRushOrder;

	public ShippingScreenHandler(Stage stage, String screenPath, Order order) throws IOException {
		super(stage, screenPath);
		this.order = order;
		// fix relative image path caused by fxml
		File file = new File("assets/images/Logo.png");
		Image im = new Image(file.toURI().toString());
		aimsImage.setImage(im);

		// on mouse clicked, we back to home
		aimsImage.setOnMouseClicked(e -> {
			homeScreenHandler.show();
		});

		backLabel.setOnMouseClicked(e -> {
			getPreviousScreen().show();
		});
	}

	public void setIsRushOrder(boolean isRushOrder){
		this.isRushOrder = isRushOrder;
	}

	public void setDatePickerVisible(boolean isVisible){
		this.datePicker.setVisible(isVisible);
		this.deliveryDate.setVisible(isVisible);
	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		final BooleanProperty firstTime = new SimpleBooleanProperty(true); // Variable to store the focus on stage load
		name.focusedProperty().addListener((observable,  oldValue,  newValue) -> {
            if(newValue && firstTime.get()){
                content.requestFocus(); // Delegate the focus to container
                firstTime.setValue(false); // Variable value changed for future references
            }
        });
		this.province.getItems().addAll(Configs.PROVINCES);
	}

	@FXML
	void submitDeliveryInfo(MouseEvent event) throws IOException, InterruptedException, SQLException, InvalidDeliveryInfoException {

		// add info to messages
		HashMap messages = new HashMap<>();
		messages.put("name", name.getText());
		messages.put("phone", phone.getText());
		messages.put("address", address.getText());
		messages.put("instructions", instructions.getText());
		messages.put("province", province.getValue());
		messages.put("date", (datePicker.getValue()!=null)?datePicker.getValue().toString():null);
		// process and validate delivery info
		try {
			if (isRushOrder) {
				getRController().processDeliveryInfo(messages);
			} else {
				getBController().processDeliveryInfo(messages);
			}
		} catch (InterruptedException e){
			PopupScreen.error(e.getMessage());
			return;
		}
		// calculate shipping fees
		order.setDeliveryInfo(messages);
		int shippingFees = isRushOrder ? getRController().calculateShippingFee(order) :getBController().calculateShippingFee(order);
		order.setShippingFees(shippingFees);

		
		// create invoice screen
		Invoice invoice = getBController().createInvoice(order);
		BaseScreenHandler InvoiceScreenHandler = new InvoiceScreenHandler(this.stage, Configs.INVOICE_SCREEN_PATH, invoice);
		InvoiceScreenHandler.setPreviousScreen(this);
		InvoiceScreenHandler.setHomeScreenHandler(homeScreenHandler);
		InvoiceScreenHandler.setScreenTitle("Invoice Screen");
		InvoiceScreenHandler.setBController(getBController());
		InvoiceScreenHandler.show();
	}

	public PlaceOrderController getBController(){
		return (PlaceOrderController) super.getBController();
	}

	public PlaceRushOrderController getRController(){
		return (PlaceRushOrderController) super.getBController();
	}

	public void notifyError(){
		// TODO: implement later on if we need
	}

}
