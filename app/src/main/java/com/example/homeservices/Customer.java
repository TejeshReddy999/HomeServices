package com.example.homeservices;

import android.os.Parcel;
import android.os.Parcelable;

public class Customer implements Parcelable {

    private String name;
    private String surName;
    private String email;
    private String phoneNumber;
    private String newPassword;
    private String confirmPassword;
    private String town;
    private String district;
    private String state;
    private String pinCode;
    private String userType;

    public Customer(String name, String surName, String email, String phoneNumber, String newPassword, String town, String distric, String state, String pinCode, String userType) {
        this.name = name;
        this.surName = surName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.newPassword = newPassword;
        this.town = town;
        this.district = distric;
        this.state = state;
        this.pinCode = pinCode;
        this.userType = userType;
    }

    public Customer() {
    }


    protected Customer(Parcel in) {
        name = in.readString();
        surName = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        newPassword = in.readString();
        confirmPassword = in.readString();
        town = in.readString();
        district = in.readString();
        state = in.readString();
        pinCode = in.readString();
        userType = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(surName);
        dest.writeString(email);
        dest.writeString(phoneNumber);
        dest.writeString(newPassword);
        dest.writeString(confirmPassword);
        dest.writeString(town);
        dest.writeString(district);
        dest.writeString(state);
        dest.writeString(pinCode);
        dest.writeString(userType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Customer> CREATOR = new Creator<Customer>() {
        @Override
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        @Override
        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurName() {
        return surName;
    }

    public void setSurName(String surName) {
        this.surName = surName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
