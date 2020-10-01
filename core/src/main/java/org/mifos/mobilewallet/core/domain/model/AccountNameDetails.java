package org.mifos.mobilewallet.core.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class AccountNameDetails implements Parcelable {

    public static final Creator<AccountNameDetails> CREATOR = new
            Creator<AccountNameDetails>() {
                @Override
                public AccountNameDetails createFromParcel(Parcel source) {
                    return new AccountNameDetails(source);
                }

                @Override
                public AccountNameDetails[] newArray(int size) {
                    return new AccountNameDetails[size];
                }
            };
    String lei;
    String image;
    AccountName name;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.lei);
        dest.writeString(this.image);
        dest.writeParcelable(this.name, flags);
    }

    public void setLei(String lei) {
        this.lei = lei;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setName(AccountName name) {
        this.name = name;
    }

    public String getLei() {
        return lei;
    }

    public String getImage() {
        return image;
    }

    public AccountName getName() {
        return name;
    }

    public AccountNameDetails(Parcel in) {
        this.lei = in.readString();
        this.image = in.readString();
        this.name = in.readParcelable(AccountName.class.getClassLoader());

    }
}
