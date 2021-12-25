package entity.payment;

import java.sql.Timestamp;

public class CreditCard extends PaymentCard{
	private String cardCode;
    private String owner;
	private int cvvCode;
	private String dateExpired;
	//Vu Trong Duc - 20183894
	public CreditCard(String cardCode, String owner, int cvvCode, String dateExpired) {
		this.cardCode = cardCode;
		this.owner = owner;
		this.cvvCode = cvvCode;
		this.dateExpired = dateExpired;
	}
}
