package org.mifos.mobilewallet.core.domain.model.gsma;

import java.io.Serializable;
import java.util.ArrayList;

public class IntTransferRequestBody implements Serializable {

    private String amount;
    private String type;
    private String requestingLei;
    private String receivingLei;
    private String currency;
    private ArrayList<CreditParty> creditParty;
    private ArrayList<DebitParty> debitParty;
    private InternationalTransferInformation internationalTransferInformation;

    public IntTransferRequestBody(String amount, String type, String requestingLei, String receivingLei, String currency, ArrayList<CreditParty> creditParty, ArrayList<DebitParty> debitParty, InternationalTransferInformation internationalTransferInformation) {
        this.amount = amount;
        this.type = type;
        this.requestingLei = requestingLei;
        this.currency = currency;
        this.receivingLei = receivingLei;
        this.creditParty = creditParty;
        this.debitParty = debitParty;
        this.internationalTransferInformation = internationalTransferInformation;
    }
}

