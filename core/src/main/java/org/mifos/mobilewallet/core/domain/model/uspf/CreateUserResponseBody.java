package org.mifos.mobilewallet.core.domain.model.uspf;

import android.os.Parcel;
import android.os.Parcelable;

public class CreateUserResponseBody implements Parcelable {

    public static final Creator<CreateUserResponseBody> CREATOR = new Creator<CreateUserResponseBody>() {
        @Override
        public CreateUserResponseBody createFromParcel(Parcel in) {
            return new CreateUserResponseBody(in);
        }

        @Override
        public CreateUserResponseBody[] newArray(int size) {
            return new CreateUserResponseBody[size];
        }
    };

    private int officeId;
    private int resourceId;

    public int getOfficeId() {
        return officeId;
    }

    public void setOfficeId(int officeId) {
        this.officeId = officeId;
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    protected CreateUserResponseBody(Parcel in) {
        this.officeId = in.readInt();
        this.resourceId = in.readInt();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.officeId);
        dest.writeInt(this.resourceId);
    }
}
