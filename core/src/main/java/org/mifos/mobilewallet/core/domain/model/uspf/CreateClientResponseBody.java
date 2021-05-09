package org.mifos.mobilewallet.core.domain.model.uspf;

import android.os.Parcel;
import android.os.Parcelable;

public class CreateClientResponseBody implements Parcelable {

    public static final Creator<CreateClientResponseBody> CREATOR = new Creator<CreateClientResponseBody>() {
        @Override
        public CreateClientResponseBody createFromParcel(Parcel in) {
            return new CreateClientResponseBody(in);
        }

        @Override
        public CreateClientResponseBody[] newArray(int size) {
            return new CreateClientResponseBody[size];
        }
    };

    private int officeId;
    private int clientId;
    private int savingsId;
    private int resourceId;

    protected CreateClientResponseBody(Parcel in) {
        this.officeId = in.readInt();
        this.clientId = in.readInt();
        this.savingsId = in.readInt();
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

    public int getSavingsId() {
        return savingsId;
    }

    public void setSavingsId(int savingsId) {
        this.savingsId = savingsId;
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
        dest.writeInt(this.savingsId);
        dest.writeInt(this.resourceId);
    }
}
