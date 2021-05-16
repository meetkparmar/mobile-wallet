package org.mifos.mobilewallet.core.domain.model.uspf;

import android.os.Parcel;
import android.os.Parcelable;

public class CreateIdentifierResponseBody implements Parcelable {

    public static final Creator<CreateIdentifierResponseBody> CREATOR = new Creator<CreateIdentifierResponseBody>() {
        @Override
        public CreateIdentifierResponseBody createFromParcel(Parcel in) {
            return new CreateIdentifierResponseBody(in);
        }

        @Override
        public CreateIdentifierResponseBody[] newArray(int size) {
            return new CreateIdentifierResponseBody[size];
        }
    };

    private int officeId;
    private int clientId;
    private int resourceId;

    protected CreateIdentifierResponseBody(Parcel in) {
        this.officeId = in.readInt();
        this.clientId = in.readInt();
        this.resourceId = in.readInt();
    }

    public int getOfficeId() {
        return officeId;
    }

    public void setOfficeId(int officeId) {
        this.officeId = officeId;
    }

    public int getClientId() {
        return clientId;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.officeId);
        dest.writeInt(this.clientId);
        dest.writeInt(this.resourceId);
    }
}
