package com.example.android.vehiclerequest;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link pendingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link pendingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class pendingFragment extends Fragment {

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



        return  view;
    }
}
