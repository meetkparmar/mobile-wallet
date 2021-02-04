package org.mifos.mobilewallet.core.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class DepositResponseBody implements Parcelable {

    public static final Creator<DepositResponseBody> CREATOR = new
            Creator<DepositResponseBody>() {
                @Override
                public DepositResponseBody createFromParcel(Parcel source) {
                    return new DepositResponseBody(source);
                }

                @Override
                public DepositResponseBody[] newArray(int size) {
                    return new DepositResponseBody[size];
                }
            };

    private String serverCorrelationId;
    private String status;

    public DepositResponseBody(Parcel in) {
        this.serverCorrelationId = in.readString();
        this.status = in.readString();
    }

    public String getServerCorrelationId() {
        return serverCorrelationId;
    }

    public void setServerCorrelationId(String serverCorrelationId) {
        this.serverCorrelationId = serverCorrelationId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.serverCorrelationId);
        dest.writeString(this.status);
    }
}
