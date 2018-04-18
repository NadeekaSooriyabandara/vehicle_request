package com.example.android.vehiclerequest;

import android.content.Context;
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

    public void setVehicleNo(String vehicleNo) {
        TextView  vehicle_no= mView.findViewById(R.id.vehicle_no);
        vehicle_no.setText("Vehicle No: " + vehicleNo);
    }

    public void setVehicleType(String vehicle, String seats, String ac) {
        TextView  vehicle_type= mView.findViewById(R.id.vehicle_type);
        vehicle_type.setText(ac + " " + vehicle + " with " + seats + " seats");
    }

    public void setDates(String startdate, String enddate) {
        TextView  dates= mView.findViewById(R.id.booked_dates);
        dates.setText("From " + startdate + " To " + enddate);
    }

    public void setImage(Context context,String image) {
        final SimpleDraweeView mSimpleDraweeView = (SimpleDraweeView) mView.findViewById(R.id.image_bus);
        if (image != null) {
            mSimpleDraweeView.setController(
                    Fresco.newDraweeControllerBuilder()
                            .setTapToRetryEnabled(true)
                            .setUri(Uri.parse(image))
                            .build());
        }
    }

    public void setTitle(String request) {
        TextView title = mView.findViewById(R.id.notification_title);
        if (request.equals("confirmed")) {
            title.setText("Request Accepted!");
        } else if (request.equals("not confirmed")) {
            title.setText("Request Rejected!");
        }
    }

    public void setNotificationDate() {
        TextView date = mView.findViewById(R.id.notification_date);


    }


}
