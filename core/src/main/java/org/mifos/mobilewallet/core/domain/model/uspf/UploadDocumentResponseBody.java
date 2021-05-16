package org.mifos.mobilewallet.core.domain.model.uspf;

import android.os.Parcel;
import android.os.Parcelable;

public class UploadDocumentResponseBody implements Parcelable {

    public static final Creator<UploadDocumentResponseBody> CREATOR = new Creator<UploadDocumentResponseBody>() {
        @Override
        public UploadDocumentResponseBody createFromParcel(Parcel in) {
            return new UploadDocumentResponseBody(in);
        }

        @Override
        public UploadDocumentResponseBody[] newArray(int size) {
            return new UploadDocumentResponseBody[size];
        }
    };

    private int resourceId;
    private int resourceIdentifier;

    protected UploadDocumentResponseBody(Parcel in) {
        this.resourceId = in.readInt();
        this.resourceIdentifier = in.readInt();
    }

    public int getResourceId() {
        return resourceId;
    }

    public void setResourceId(int resourceId) {
        this.resourceId = resourceId;
    }

    public int getResourceIdentifier() {
        return resourceIdentifier;
    }

    public void setResourceIdentifier(int resourceIdentifier) {
        this.resourceIdentifier = resourceIdentifier;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.resourceId);
        dest.writeInt(this.resourceIdentifier);
    }
}
