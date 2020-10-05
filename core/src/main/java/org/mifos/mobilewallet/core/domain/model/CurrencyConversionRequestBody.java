package org.mifos.mobilewallet.core.domain.model;

public class CurrencyConversionRequestBody {

    private String lockKey;
    private String amount;
    private String from;
    private String to;
    private boolean failWhenExpired;

    public CurrencyConversionRequestBody(String lockKey, String amount, String from, String to, boolean failWhenExpired) {
        this.lockKey = lockKey;
        this.amount = amount;
        this.from = from;
        this.to = to;
        this.failWhenExpired = failWhenExpired;
    }
}
