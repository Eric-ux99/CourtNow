package com.example.courtnowproject;

import android.os.Parcel;
import android.os.Parcelable;

public class Complex implements Parcelable {

    private String name, address, phone, openHours, complexId;

    public Complex() {
    }

    protected Complex(Parcel in) {
        name = in.readString();
        address = in.readString();
        phone = in.readString();
        openHours = in.readString();
        complexId = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(phone);
        dest.writeString(openHours);
        dest.writeString(complexId);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Complex> CREATOR = new Creator<Complex>() {
        @Override
        public Complex createFromParcel(Parcel in) {
            return new Complex(in);
        }

        @Override
        public Complex[] newArray(int size) {
            return new Complex[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getOpenHours() {
        return openHours;
    }

    public void setOpenHours(String openHours) {
        this.openHours = openHours;
    }

    public String getComplexId() {
        return complexId;
    }

    public void setComplexId(String complexId) {
        this.complexId = complexId;
    }
}
