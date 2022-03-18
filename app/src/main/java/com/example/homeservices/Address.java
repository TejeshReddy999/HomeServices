package com.example.homeservices;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {

    private String customerPhoneNumber;
    private String employerPhoneNumber;
    private String requestService;
    private String Acknowledgment;

    public Address(String customerPhoneNumber, String employerPhoneNumber, String requestService, String acknowledgment) {
        this.customerPhoneNumber = customerPhoneNumber;
        this.employerPhoneNumber = employerPhoneNumber;
        this.requestService = requestService;
        Acknowledgment = acknowledgment;
    }

    public Address() {
    }

    protected Address(Parcel in) {
        customerPhoneNumber = in.readString();
        employerPhoneNumber = in.readString();
        requestService = in.readString();
        Acknowledgment = in.readString();
    }

    public static final Creator<Address> CREATOR = new Creator<Address>() {
        @Override
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        @Override
        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    public String getCustomerPhoneNumber() {
        return customerPhoneNumber;
    }

    public void setCustomerPhoneNumber(String customerPhoneNumber) {
        this.customerPhoneNumber = customerPhoneNumber;
    }

    public String getEmployerPhoneNumber() {
        return employerPhoneNumber;
    }

    public void setEmployerPhoneNumber(String employerPhoneNumber) {
        this.employerPhoneNumber = employerPhoneNumber;
    }

    public String getRequestService() {
        return requestService;
    }

    public void setRequestService(String requestService) {
        this.requestService = requestService;
    }

    public String getAcknowledgment() {
        return Acknowledgment;
    }

    public void setAcknowledgment(String acknowledgment) {
        Acknowledgment = acknowledgment;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(customerPhoneNumber);
        dest.writeString(employerPhoneNumber);
        dest.writeString(requestService);
        dest.writeString(Acknowledgment);
    }
}
