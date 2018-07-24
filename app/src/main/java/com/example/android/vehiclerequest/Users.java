package com.example.android.vehiclerequest;

public class Users extends UserId{

    String fromuserid, message, vehicles, date, passengers, stime, etime;

    public Users() {
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {

        return date;
    }

    public Users(String fromuserid, String message, String date, String passengers, String stime, String etime) {
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
}
