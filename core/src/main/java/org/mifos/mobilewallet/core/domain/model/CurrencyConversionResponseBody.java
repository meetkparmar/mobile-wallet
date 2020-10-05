package org.mifos.mobilewallet.core.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class CurrencyConversionResponseBody implements Parcelable {

    public static final Creator<CurrencyConversionResponseBody> CREATOR = new
            Creator<CurrencyConversionResponseBody>() {
                @Override
                public CurrencyConversionResponseBody createFromParcel(Parcel source) {
                    return new CurrencyConversionResponseBody(source);
                }

                @Override
                public CurrencyConversionResponseBody[] newArray(int size) {
                    return new CurrencyConversionResponseBody[size];
                }
            };

    String lockKey;
    String from;
    String to;
    boolean failWhenExpired;
    Double amount;
    Double rate;
    Double convertedAmount;
    Long expireBy;

    public CurrencyConversionResponseBody(Parcel in) {
        this.lockKey = in.readString();
        ;
        this.amount = in.readDouble();
        this.from = in.readString();
        ;
        this.to = in.readString();
        ;
        this.failWhenExpired = in.readByte() != 0;
        this.rate = in.readDouble();
        this.convertedAmount = in.readDouble();
        this.expireBy = in.readLong();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void setLockKey(String lockKey) {
        this.lockKey = lockKey;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setFailWhenExpired(boolean failWhenExpired) {
        this.failWhenExpired = failWhenExpired;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public void setRate(Double rate) {
        this.rate = rate;
    }

    public void setConvertedAmount(Double convertedAmount) {
        this.convertedAmount = convertedAmount;
    }

    public void setExpireBy(Long expireBy) {
        this.expireBy = expireBy;
    }

    public String getLockKey() {
        return lockKey;
    }

    public String getFrom() {
        return from;
    }

    public String getTo() {
        return to;
    }

    public boolean isFailWhenExpired() {
        return failWhenExpired;
    }

    public Double getAmount() {
        return amount;
    }

    public Double getRate() {
        return rate;
    }

    public Double getConvertedAmount() {
        return convertedAmount;
    }

    public Long getExpireBy() {
        return expireBy;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.lockKey);
        dest.writeString(this.from);
        dest.writeString(this.to);
        dest.writeByte((byte) (failWhenExpired ? 1 : 0));
        dest.writeDouble(this.amount);
        dest.writeDouble(this.rate);
        dest.writeDouble(this.convertedAmount);
        dest.writeLong(this.expireBy);
    }
}
