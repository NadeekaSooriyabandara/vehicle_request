package com.example.android.vehiclerequest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;


public class HistoryFragment extends Fragment {

    private RecyclerView mNotificationList;
    //private NotificationsAdapter notificationsAdapter;

    private List<Users> mNotifList;
    //private FirebaseFirestore mFirestore;
    private DatabaseReference mdb;
    private FirebaseAuth mAuth;

    public HistoryFragment() {
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
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        mNotifList = new ArrayList<>();

        //mNotificationList = (RecyclerView) view.findViewById(R.id.notification_list);
        //notificationsAdapter = new NotificationsAdapter(getContext(), mNotifList);

        mNotificationList.setHasFixedSize(true);
        mNotificationList.setLayoutManager(new LinearLayoutManager(container.getContext()));
        //mNotificationList.setAdapter(notificationsAdapter);

        //mFirestore = FirebaseFirestore.getInstance();
        mdb = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        String current_user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

    }
}
