package org.mifos.mobilewallet.core.domain.model.uspf;

public class CreateIdentifierRequestBody {

    private String documentTypeId;
    private String documentKey;
    private String status;
    private String description;

    public CreateIdentifierRequestBody(String documentTypeId, String documentKey, String status, String description) {
        this.documentTypeId = documentTypeId;
        this.documentKey = documentKey;
        this.status = status;
        this.description = description;
    }
}
