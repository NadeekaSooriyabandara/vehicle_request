package com.example.android.vehiclerequest;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class Vehicle {

    private String mVehicleNo, mSeats;
    private String mvehicleImage;

    public Vehicle() {

    }

    public Vehicle(String VehicleNo, String Seats, String image) {
        this.mVehicleNo = VehicleNo;
        this.mSeats = Seats;
        this.mvehicleImage = image;
    }

    public String getVehicleNo() {
        return mVehicleNo;
    }

    public String getSeats() {
        return mSeats;
    }

    public String getImage() {
        return mvehicleImage;
    }

    public void setVehicleNo(String VehicleNo) {
        this.mVehicleNo = VehicleNo;
    }

    public void setSeats(String Seats) {
        this.mSeats = Seats;
    }

    public void setImage(String image) {
        this.mvehicleImage = image;
    }

}
