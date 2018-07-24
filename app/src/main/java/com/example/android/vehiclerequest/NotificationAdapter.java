package com.example.android.vehiclerequest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NotificationAdapter extends FirebaseRecyclerAdapter<NotificationModel, NotificationHolder>{

    private Context context;
    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;

    public NotificationAdapter(@NonNull FirebaseRecyclerOptions<NotificationModel> options, Context context, DatabaseReference databaseReference, FirebaseAuth firebaseAuth) {
        super(options);
        this.context = context;
        this.mDatabaseReference = databaseReference;
        this.mAuth = firebaseAuth;
    }

    @Override
    protected void onBindViewHolder(@NonNull NotificationHolder holder, int position, @NonNull NotificationModel model) {

        /*holder.setTitle(model.getRequest());
        holder.setDates(model.getStartDate(), model.getEndDate());
        holder.setImage(context, model.getImage());
        holder.setVehicleNo(model.getVehicleNo());
        holder.setVehicleType(model.getVehicleType(), model.getSeats(), model.getAC());
        holder.setNotificationDate();*/



        final String key = getRef(position).getKey();
        /*holder.mView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO delete process here
                mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users")
                        .child(mAuth.getCurrentUser().getUid()).child("requests");
                mDatabaseReference.child(key).removeValue();
            }
        });*/
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //TODO remove from notification activity and display in history activity

            }
        });
    }

    @NonNull
    @Override
    public NotificationHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.notification_row, parent, false);

        return new NotificationHolder(view);
    }
}
