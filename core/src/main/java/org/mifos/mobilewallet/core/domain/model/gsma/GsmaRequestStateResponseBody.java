package org.mifos.mobilewallet.core.domain.model.gsma;

import android.os.Parcel;
import android.os.Parcelable;

public class GsmaRequestStateResponseBody implements Parcelable {

    public static final Creator<GsmaRequestStateResponseBody> CREATOR = new Creator<GsmaRequestStateResponseBody>() {
        @Override
        public GsmaRequestStateResponseBody createFromParcel(Parcel in) {
            return new GsmaRequestStateResponseBody(in);
        }

        @Override
        public GsmaRequestStateResponseBody[] newArray(int size) {
            return new GsmaRequestStateResponseBody[size];
        }
    };

    String notificationMethod;
    String serverCorrelationId;
    String status;
    String pendingReason;
    String objectReference;
    String expiryTime;
    String pollLimit;

    protected GsmaRequestStateResponseBody(Parcel in) {
        this.notificationMethod = in.readString();
        this.serverCorrelationId = in.readString();
        this.status = in.readString();
        this.pendingReason = in.readString();
        this.objectReference = in.readString();
        this.expiryTime = in.readString();
        this.pollLimit = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
    }

    public String getNotificationMethod() {
        return notificationMethod;
    }

    public void setNotificationMethod(String notificationMethod) {
        this.notificationMethod = notificationMethod;
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

    public String getPendingReason() {
        return pendingReason;
    }

    public void setPendingReason(String pendingReason) {
        this.pendingReason = pendingReason;
    }

    public String getObjectReference() {
        return objectReference;
    }

    public void setObjectReference(String objectReference) {
        this.objectReference = objectReference;
    }

    public String getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(String expiryTime) {
        this.expiryTime = expiryTime;
    }

    public String getPollLimit() {
        return pollLimit;
    }

    public void setPollLimit(String pollLimit) {
        this.pollLimit = pollLimit;
    }
}
