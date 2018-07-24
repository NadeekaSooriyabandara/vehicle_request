package com.example.android.vehiclerequest;

public class NotificationModel {

    String fromuserid, message, vehicles, date, passengers, stime, etime;

    public NotificationModel() {
    }

    public NotificationModel(String fromuserid, String message, String date, String passengers, String stime, String etime) {
        this.fromuserid = fromuserid;
        this.message = message;
        this.date  =date;
        this.passengers = passengers;
        this.stime = stime;
        this.etime = etime;
    }

    public void setPassengers(String passengers) {
        this.passengers = passengers;
    }

    public void setStime(String stime) {
        this.stime = stime;
    }

    public void setEtime(String etime) {
        this.etime = etime;
    }

    public String getPassengers() {
        return passengers;
    }

    public String getStime() {
        return stime;
    }

    public String getEtime() {
        return etime;
    }

    public String getName() {
        return fromuserid;
    }

    public String getImage() {
        return message;
    }

    public void setName(String fromuserid) {
        this.fromuserid = fromuserid;
    }

    public void setImage(String message) {
        this.message = message;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {

        return date;
    }
}
