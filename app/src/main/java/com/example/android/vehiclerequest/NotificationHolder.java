package com.example.android.vehiclerequest;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;

public class NotificationHolder extends RecyclerView.ViewHolder {

    View mView;


    public NotificationHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public void setVehicle(String vehicle) {
        TextView  vehicle_no= mView.findViewById(R.id.noti_vehicle_no);
        vehicle_no.setText("Vehicle: " + vehicle);
    }

    public void setDates(String sdate, String edate, String stime, String etime) {
        TextView  start_dates= mView.findViewById(R.id.noti_start_dates);
        TextView  end_dates= mView.findViewById(R.id.noti_end_dates);
        start_dates.setText("From: " + sdate + " " + stime);
        end_dates.setText("To: " + edate + " " + etime);

    }

    public void setTitle(String request) {
        TextView title = mView.findViewById(R.id.notification_title);
        if (request.equals("confirmed")) {
            title.setText("Request Accepted!");
        } else if (request.equals("rejected")) {
            title.setText("Request Rejected!");
            title.setTextColor(Color.RED);
        }
    }

    public void setNotificationDate() {
        TextView date = mView.findViewById(R.id.notification_date);


    }

    public void setReason(String reason) {
        TextView reso = mView.findViewById(R.id.noti_reason);
        reso.setText("Reason: " + reason);
    }

    public void setPassengers(String passengers) {
        TextView reso = mView.findViewById(R.id.noti_paseng);
        reso.setText("No of Passengers: " + passengers);
    }

    public void setRejectedReason(String reason){
        TextView rejo = mView.findViewById(R.id.reject_reason);
    }


}
