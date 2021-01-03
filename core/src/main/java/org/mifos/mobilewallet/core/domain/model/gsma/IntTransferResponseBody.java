package org.mifos.mobilewallet.core.domain.model.gsma;

import android.os.Parcel;
import android.os.Parcelable;

public class IntTransferResponseBody implements Parcelable {

    public static final Creator<IntTransferResponseBody> CREATOR = new Creator<IntTransferResponseBody>() {
        @Override
        public IntTransferResponseBody createFromParcel(Parcel in) {
            return new IntTransferResponseBody(in);
        }

        @Override
        public IntTransferResponseBody[] newArray(int size) {
            return new IntTransferResponseBody[size];
        }
    };

    String transactionId;

    protected IntTransferResponseBody(Parcel in) {
        this.transactionId = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.transactionId);
    }
}
