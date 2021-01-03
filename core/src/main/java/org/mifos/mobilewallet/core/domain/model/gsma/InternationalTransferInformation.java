package org.mifos.mobilewallet.core.domain.model.gsma;

class InternationalTransferInformation {

    private String originCountry;
    private String receivingCountry;
    private String receivingCurrency;
    private String currencyPair;
    private Double currencyPairRate;

    public InternationalTransferInformation(String originCountry, String receivingCountry, String receivingCurrency, String currencyPair, Double currencyPairRate) {
        this.originCountry = originCountry;
        this.receivingCountry = receivingCountry;
        this.receivingCurrency = receivingCurrency;
        this.currencyPair = currencyPair;
        this.currencyPairRate = currencyPairRate;
    }
}
