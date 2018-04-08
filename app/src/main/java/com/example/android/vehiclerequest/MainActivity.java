package com.example.android.vehiclerequest;

import android.app.DatePickerDialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.view.SimpleDraweeView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Calendar myCalendar1;
    private Calendar myCalendar2;
    DatePickerDialog.OnDateSetListener date1;
    DatePickerDialog.OnDateSetListener date2;
    private TextView startDate;
    private TextView endDate;

    private RecyclerView vehicle_list;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    FirebaseRecyclerAdapter<Vehicle, VehicleViewHolder> FBRA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Fresco.initialize(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(MainActivity.this, LoginActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginIntent);

                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);

        myCalendar1 = Calendar.getInstance();
        myCalendar2 = Calendar.getInstance();
        startDate = (TextView) findViewById(R.id.tv_start_date);
        endDate = (TextView) findViewById(R.id.tv_end_date);

        date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();
            }

        };

        date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                // TODO Auto-generated method stub
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEnd();
            }

        };

        vehicle_list = (RecyclerView) findViewById(R.id.vehicles_list);
        vehicle_list.setHasFixedSize(true);
        vehicle_list.setLayoutManager(new LinearLayoutManager(this));

        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference().child("Vehicles").child("Bus");

        Query postQuery = mDatabaseReference.orderByKey();
        FirebaseRecyclerOptions<Vehicle> options = new FirebaseRecyclerOptions.Builder<Vehicle>()
                .setQuery(postQuery, Vehicle.class)
                .build();
        FBRA = new FirebaseRecyclerAdapter<Vehicle, VehicleViewHolder>(
                options) {
            @NonNull
            @Override
            public VehicleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.vehicle_row, parent, false);

                return new VehicleViewHolder(view);
            }

            @Override
            protected void onBindViewHolder(@NonNull VehicleViewHolder holder, int position, @NonNull Vehicle model) {

                //final String post_key = getRef(position).getKey().toString();

                holder.setVehicleNo(model.getVehicleNo());
                holder.setSeats(model.getSeats());
                holder.setImage(getApplicationContext(), model.getImage());

                /*holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent singleSocialActivity = new Intent(MainActivity.this, SingleSocialActivity.class);
                        singleSocialActivity.putExtra("PostId", post_key);
                        startActivity(singleSocialActivity);

                    }
                });*/
            }
        };
        vehicle_list.setAdapter(FBRA);

    }


    public void logoutClicked(View view){
        mAuth.signOut();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        vehicle_list.setAdapter(FBRA);
        FBRA.startListening();

    }

    @Override
    protected void onStop() {
        super.onStop();
        FBRA.stopListening();
    }

    public void startDateClicked(View view) {
        new DatePickerDialog(MainActivity.this, date1, myCalendar1
                .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                myCalendar1.get(Calendar.DAY_OF_MONTH)).show();

    }

    public void endDateClicked(View view) {
        new DatePickerDialog(MainActivity.this, date2, myCalendar2
                .get(Calendar.YEAR), myCalendar2.get(Calendar.MONTH),
                myCalendar2.get(Calendar.DAY_OF_MONTH)).show();
    }

    private void updateLabelStart() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Toast.makeText(this, sdf.format(myCalendar1.getTime()), Toast.LENGTH_SHORT).show();
        startDate.setText(sdf.format(myCalendar1.getTime()));

    }

    private void updateLabelEnd(){
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        Toast.makeText(this, sdf.format(myCalendar2.getTime()), Toast.LENGTH_SHORT).show();
        endDate.setText(sdf.format(myCalendar2.getTime()));
    }

    public static class VehicleViewHolder extends RecyclerView.ViewHolder {

        View mView;

        public VehicleViewHolder(View itemView) {
            super(itemView);
            mView = itemView;
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
            }else{
                Toast.makeText(context, "null image", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void searchButtonClicked(View view) {
        vehicle_list.setAdapter(FBRA);
    }
}
