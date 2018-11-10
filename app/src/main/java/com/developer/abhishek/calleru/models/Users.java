package com.developer.abhishek.calleru.models;

public class Users {

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
}
