package views.screen.payment;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import controller.PaymentController;
import entity.invoice.Invoice;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import utils.Configs;
import views.screen.BaseScreenHandler;

public class PaymentScreenHandler extends BaseScreenHandler {

	@FXML
	private Button btnConfirmPayment;

	@FXML
	private ImageView loadingImage;

	@FXML
	private RadioButton creditCard;

	@FXML
	private RadioButton domesticDebitCard;

	@FXML
	private Label dateLabel;

	@FXML
	private Label securityLabel;

	private Invoice invoice;

	private static final String DOMESTIC_CARD = "Domestic Debit Card";

	private static final String CREDIT_CARD = "Credit Card";

	private String paymentMethod = CREDIT_CARD;

	public PaymentScreenHandler(Stage stage, String screenPath, int amount, String contents) throws IOException {
		super(stage, screenPath);
	}

	public PaymentScreenHandler(Stage stage, String screenPath, Invoice invoice) throws IOException {
		super(stage, screenPath);
		this.invoice = invoice;
		
		btnConfirmPayment.setOnMouseClicked(e -> {
			try {
				confirmToPayOrder();
				((PaymentController) getBController()).emptyCart();
			} catch (Exception exp) {
				System.out.println(exp.getStackTrace());
			}
		});

		ToggleGroup group = new ToggleGroup();
		creditCard.setToggleGroup(group);
		creditCard.setSelected(true);
		domesticDebitCard.setToggleGroup(group);

		group.selectedToggleProperty().addListener(new ChangeListener<Toggle>() {
			@Override
			public void changed(ObservableValue<? extends Toggle> observable, Toggle oldValue, Toggle newValue) {
				if(group.getSelectedToggle() != null){
					RadioButton button = (RadioButton) group.getSelectedToggle();
					paymentMethod = button.getText();
					if(button.getText().equals(DOMESTIC_CARD)){
						dateLabel.setText("Valid From");
						securityLabel.setText("Issuing Bank");
					} else {
						dateLabel.setText("Expiration Date");
						securityLabel.setText("Card security code");
					}

				}
			}
		});
	}

	@FXML
	private Label pageTitle;

	@FXML
	private TextField cardNumber;

	@FXML
	private TextField holderName;

	@FXML
	private TextField expirationDate;

	@FXML
	private TextField securityCode;

	void confirmToPayOrder() throws IOException{
		String contents = "pay order";
		PaymentController ctrl = (PaymentController) getBController();
		Map<String, String> response = new HashMap<>();
		if(paymentMethod.equals(DOMESTIC_CARD)){
			 response = ctrl.payOrderByDomesticCard(invoice.getAmount(), contents, cardNumber.getText(), holderName.getText(),
					expirationDate.getText(), securityCode.getText());
		} else if(paymentMethod.equals(CREDIT_CARD)){
			 response = ctrl.payOrderByCreditCard(invoice.getAmount(), contents, cardNumber.getText(), holderName.getText(),
					expirationDate.getText(), securityCode.getText());
		}


		BaseScreenHandler resultScreen = new ResultScreenHandler(this.stage, Configs.RESULT_SCREEN_PATH, response.get("RESULT"), response.get("MESSAGE") );
		resultScreen.setPreviousScreen(this);
		resultScreen.setHomeScreenHandler(homeScreenHandler);
		resultScreen.setScreenTitle("Result Screen");
		resultScreen.show();
	}

}
