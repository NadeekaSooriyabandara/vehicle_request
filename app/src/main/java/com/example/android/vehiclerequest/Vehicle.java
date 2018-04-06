package com.example.android.vehiclerequest;

import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class Vehicle {

    private TextView mVehicleNo, mSeats;
    private ImageView mvehicleImage;
    private boolean mIsChecked;

    public Vehicle() {

    }

    public Vehicle(TextView VehicleNo, TextView Seats, ImageView vehicleImage, boolean IsChecked) {
        this.mVehicleNo = VehicleNo;
        this.mSeats = Seats;
        this.mvehicleImage = vehicleImage;
        this.mIsChecked = IsChecked;
    }

    public TextView getmVehicleNo() {
        return mVehicleNo;
    }

    public TextView getmSeats() {
        return mSeats;
    }

    public ImageView getvehicleImage() {
        return mvehicleImage;
    }

    public boolean IsChecked() {
        return mIsChecked;
    }

    public void setmVehicleNo(TextView VehicleNo) {
        this.mVehicleNo = VehicleNo;
    }

    public void setmSeats(TextView Seats) {
        this.mSeats = Seats;
    }

    public void setMvehicleImage(ImageView vehicleImage) {
        this.mvehicleImage = vehicleImage;
    }

    public void setmIsChecked(boolean IsChecked) {
        this.mIsChecked = IsChecked;
    }
}
