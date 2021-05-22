package org.mifos.mobilewallet.core.domain.model.uspf;

import java.io.Serializable;

public class ClientAddress implements Serializable {

    private String addressTypeId;
    private boolean isActive = true;
    private String addressLine1;
    private String addressLine2;
    private String addressLine3 = "";
    private String postalCode;
    private String city;

    public ClientAddress(String addressTypeId, String addressLine1, String addressLine2, String addressLine3, String city, String postalCode) {
        this.addressTypeId = addressTypeId;
        this.addressLine1 = addressLine1;
        this.addressLine2 = addressLine2;
        this.addressLine3 = addressLine3;
        this.city = city;
        this.postalCode = postalCode;
    }
}
