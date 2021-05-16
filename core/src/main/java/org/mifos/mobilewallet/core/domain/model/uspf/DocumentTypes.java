package org.mifos.mobilewallet.core.domain.model.uspf;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.File;

public class DocumentTypes implements Parcelable {

    public static final Parcelable.Creator<DocumentTypes> CREATOR = new Parcelable.Creator<DocumentTypes>() {
        @Override
        public DocumentTypes createFromParcel(Parcel in) {
            return new DocumentTypes(in);
        }

        @Override
        public DocumentTypes[] newArray(int size) {
            return new DocumentTypes[size];
        }
    };

    private int id;
    private String name;
    private int position;
    private Boolean active;
    private Boolean mandatory;
    private String documentNumber;
    private String documentImage;
    private String file;

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public DocumentTypes(Parcel in) {
        this.id = in.readInt();
        this.name = in.readString();
        this.position = in.readInt();
        this.active = in.readByte() != 0;
        this.mandatory = in.readByte() != 0;
        this.documentNumber = in.readString();
        this.documentImage = in.readString();
        this.file = in.readString();
    }

    public String getDocumentNumber() {
        return documentNumber;
    }

    public void setDocumentNumber(String documentNumber) {
        this.documentNumber = documentNumber;
    }

    public String getDocumentImage() {
        return documentImage;
    }

    public void setDocumentImage(String documentImage) {
        this.documentImage = documentImage;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Boolean getMandatory() {
        return mandatory;
    }

    public void setMandatory(Boolean mandatory) {
        this.mandatory = mandatory;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.name);
        dest.writeInt(this.position);
        dest.writeByte((byte) (active ? 1 : 0));
        dest.writeByte((byte) (mandatory ? 1 : 0));
        dest.writeString(this.documentNumber);
        dest.writeString(this.documentImage);
        dest.writeString(this.file);
    }
}
