package org.mifos.mobilewallet.core.domain.model.uspf;

import java.util.ArrayList;
import java.util.List;

public class CreateClientRequestBody {

    private String firstname;
    private String lastname;
    private String mobileNo;
    private String dateOfBirth;
    private String submittedOnDate;
    private String activationDate;
    private String dateFormat = "dd MMMM yyyy";
    private String locale = "en";
    private Boolean active = true;
    private int savingsProductId;
    private int legalFormId;
    private int officeId = 1;
    private List<ClientAddress> address = new ArrayList<>();
    private List<String> familyMembers = new ArrayList<>();

    public CreateClientRequestBody(String firstname, String lastname, String mobileNo, String dateOfBirth, String submittedOnDate, String activationDate, String dateFormat, String locale, Boolean active, int savingsProductId, int legalFormId, int officeId, List<ClientAddress> address, List<String> familyMembers) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.mobileNo = mobileNo;
        this.dateOfBirth = dateOfBirth;
        this.submittedOnDate = submittedOnDate;
        this.activationDate = activationDate;
        this.dateFormat = dateFormat;
        this.locale = locale;
        this.active = active;
        this.savingsProductId = savingsProductId;
        this.legalFormId = legalFormId;
        this.officeId = officeId;
        this.address = address;
        this.familyMembers = familyMembers;
    }
}
