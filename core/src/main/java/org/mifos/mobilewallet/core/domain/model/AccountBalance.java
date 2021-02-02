package org.mifos.mobilewallet.core.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AccountBalance implements Parcelable {

    public static final Creator<AccountBalance> CREATOR = new Creator<AccountBalance>() {
        @Override
        public AccountBalance createFromParcel(Parcel source) {
            return new AccountBalance(source);
        }

        @Override
        public AccountBalance[] newArray(int size) {
            return new AccountBalance[size];
        }
    };

    private String accountStatus;
    private String reservedBalance;
    private String currentBalance;
    private String currency;
    private String availableBalance;
    private String unclearedBalance;

    public AccountBalance() {
    }

    protected AccountBalance(Parcel in) {
        this.accountStatus = in.readString();
        this.reservedBalance = in.readString();
        this.currentBalance = in.readString();
        this.currency = in.readString();
        this.availableBalance = in.readString();
        this.unclearedBalance = in.readString();
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public String getReservedBalance() {
        return reservedBalance;
    }

    public String getCurrentBalance() {
        return currentBalance;
    }

    public String getCurrency() {
        return currency;
    }

    public String getAvailableBalance() {
        return availableBalance;
    }

    public String getUnclearedBalance() {
        return unclearedBalance;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }

    public void setReservedBalance(String reservedBalance) {
        this.reservedBalance = reservedBalance;
    }

    public void setCurrentBalance(String currentBalance) {
        this.currentBalance = currentBalance;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public void setAvailableBalance(String availableBalance) {
        this.availableBalance = availableBalance;
    }

    public void setUnclearedBalance(String unclearedBalance) {
        this.unclearedBalance = unclearedBalance;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.accountStatus);
        dest.writeString(this.reservedBalance);
        dest.writeString(this.currentBalance);
        dest.writeString(this.currency);
        dest.writeString(this.availableBalance);
        dest.writeString(this.unclearedBalance);
    }
}
