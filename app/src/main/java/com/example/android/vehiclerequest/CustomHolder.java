package com.example.android.vehiclerequest;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

public class CustomHolder extends RecyclerView.ViewHolder {

    View mView;

    public CustomHolder(View itemView) {
        super(itemView);
        mView = itemView;
    }

    public boolean isSelected() {
        RadioButton radioButton = (RadioButton) mView.findViewById(R.id.check_vehicle);
        return radioButton.isChecked();
    }

    public void setBooked(TextView startdate, TextView enddate, String vehicleNo, DatabaseReference databaseReference){
        final RadioButton checkBox = (RadioButton) mView.findViewById(R.id.check_vehicle);
        final TextView tv_booked = (TextView) mView.findViewById(R.id.booked);
        final String sdate = startdate.getText().toString();
        String edate = enddate.getText().toString();
        String myStartYear = sdate.substring(6);
        String myStartMonth = sdate.substring(3, 5);
        String myStartDay = sdate.substring(0, 2);
        String myEndYear = edate.substring(6);
        String myEndMonth = edate.substring(3, 5);
        String myEndDay = edate.substring(0, 2);
        final SimpleDateFormat sdf = new SimpleDateFormat("ddMMyyyy", Locale.US);

        try {
            final long e1 = sdf.parse(myEndDay+myEndMonth+myEndYear).getTime();
            final long s1 = sdf.parse(myStartDay+myStartMonth+myStartYear).getTime();

            if (!sdate.equals("Select Date") && !edate.equals("Select Date")) {
                databaseReference.child(vehicleNo).child("bookdates").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                            String d1 = snapshot.getKey();
                            String d2 = (String) snapshot.getValue();
                            try {
                                long s2 = sdf.parse(d1).getTime();
                                long e2 = sdf.parse(d2).getTime();
                                if ((e2 >= s1 && e1 >= s2) || (e1 >= s2 && e2 >= s1)) {
                                    checkBox.setVisibility(View.INVISIBLE);
                                    tv_booked.setTextColor(Color.RED);
                                    tv_booked.setText("Booked");
                                    break;
                                }
                            } catch (ParseException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    public void setAC(String ac) {
        TextView tv_ac = (TextView) mView.findViewById(R.id.ac);
        tv_ac.setText(ac);
    }


    public void setVehicleNo(String vehicleNo) {
        TextView vehicle_no = (TextView) mView.findViewById(R.id.vehicle_number);
        vehicle_no.setText(vehicleNo);
    }

    public void setSeats(String seats) {
        TextView tvseats = (TextView) mView.findViewById(R.id.no_of_seats);
        tvseats.setText(seats);
    }

    public void setImage(Context context, String image) {
        final SimpleDraweeView mSimpleDraweeView = (SimpleDraweeView) mView.findViewById(R.id.vehicle_image);
        if (image != null) {
            mSimpleDraweeView.setController(
                    Fresco.newDraweeControllerBuilder()
                            .setTapToRetryEnabled(true)
                            .setUri(Uri.parse(image))
                            .build());
        }
    }
}
