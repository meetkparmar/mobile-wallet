package org.mifos.mobilewallet.mifospay.createuser.model;

import android.os.Parcel;
import android.os.Parcelable;

public class LoginDetails implements Parcelable {

    public static final Creator<LoginDetails> CREATOR = new Creator<LoginDetails>() {
        @Override
        public LoginDetails createFromParcel(Parcel in) {
            return new LoginDetails(in);
        }

        @Override
        public LoginDetails[] newArray(int size) {
            return new LoginDetails[size];
        }
    };

    private String firstName;
    private String lastName;
    private String mobileNumber;
    private String email;
    private String dateOfBirth;

    protected LoginDetails(Parcel in) {
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.mobileNumber = in.readString();
        this.email = in.readString();
        this.dateOfBirth = in.readString();
    }

    public LoginDetails() {
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.firstName);
        dest.writeString(this.lastName);
        dest.writeString(this.mobileNumber);
        dest.writeString(this.email);
        dest.writeString(this.dateOfBirth);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
