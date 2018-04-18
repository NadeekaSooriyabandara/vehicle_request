package com.example.android.vehiclerequest;

public class NotificationModel {

    private String mStartDate, mEndDate, mVehicleNo, mImage, mSeats, mAC, mVehicleType, mRequest;

    public NotificationModel(String startDate, String endDate, String vehicleNo, String image, String seats, String ac, String vehicleType, String request) {
        this.mStartDate = startDate;
        this.mEndDate = endDate;
        this.mVehicleNo = vehicleNo;
        this.mImage = image;
        this.mSeats = seats;
        this.mAC = ac;
        this.mVehicleType = vehicleType;
        this.mRequest = request;
    }

    public String getStartDate() {
        return mStartDate;
    }

    public String getEndDate() {
        return mEndDate;
    }

    public String getVehicleNo() {
        return mVehicleNo;
    }

    public String getImage() {
        return mImage;
    }

    public String getSeats() {
        return mSeats;
    }

    public String getAC() {
        return mAC;
    }

    public String getVehicleType() {
        return mVehicleType;
    }

    public String getRequest() {
        return mRequest;
    }

    public void setStartDate(String startDate) {
        this.mStartDate = startDate;
    }

    public void setEndDate(String endDate) {
        this.mEndDate = endDate;
    }

    public void setVehicleNo(String vehicleNo) {
        this.mVehicleNo = vehicleNo;
    }

    public void setImage(String image) {
        this.mImage = image;
    }

    public void setSeats(String seats) {
        this.mSeats = seats;
    }

    public void setAC(String ac) {
        this.mAC = ac;
    }

    public void setVehicleType(String mVehicleType) {
        this.mVehicleType = mVehicleType;
    }

    public void setRequest(String mRequest) {
        this.mRequest = mRequest;
    }
}
