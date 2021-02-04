package org.mifos.mobilewallet.core.domain.model;

import org.mifos.mobilewallet.core.domain.model.gsma.CreditParty;

public class DepositRequestBody {

    private String amount;
    private String type;
    private String currency;
    private CreditParty creditParty;

    public DepositRequestBody(String amount, String type, String currency, CreditParty creditParty) {
        this.amount = amount;
        this.type = type;
        this.currency = currency;
        this.creditParty = creditParty;
    }
}
