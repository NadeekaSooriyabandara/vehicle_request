package com.example.android.vehiclerequest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;

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

public class NotificationActivity extends AppCompatActivity {

    private DatabaseReference mDatabaseReference;
    private FirebaseAuth mAuth;
    private RecyclerView notification_list;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private NotificationAdapter notificationAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notification);
        setTitle("Notifications");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_back);
        Fresco.initialize(this);

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // back button pressed
                Intent intent = new Intent(NotificationActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });


        mAuth = FirebaseAuth.getInstance();
        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() == null) {
                    Intent loginIntent = new Intent(NotificationActivity.this, SigninSignupActivity.class);
                    loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(loginIntent);

                }
            }
        };
        mAuth.addAuthStateListener(mAuthListener);

        notification_list = (RecyclerView) findViewById(R.id.notification_list);
        notification_list.setHasFixedSize(true);
        notification_list.setLayoutManager(new LinearLayoutManager(this, LinearLayout.VERTICAL, false));

    }

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);

        final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
        final DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        final String user_id = mAuth.getCurrentUser().getUid();
        db.child("UserIdentities").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_id)) {
                    String indexNo = (String) dataSnapshot.child(user_id).getValue();

                    mDatabaseReference = mDatabase.getReference().child("Users").child(indexNo).child("notifications");

                    Query query = mDatabaseReference.orderByKey();
                    FirebaseRecyclerOptions<NotificationModel> firebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<NotificationModel>()
                            .setQuery(query, NotificationModel.class)
                            .build();
                    notificationAdapter = new NotificationAdapter(firebaseRecyclerOptions, NotificationActivity.this, mDatabaseReference, mAuth);
                    notificationAdapter.startListening();
                    notification_list.setAdapter(notificationAdapter);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        notificationAdapter.stopListening();
    }

}
