package org.mifos.mobilewallet.core.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AccountName implements Parcelable {

    public static final Parcelable.Creator<AccountName> CREATOR = new Parcelable.Creator<AccountName>() {
        @Override
        public AccountName createFromParcel(Parcel source) {
            return new AccountName(source);
        }

        @Override
        public AccountName[] newArray(int size) {
            return new AccountName[size];
        }
    };
    private String fullName;
    private String nativeName;
    private String title;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.fullName);
        dest.writeString(this.nativeName);
        dest.writeString(this.title);
    }

    public void setFullName(String firstName) {
        this.fullName = firstName;
    }

    public void setNativeName(String nativeName) {
        this.nativeName = nativeName;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getFullName() {
        return fullName;
    }

    public String getNativeName() {
        return nativeName;
    }

    public String getTitle() {
        return title;
    }

    public AccountName(Parcel in) {
        this.fullName = in.readString();
        this.nativeName = in.readString();
        this.title = in.readString();
    }
}
