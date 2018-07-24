package com.example.android.vehiclerequest;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CustomAdapter extends FirebaseRecyclerAdapter<Vehicle, CustomHolder> {

    private Context context;
    private TextView startdate, enddate;
    private DatabaseReference databaseReference, db;
    private String user_id;

    private String ss;
    private String vno;

    public int mSelectedPosition = -1;
    public RadioButton mSelectedRB;
    public CustomAdapter(@NonNull FirebaseRecyclerOptions<Vehicle> options, Context context,TextView startdate, TextView enddate, DatabaseReference databaseReference, String user_id, DatabaseReference db) {
        super(options);
        this.context = context;
        this.startdate = startdate;
        this.enddate = enddate;
        this.databaseReference = databaseReference;
        this.user_id = user_id;
        this.db = db;
    }

    @Override
    protected void onBindViewHolder(@NonNull final CustomHolder holder, final int position, @NonNull final Vehicle model) {
        holder.setVehicleNo(model.getVehicleNo());
        holder.setSeats(model.getSeats() + " Seats");
        holder.setImage(context, model.getImage());
        holder.setAC(model.getAC());
        holder.setBooked(startdate, enddate, model.getVehicleNo(), databaseReference);

        holder.mView.findViewById(R.id.check_vehicle).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if((position != mSelectedPosition && mSelectedRB != null)){
                    mSelectedRB.setChecked(false);
                }
                mSelectedPosition = position;
                ss = model.getVehicleNo()+"("+model.getVehicle()+")";
                vno = model.getVehicleNo();
                mSelectedRB = (RadioButton)view;
                /*db.child("UserIdentities").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if (dataSnapshot.hasChild(user_id)) {
                            String indexNo = (String) dataSnapshot.child(user_id).getValue();

                            if (holder.isSelected()) {
                                DatabaseReference ref = db.child("Users").child(indexNo);
                                ref.child("checkedVehicle").setValue(model.getVehicleNo());
                            } else {
                                DatabaseReference ref = db.child("Users").child(indexNo);
                                ref.child("checkedVehicle").removeValue();
                            }
                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/

            }
        });

        if(mSelectedPosition != position){
            RadioButton rb = (RadioButton) holder.mView.findViewById(R.id.check_vehicle);
            rb.setChecked(false);
        }else{
            RadioButton rb = (RadioButton) holder.mView.findViewById(R.id.check_vehicle);
            rb.setChecked(true);
            ss = model.getVehicleNo()+"("+model.getVehicle()+")";
            vno = model.getVehicleNo();
            if(mSelectedRB != null && rb!= mSelectedRB){
                mSelectedRB = rb;
            }
        }

    }

    @NonNull
    @Override
    public CustomHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.vehicle_row, parent, false);

        return new CustomHolder(view);
    }

    public int getSelectedRadioButtonPosition(){
        return mSelectedPosition;
    }

    public RadioButton getSelectedRadioButton(){
        return mSelectedRB;
    }

    public String getSelectedVehicle(){
        return ss;
    }

    public String getSelectedVehicleNo(){
        return vno;
    }
}
