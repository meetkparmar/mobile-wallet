package org.mifos.mobilewallet.core.domain.model.uspf;

import android.os.Parcel;
import android.os.Parcelable;


import java.util.ArrayList;
import java.util.List;

public class IdentifierTemplateResponseBody implements Parcelable {

    public static final Parcelable.Creator<IdentifierTemplateResponseBody> CREATOR = new Parcelable.Creator<IdentifierTemplateResponseBody>() {
        @Override
        public IdentifierTemplateResponseBody createFromParcel(Parcel in) {
            return new IdentifierTemplateResponseBody(in);
        }

        @Override
        public IdentifierTemplateResponseBody[] newArray(int size) {
            return new IdentifierTemplateResponseBody[size];
        }
    };

    private List<DocumentTypes> allowedDocumentTypes = new ArrayList<>();

    public IdentifierTemplateResponseBody(Parcel in) {
        in.readList(allowedDocumentTypes, DocumentTypes.class.getClassLoader());
    }

    public List<DocumentTypes> getAllowedDocumentTypes() {
        return allowedDocumentTypes;
    }

    public void setAllowedDocumentTypes(List<DocumentTypes> allowedDocumentTypes) {
        this.allowedDocumentTypes = allowedDocumentTypes;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(this.allowedDocumentTypes);
    }
}
