package com.example.android.vehiclerequest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;


public class pendingFragment extends Fragment {

    private RecyclerView mPendingList;
    private pendingadapter pendingAdapter;

    private List<NotificationModel> mPendList;
    private DatabaseReference mdb;
    private FirebaseAuth mAuth;

    public pendingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_pending, container, false);

        mPendList = new ArrayList<>();

        mPendingList = (RecyclerView) view.findViewById(R.id.pending_list);
        pendingAdapter = new pendingadapter(getContext(), mPendList);

        mPendingList.setHasFixedSize(true);
        mPendingList.setLayoutManager(new LinearLayoutManager(container.getContext()));
        mPendingList.setAdapter(pendingAdapter);

        mdb = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();



        return  view;
    }

    @Override
    public void onStart() {
        super.onStart();

        mPendList.clear();
        String current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mdb.child("UserIdentities").child(current_user_id).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                final String indexNo = (String) dataSnapshot.getValue();
                mdb.child("Users").child(indexNo).child("pendingnotifications").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        mPendList.clear();
                        for (DataSnapshot messageSnapshot: dataSnapshot.getChildren()) {
                            //String name = (String) messageSnapshot.child("name").getValue();
                            //String message = (String) messageSnapshot.child("message").getValue();
                            String user_id = messageSnapshot.getKey();
                            NotificationModel notifications = messageSnapshot.getValue(NotificationModel.class).withId(user_id);
                            mPendList.add(notifications);

                            pendingAdapter.notifyDataSetChanged();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }
}
