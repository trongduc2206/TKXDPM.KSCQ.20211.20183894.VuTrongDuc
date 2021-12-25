package entity.payment;

public class DomesticDebitCard extends PaymentCard {
    private String cardCode;
    private String owner;
    private String validDate;
    private String issuingBank;
//Vu Trong Duc - 20183894
    public DomesticDebitCard(String cardCode, String owner, String validDate, String issuingBank){
        this.cardCode= cardCode;
        this.owner = owner;
        this.validDate = validDate;
        this.issuingBank = issuingBank;
    }
}
