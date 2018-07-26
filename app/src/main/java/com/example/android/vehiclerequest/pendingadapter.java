package com.example.android.vehiclerequest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

public class pendingadapter extends RecyclerView.Adapter<pendingadapter.ViewHolder> {

    private Context mContext;
    private List<NotificationModel> mPendList;

    public pendingadapter(Context context, List<NotificationModel> pendList) {
        this.mContext = context;
        this.mPendList = pendList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pending_row, parent, false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull pendingadapter.ViewHolder holder, int position) {

        holder.mNotifVehicle.setText("Vehicle: " + mPendList.get(position).getVehicle());
        holder.mNotifStartDate.setText("From: " + mPendList.get(position).getSdate() + " " + mPendList.get(position).getStime());
        holder.mNotifEndDate.setText("To: " + mPendList.get(position).getEdate() + " " + mPendList.get(position).getEtime());
        holder.mNotifPassenger.setText("No of Passengers: " + mPendList.get(position).getPassengers());
        holder.mNotifReason.setText("Reason: " + mPendList.get(position).getMessage());

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private View mView;
        private TextView mNotifReason, mNotifStartDate, mNotifEndDate, mNotifPassenger, mNotifVehicle;

        public ViewHolder(View itemView) {
            super(itemView);

            mView = itemView;
            mNotifStartDate = mView.findViewById(R.id.booked_start_dates);
            mNotifEndDate = mView.findViewById(R.id.booked_end_dates);
            mNotifReason = mView.findViewById(R.id.booked_reason);
            mNotifVehicle = mView.findViewById(R.id.booked_vehicle_no);
            mNotifPassenger = mView.findViewById(R.id.booked_passengers);

        }
    }
}
