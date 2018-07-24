package com.example.android.vehiclerequest;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class Vehicle {

    private String mVehicleNo, mSeats, mAC, mVehicle;
    private String mvehicleImage;

    public Vehicle() {

    }

    public Vehicle(String VehicleNo, String Seats, String image, String ac, String vehicle) {
        this.mVehicleNo = VehicleNo;
        this.mSeats = Seats;
        this.mvehicleImage = image;
        this.mAC = ac;
        this.mVehicle = vehicle;
    }

    public String getVehicle() {
        return mVehicle;
    }

    public void setVehicle(String vehicle) {
        this.mVehicle = vehicle;
    }

    public String getAC() {
        return mAC;
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

    public void setAC(String ac) {
        this.mAC = ac;
    }

}
