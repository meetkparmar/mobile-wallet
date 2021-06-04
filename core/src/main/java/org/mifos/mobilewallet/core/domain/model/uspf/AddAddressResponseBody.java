package org.mifos.mobilewallet.core.domain.model.uspf;

import android.os.Parcel;
import android.os.Parcelable;

public class AddAddressResponseBody implements Parcelable {

    public static final Parcelable.Creator<AddAddressResponseBody> CREATOR = new Parcelable.Creator<AddAddressResponseBody>() {
        @Override
        public AddAddressResponseBody createFromParcel(Parcel in) {
            return new AddAddressResponseBody(in);
        }

        @Override
        public AddAddressResponseBody[] newArray(int size) {
            return new AddAddressResponseBody[size];
        }
    };

    private int resourceId;

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    protected AddAddressResponseBody(Parcel in) {
        this.resourceId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int i) {
        dest.writeInt(this.resourceId);
    }
}
