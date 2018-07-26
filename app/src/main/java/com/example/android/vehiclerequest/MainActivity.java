package com.example.android.vehiclerequest;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.infideap.drawerbehavior.AdvanceDrawerLayout;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private AdvanceDrawerLayout drawer;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    private Calendar myCalendar1;
    private Calendar myCalendar2;
    DatePickerDialog.OnDateSetListener date1;
    DatePickerDialog.OnDateSetListener date2;
    private TextView startDate, startTime;
    private TextView endDate, endTime;
    private CheckBox van, bus, ac, nonac;
    private ProgressBar progressBar;
    private String stime, etime;
    private EditText noPassengers;
    private String currentTimeString;

    private RecyclerView vehicle_list;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mDatabaseUsers;
    //FirebaseRecyclerAdapter<Vehicle, VehicleViewHolder> FBRA;
    //Query postQuery;
    CustomAdapter adapterBusac, adapterBusnonac, adapterBus, adapterVan, adapterBusacVan, adapterBusnonacVan, adapterBusVan;

    public static int count = 0;
    private String selectedVehicle, selectedVno;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(null);
        Toolbar topToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(topToolBar);

        Fresco.initialize(this);
        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(MainActivity.this, SigninSignupActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginIntent);

                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);

        drawer = (AdvanceDrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, topToolBar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        progressBar = (ProgressBar) findViewById(R.id.pb_main);
        myCalendar1 = Calendar.getInstance();
        myCalendar2 = Calendar.getInstance();
        startDate = (TextView) findViewById(R.id.tv_start_date);
        endDate = (TextView) findViewById(R.id.tv_end_date);
        startTime = (TextView) findViewById(R.id.tv_start_time);
        endTime = (TextView) findViewById(R.id.tv_end_time);
        van = (CheckBox) findViewById(R.id.checkBox_van);
        bus = (CheckBox) findViewById(R.id.checkBox_bus);
        ac = (CheckBox) findViewById(R.id.checkBox_ac);
        nonac = (CheckBox) findViewById(R.id.checkBox_nonac);
        noPassengers = (EditText) findViewById(R.id.no_passengers);


        date1 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar1.set(Calendar.YEAR, year);
                myCalendar1.set(Calendar.MONTH, monthOfYear);
                myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelStart();
            }

        };

        date2 = new DatePickerDialog.OnDateSetListener() {

            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                myCalendar2.set(Calendar.YEAR, year);
                myCalendar2.set(Calendar.MONTH, monthOfYear);
                myCalendar2.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                updateLabelEnd();
            }

        };

        vehicle_list = (RecyclerView) findViewById(R.id.vehicles_list);
        vehicle_list.setHasFixedSize(true);
        vehicle_list.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));

        FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        mDatabaseUsers = mDatabase.getReference().child("Users");
        mDatabaseReference = mDatabase.getReference().child("Vehicles");

        Query queryBusac = mDatabaseReference.orderByChild("vehicletype").equalTo("busac");
        Query queryBusnonac = mDatabaseReference.orderByChild("vehicletype").equalTo("busnonac");
        Query queryBus = mDatabaseReference.orderByChild("bus").equalTo("yes");
        Query queryVan = mDatabaseReference.orderByChild("vehicletype").equalTo("van");
        Query queryBusacVan = mDatabaseReference.orderByChild("busacorvan").equalTo("yes");
        Query queryBusnonacVan = mDatabaseReference.orderByChild("busnonacorvan").equalTo("yes");
        Query queryBusVan = mDatabaseReference.orderByKey();
        FirebaseRecyclerOptions<Vehicle> optionBusac = new FirebaseRecyclerOptions.Builder<Vehicle>()
                .setQuery(queryBusac, Vehicle.class)
                .build();
        FirebaseRecyclerOptions<Vehicle> optionBusnonac = new FirebaseRecyclerOptions.Builder<Vehicle>()
                .setQuery(queryBusnonac, Vehicle.class)
                .build();
        FirebaseRecyclerOptions<Vehicle> optionBus = new FirebaseRecyclerOptions.Builder<Vehicle>()
                .setQuery(queryBus, Vehicle.class)
                .build();
        FirebaseRecyclerOptions<Vehicle> optionVan = new FirebaseRecyclerOptions.Builder<Vehicle>()
                .setQuery(queryVan, Vehicle.class)
                .build();
        FirebaseRecyclerOptions<Vehicle> optionBusacVan = new FirebaseRecyclerOptions.Builder<Vehicle>()
                .setQuery(queryBusacVan, Vehicle.class)
                .build();
        FirebaseRecyclerOptions<Vehicle> optionBusnonacVan = new FirebaseRecyclerOptions.Builder<Vehicle>()
                .setQuery(queryBusnonacVan, Vehicle.class)
                .build();
        FirebaseRecyclerOptions<Vehicle> optionBusVan = new FirebaseRecyclerOptions.Builder<Vehicle>()
                .setQuery(queryBusVan, Vehicle.class)
                .build();
        if (mAuth.getCurrentUser() != null) {
            String user_id = mAuth.getCurrentUser().getUid();

            DatabaseReference db = FirebaseDatabase.getInstance().getReference();
            adapterBusac = new CustomAdapter(optionBusac, this, startDate, endDate, mDatabaseReference, user_id, db);
            adapterBusnonac = new CustomAdapter(optionBusnonac, this, startDate, endDate, mDatabaseReference, user_id, db);
            adapterBus = new CustomAdapter(optionBus, this, startDate, endDate, mDatabaseReference, user_id, db);
            adapterVan = new CustomAdapter(optionVan, this, startDate, endDate, mDatabaseReference, user_id, db);
            adapterBusacVan = new CustomAdapter(optionBusacVan, this, startDate, endDate, mDatabaseReference, user_id, db);
            adapterBusnonacVan = new CustomAdapter(optionBusnonacVan, this, startDate, endDate, mDatabaseReference, user_id, db);
            adapterBusVan = new CustomAdapter(optionBusVan, this, startDate, endDate, mDatabaseReference, user_id, db);
        } else {
            Intent loginIntent = new Intent(MainActivity.this, SigninSignupActivity.class);
            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(loginIntent);
        }

        updateLabelStart();
        updateLabelEnd();
        currentTimeString = new SimpleDateFormat("HH:mm").format(new Date());
        startTime.setText(currentTimeString);
        endTime.setText(currentTimeString);
        stime = startTime.getText().toString();
        etime = endTime.getText().toString();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        MenuItem menuItem = menu.findItem(R.id.notifications);
        menuItem.setIcon(buildCounterDrawable(count, R.drawable.ic_notification));

        return true;
    }

    private Drawable buildCounterDrawable(int count, int backgroundImageId) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.counter_menuitem_layout, null);
        view.setBackgroundResource(backgroundImageId);

        if (count == 0) {
            View counterTextPanel = view.findViewById(R.id.counterValuePanel);
            counterTextPanel.setVisibility(View.GONE);
        } else {
            TextView textView = (TextView) view.findViewById(R.id.count);
            textView.setText("" + count);
        }

        view.measure(
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());

        view.setDrawingCacheEnabled(true);
        view.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH);
        Bitmap bitmap = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);

        return new BitmapDrawable(getResources(), bitmap);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id.log_out: {
                final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);

                //dialogBuilder.setTitle("Request a Vehicle");
                dialogBuilder.setMessage("Are you sure want to logout?");
                dialogBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                        final String user_id = mAuth.getCurrentUser().getUid();
                        db.child("UserIdentities").addListenerForSingleValueEvent(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                if (dataSnapshot.hasChild(user_id)) {
                                    String indexNo = (String) dataSnapshot.child(user_id).getValue();
                                    db.child("Users").child(indexNo).child("token").setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            mAuth.signOut();
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });

                    }
                });
                dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //pass
                    }
                });
                final AlertDialog b = dialogBuilder.create();
                b.setOnShowListener( new DialogInterface.OnShowListener() {
                    @Override
                    public void onShow(DialogInterface arg0) {
                        b.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.rgb(0, 255, 0));
                    }
                });
                b.show();

                return true;
                }
            case R.id.notifications:{
                count++;
                invalidateOptionsMenu();
                Intent intent = new Intent(MainActivity.this, NotificationActivity.class);
                startActivity(intent);

                return true;
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
        if (mAuth.getCurrentUser() != null) {
            adapterBusac.startListening();
            adapterBusnonac.startListening();
            adapterBus.startListening();
            adapterVan.startListening();
            adapterBusacVan.startListening();
            adapterBusnonacVan.startListening();
            adapterBusVan.startListening();
        }

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mAuth.getCurrentUser() != null) {
            adapterBusac.stopListening();
            adapterBusnonac.stopListening();
            adapterBus.stopListening();
            adapterVan.stopListening();
            adapterBusacVan.stopListening();
            adapterBusnonacVan.stopListening();
            adapterBusVan.stopListening();
        }
        if (mAuth != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
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

    public void startTimeClicked(View view) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int startHour = c.get(Calendar.HOUR_OF_DAY);
        int startMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay,
                                          int minute) {

                        startTime.setText(hourOfDay + ":" + minute);
                        stime = hourOfDay + ":" + minute;
                    }
                }, startHour, startMinute, false);
        timePickerDialog.show();
    }

    public void endTimeClicked(View view) {
        // Get Current Time
        final Calendar c = Calendar.getInstance();
        int endHour = c.get(Calendar.HOUR_OF_DAY);
        int endMinute = c.get(Calendar.MINUTE);

        // Launch Time Picker Dialog
        TimePickerDialog timePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {

            @Override
            public void onTimeSet(TimePicker view, int hourOfDay,
                                  int minute) {

                endTime.setText(hourOfDay + ":" + minute);
                etime = hourOfDay + ":" + minute;
            }
        }, endHour, endMinute, false);
        timePickerDialog.show();
    }

    private void updateLabelStart() {
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        startDate.setText(sdf.format(myCalendar1.getTime()));

    }

    private void updateLabelEnd(){
        String myFormat = "dd/MM/yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        endDate.setText(sdf.format(myCalendar2.getTime()));
    }

    public void confirmButtonClicked(View view) {
        boolean isBus = !van.isChecked() && bus.isChecked() && ac.isChecked() && nonac.isChecked();
        boolean isBusac = !van.isChecked() && bus.isChecked() && ac.isChecked() && !nonac.isChecked();
        boolean isBusnonac = !van.isChecked() && bus.isChecked() && !ac.isChecked() && nonac.isChecked();
        boolean isVan = van.isChecked() && !bus.isChecked() && ac.isChecked();
        boolean isVanBus = van.isChecked() && bus.isChecked() && ac.isChecked() && nonac.isChecked();
        boolean isVanBusac = van.isChecked() && bus.isChecked() && ac.isChecked() && !nonac.isChecked();
        boolean isVanBusnonac = van.isChecked() && bus.isChecked() && !ac.isChecked() && nonac.isChecked();

        if (isBus) {
            if (adapterBus.getSelectedRadioButtonPosition() != -1) {
                selectedVehicle = adapterBus.getSelectedVehicle();
                selectedVno = adapterBus.getSelectedVehicleNo();
            }
        } else if (isBusac) {
            if (adapterBusac.getSelectedRadioButtonPosition() != -1) {
                selectedVehicle = adapterBusac.getSelectedVehicle();
                selectedVno = adapterBusac.getSelectedVehicleNo();
            }
        } else if (isBusnonac) {
            if (adapterBusnonac.getSelectedRadioButtonPosition() != -1) {
                selectedVehicle = adapterBusnonac.getSelectedVehicle();
                selectedVno = adapterBusnonac.getSelectedVehicleNo();
            }
        } else if (isVan) {
            if (adapterVan.getSelectedRadioButtonPosition() != -1) {
                selectedVehicle = adapterVan.getSelectedVehicle();
                selectedVno = adapterVan.getSelectedVehicleNo();
            }
        } else if (isVanBus) {
            if (adapterBusVan.getSelectedRadioButtonPosition() != -1) {
                selectedVehicle = adapterBusVan.getSelectedVehicle();
                selectedVno = adapterBusVan.getSelectedVehicleNo();
            }
        } else if (isVanBusac) {
            if (adapterBusacVan.getSelectedRadioButtonPosition() != -1) {
                selectedVehicle = adapterBusacVan.getSelectedVehicle();
                selectedVno = adapterBusacVan.getSelectedVehicleNo();
            }
        } else if (isVanBusnonac) {
            if (adapterBusnonacVan.getSelectedRadioButtonPosition() != -1) {
                selectedVehicle = adapterBusnonacVan.getSelectedVehicle();
                selectedVno = adapterBusnonacVan.getSelectedVehicleNo();
            }
        }
        if (!selectedVehicle.equals("nothing")) {
            showRequestDialog();
        } else {
            Toast.makeText(this, "Select a vehicle to confirm", Toast.LENGTH_SHORT).show();
        }

    }

    /*public static class VehicleViewHolder extends RecyclerView.ViewHolder {

        View mView;

        VehicleViewHolder(View itemView) {
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

    }*/

    public void searchButtonClicked(View view) {
        selectedVehicle = "nothing";
        if (!startDate.getText().toString().equals("Select Date") && !endDate.getText().toString().equals("Select Date")) {
            Button confirmButton = (Button) findViewById(R.id.confirm_button);
            Button searchButton = (Button) findViewById(R.id.search_button);
            boolean isnotCompleted = (!van.isChecked() && !bus.isChecked()) || (!ac.isChecked() && !nonac.isChecked());
            boolean isBus = !van.isChecked() && bus.isChecked() && ac.isChecked() && nonac.isChecked();
            boolean isBusac = !van.isChecked() && bus.isChecked() && ac.isChecked() && !nonac.isChecked();
            boolean isBusnonac = !van.isChecked() && bus.isChecked() && !ac.isChecked() && nonac.isChecked();
            boolean isVan = van.isChecked() && !bus.isChecked() && ac.isChecked();
            boolean isVanBus = van.isChecked() && bus.isChecked() && ac.isChecked() && nonac.isChecked();
            boolean isVanBusac = van.isChecked() && bus.isChecked() && ac.isChecked() && !nonac.isChecked();
            boolean isVanBusnonac = van.isChecked() && bus.isChecked() && !ac.isChecked() && nonac.isChecked();
            if (isnotCompleted) {
                vehicle_list.removeAllViewsInLayout();
                confirmButton.setVisibility(View.GONE);
            } else if (isBus) {
                progressBar.setVisibility(View.VISIBLE);
                vehicle_list.removeAllViewsInLayout();
                vehicle_list.setAdapter(adapterBus);
                confirmButton.setVisibility(View.VISIBLE);
                searchButton.setText("Update Search");
                progressBar.setVisibility(View.INVISIBLE);
            } else if (isBusac) {
                progressBar.setVisibility(View.VISIBLE);
                vehicle_list.removeAllViewsInLayout();
                vehicle_list.setAdapter(adapterBusac);
                confirmButton.setVisibility(View.VISIBLE);
                searchButton.setText("Update Search");
                progressBar.setVisibility(View.INVISIBLE);
            } else if (isBusnonac) {
                progressBar.setVisibility(View.VISIBLE);
                vehicle_list.removeAllViewsInLayout();
                vehicle_list.setAdapter(adapterBusnonac);
                confirmButton.setVisibility(View.VISIBLE);
                searchButton.setText("Update Search");
                progressBar.setVisibility(View.INVISIBLE);
            } else if (isVan) {
                progressBar.setVisibility(View.VISIBLE);
                vehicle_list.removeAllViewsInLayout();
                vehicle_list.setAdapter(adapterVan);
                confirmButton.setVisibility(View.VISIBLE);
                searchButton.setText("Update Search");
                progressBar.setVisibility(View.INVISIBLE);
            } else if (isVanBus) {
                progressBar.setVisibility(View.VISIBLE);
                vehicle_list.removeAllViewsInLayout();
                vehicle_list.setAdapter(adapterBusVan);
                confirmButton.setVisibility(View.VISIBLE);
                searchButton.setText("Update Search");
                progressBar.setVisibility(View.INVISIBLE);
            } else if (isVanBusac) {
                progressBar.setVisibility(View.VISIBLE);
                vehicle_list.removeAllViewsInLayout();
                vehicle_list.setAdapter(adapterBusacVan);
                confirmButton.setVisibility(View.VISIBLE);
                searchButton.setText("Update Search");
                progressBar.setVisibility(View.INVISIBLE);
            } else if (isVanBusnonac) {
                progressBar.setVisibility(View.VISIBLE);
                vehicle_list.removeAllViewsInLayout();
                vehicle_list.setAdapter(adapterBusnonacVan);
                confirmButton.setVisibility(View.VISIBLE);
                searchButton.setText("Update Search");
                progressBar.setVisibility(View.INVISIBLE);
            } else {
                vehicle_list.removeAllViewsInLayout();
                confirmButton.setVisibility(View.GONE);
                Toast.makeText(this, "No Vehicles", Toast.LENGTH_SHORT).show();
            }
        } else {
            Toast.makeText(this, "Select Dates", Toast.LENGTH_SHORT).show();
        }

    }

    public void showRequestDialog() {
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edittext_reason);

        dialogBuilder.setTitle("Request a Vehicle");
        dialogBuilder.setMessage("Enter reason below");
        dialogBuilder.setPositiveButton("Request", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //TODO something with edt.getText().toString(); update database
                mDatabaseUsers = mDatabaseUsers.child(mAuth.getCurrentUser().getUid()).child("requests");
                sendNotificationToUser(mAuth.getCurrentUser().getUid(), edt.getText().toString(), startDate, endDate, stime, etime, noPassengers.getText().toString().trim());

            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //pass
            }
        });
        final AlertDialog b = dialogBuilder.create();
        b.setOnShowListener( new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface arg0) {
                b.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.rgb(0, 255, 0));
            }
        });
        b.show();
    }

    public void sendNotificationToUser(final String userId, final String message, final TextView startDate, final TextView endDate, final String stime, final String etime, final String passengers) {
        final DatabaseReference userref = FirebaseDatabase.getInstance().getReference();
        final String date;
        if (stime == null || etime == null) {
            date = "from " + startDate.getText().toString() + " to " + endDate.getText().toString();
        }else{
            date = "from " + startDate.getText().toString() + " " + stime+ " to "
                    + endDate.getText().toString() + " " + etime;
        }

        final String[] userIndex = new String[1];
        final String[] userFaculty = new String[1];
        final String[] userDepartment = new String[1];
        final String[] userName = new String[1];

        userref.child("UserIdentities").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                userIndex[0] = (String) dataSnapshot.child(userId).getValue();

                userref.child("Users").child(userIndex[0]).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        userFaculty[0] = (String) dataSnapshot.child("faculty").getValue();
                        userDepartment[0] = (String) dataSnapshot.child("department").getValue();
                        userName[0] = (String) dataSnapshot.child("name").getValue();
                        //TODO get checked vehicles
                        final String key = userId + System.currentTimeMillis();
                        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("faculty").child(userFaculty[0])
                                .child(userDepartment[0]).child("notifications").child(key);


                        final Map notification = new HashMap<>();
                        notification.put("fromuserid", userId);
                        notification.put("message", message);
                        notification.put("name", userName[0]);
                        notification.put("faculty", userFaculty[0]);
                        notification.put("department", userDepartment[0]);
                        notification.put("date", date);
                        notification.put("indexNo", userIndex[0]);
                        notification.put("stime", stime);
                        notification.put("etime", etime);
                        notification.put("respond", "false");
                        notification.put("passengers", passengers);
                        notification.put("vehicle", selectedVehicle);
                        notification.put("vehicleNo", selectedVno);
                        notification.put("sdate", startDate.getText().toString().trim());
                        notification.put("edate", endDate.getText().toString().trim());


                        ref.updateChildren(notification).addOnSuccessListener(new OnSuccessListener() {
                            @Override
                            public void onSuccess(Object o) {
                                userref.child("Users").child(userIndex[0]).child("pendingnotifications").child(key).updateChildren(notification);
                            }
                        });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        //TODO Handle navigation view item clicks here.

        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment = null;
        Class fragmentClass = null;
        if (id == R.id.nav_pending) {
            fragmentClass = pendingFragment.class;
            findViewById(R.id.infol).setVisibility(View.GONE);
            findViewById(R.id.vehiclesl).setVisibility(View.GONE);
            findViewById(R.id.confirm_button).setVisibility(View.GONE);
            findViewById(R.id.fragmentmain).setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_request) {
            findViewById(R.id.fragmentmain).setVisibility(View.GONE);
            findViewById(R.id.infol).setVisibility(View.VISIBLE);
            findViewById(R.id.vehiclesl).setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_history) {
            fragmentClass = HistoryFragment.class;
            findViewById(R.id.infol).setVisibility(View.GONE);
            findViewById(R.id.vehiclesl).setVisibility(View.GONE);
            findViewById(R.id.confirm_button).setVisibility(View.GONE);
            findViewById(R.id.fragmentmain).setVisibility(View.VISIBLE);
        } else if (id == R.id.nav_logout) {
            final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, AlertDialog.THEME_HOLO_DARK);

            //dialogBuilder.setTitle("Request a Vehicle");
            dialogBuilder.setMessage("Are you sure want to logout?");
            dialogBuilder.setPositiveButton("Logout", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
                    final String user_id = mAuth.getCurrentUser().getUid();
                    db.child("UserIdentities").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.hasChild(user_id)) {
                                String indexNo = (String) dataSnapshot.child(user_id).getValue();
                                db.child("Users").child(indexNo).child("token").setValue("").addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        mAuth.signOut();
                                    }
                                });
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });

                }
            });
            dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    //pass
                }
            });
            final AlertDialog b = dialogBuilder.create();
            b.setOnShowListener( new DialogInterface.OnShowListener() {
                @Override
                public void onShow(DialogInterface arg0) {
                    b.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(Color.rgb(0, 255, 0));
                }
            });
            b.show();
        }
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (id != R.id.nav_request && id != R.id.nav_logout) {
            fragmentManager.beginTransaction().replace(R.id.fragmentmain, fragment).commit();
        }

        drawer.closeDrawer(GravityCompat.START);

        return true;
    }
}
