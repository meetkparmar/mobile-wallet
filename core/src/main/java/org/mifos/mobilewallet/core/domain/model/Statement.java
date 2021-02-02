package org.mifos.mobilewallet.core.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Statement implements Parcelable{

    public static final Parcelable.Creator<Statement> CREATOR = new
            Parcelable.Creator<Statement>() {
                @Override
                public Statement createFromParcel(Parcel source) {
                    return new Statement(source);
                }

                @Override
                public Statement[] newArray(int size) {
                    return new Statement[size];
                }
            };

    long workflowInstanceKey;
    String transactionId;
    String startedAt;
    String completedAt;
    String status;
    String payeeDfspId;
    String payeePartyId;
    String payeePartyIdType;
    String payerDfspId;
    String payerPartyId;
    String payerPartyIdType;
    Long amount;
    String currency;
    String direction;

    protected Statement(Parcel in) {
        this.workflowInstanceKey = in.readLong();
        this.transactionId = in.readString();
        this.startedAt = in.readString();
        this.completedAt = in.readString();
        this.status = in.readString();
        this.payeeDfspId = in.readString();
        this.payeePartyId = in.readString();
        this.payeePartyIdType = in.readString();
        this.payerDfspId = in.readString();
        this.payerPartyId = in.readString();
        this.payerPartyIdType = in.readString();
        this.amount = in.readLong();
        this.currency = in.readString();
        this.direction = in.readString();
    }

    public Statement(){

    }


    public static Creator<Statement> getCREATOR() {
        return CREATOR;
    }

    public long getWorkflowInstanceKey() {
        return workflowInstanceKey;
    }

    public void setWorkflowInstanceKey(long workflowInstanceKey) {
        this.workflowInstanceKey = workflowInstanceKey;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public String getStartedAt() {
        return startedAt;
    }

    public void setStartedAt(String startedAt) {
        this.startedAt = startedAt;
    }

    public String getCompletedAt() {
        return completedAt;
    }

    public void setCompletedAt(String completedAt) {
        this.completedAt = completedAt;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPayeeDfspId() {
        return payeeDfspId;
    }

    public void setPayeeDfspId(String payeeDfspId) {
        this.payeeDfspId = payeeDfspId;
    }

    public String getPayeePartyId() {
        return payeePartyId;
    }

    public void setPayeePartyId(String payeePartyId) {
        this.payeePartyId = payeePartyId;
    }

    public String getPayeePartyIdType() {
        return payeePartyIdType;
    }

    public void setPayeePartyIdType(String payeePartyIdType) {
        this.payeePartyIdType = payeePartyIdType;
    }

    public String getPayerDfspId() {
        return payerDfspId;
    }

    public void setPayerDfspId(String payerDfspId) {
        this.payerDfspId = payerDfspId;
    }

    public String getPayerPartyId() {
        return payerPartyId;
    }

    public void setPayerPartyId(String payerPartyId) {
        this.payerPartyId = payerPartyId;
    }

    public String getPayerPartyIdType() {
        return payerPartyIdType;
    }

    public void setPayerPartyIdType(String payerPartyIdType) {
        this.payerPartyIdType = payerPartyIdType;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(this.workflowInstanceKey);
        dest.writeString(this.transactionId);
        dest.writeString(this.startedAt);
        dest.writeString(this.completedAt);
        dest.writeString(this.status);
        dest.writeString(this.payeeDfspId);
        dest.writeString(this.payeePartyId);
        dest.writeString(this.payeePartyIdType);
        dest.writeString(this.payerDfspId);
        dest.writeString(this.payerPartyId);
        dest.writeString(this.payerPartyIdType);
        dest.writeLong(this.amount);
        dest.writeString(this.currency);
        dest.writeString(this.direction);
    }
}
