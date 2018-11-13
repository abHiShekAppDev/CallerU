package com.developer.abhishek.calleru.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Users implements Parcelable {

    private String Id;
    private String MobileNumber;
    private String Name;

    public Users() {
    }

    public Users(String id, String mobileNumber, String name) {
        Id = id;
        MobileNumber = mobileNumber;
        Name = name;
    }

    protected Users(Parcel in) {
        Id = in.readString();
        MobileNumber = in.readString();
        Name = in.readString();
    }

    public static final Creator<Users> CREATOR = new Creator<Users>() {
        @Override
        public Users createFromParcel(Parcel in) {
            return new Users(in);
        }

        @Override
        public Users[] newArray(int size) {
            return new Users[size];
        }
    };

    public void setId(String id) {
        Id = id;
    }

    public void setMobileNumber(String mobileNumber) {
        MobileNumber = mobileNumber;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getId() {
        return Id;
    }

    public String getMobileNumber() {
        return MobileNumber;
    }

    public String getName() {
        return Name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(Id);
        parcel.writeString(MobileNumber);
        parcel.writeString(Name);
    }
}
