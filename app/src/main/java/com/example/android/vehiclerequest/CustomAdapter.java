package com.example.android.vehiclerequest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DatabaseReference;

public class CustomAdapter extends FirebaseRecyclerAdapter<Vehicle, CustomHolder> {

    private Context context;
    private TextView startdate, enddate;
    private DatabaseReference databaseReference;

    public CustomAdapter(@NonNull FirebaseRecyclerOptions<Vehicle> options, Context context,TextView startdate, TextView enddate, DatabaseReference databaseReference) {
        super(options);
        this.context = context;
        this.startdate = startdate;
        this.enddate = enddate;
        this.databaseReference = databaseReference;
    }

    @Override
    protected void onBindViewHolder(@NonNull CustomHolder holder, int position, @NonNull Vehicle model) {
        holder.setVehicleNo(model.getVehicleNo());
        holder.setSeats(model.getSeats() + " Seats");
        holder.setImage(context, model.getImage());
        holder.setAC(model.getAC());
        holder.setBooked(startdate, enddate, model.getVehicleNo(), databaseReference);

    }

    @NonNull
    @Override
    public CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vehicle_row, parent, false);

        return new CustomHolder(view);
    }
}
