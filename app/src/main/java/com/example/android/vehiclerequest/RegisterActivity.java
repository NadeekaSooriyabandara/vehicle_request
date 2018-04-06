package com.example.android.vehiclerequest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterActivity extends AppCompatActivity implements android.widget.AdapterView.OnItemSelectedListener {
    private Spinner spfaculty, spdepartment;

    private ArrayList<String> listApplied = new ArrayList<String>(Arrays.asList("Boteny", "Chemistry",
            "Computer Science", "Forestry and Environmental Sciences",
            "Food Science and Technology", "Mathematics", "Physics",
            "Statistics", "Zoology", "Sport Science"));

    private EditText nameField;
    private EditText passField;
    private EditText emailField;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        spfaculty = (Spinner) findViewById(R.id.spinner_faculty);
        spdepartment = (Spinner) findViewById(R.id.spinner_department);
        addListenerOnSpinnerItemSelection();

        nameField = (EditText) findViewById(R.id.nameField);
        passField = (EditText) findViewById(R.id.passField);
        emailField = (EditText) findViewById(R.id.emailField);
        progressBar = (ProgressBar) findViewById(R.id.register_progressBar);
        progressBar.setVisibility(View.INVISIBLE);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
        //mDatabaseReference = mDatabase.getReference().child("Users");


    }

    public void registerButtonClicked(View view) {
        final String name = nameField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String pass = passField.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
            progressBar.setVisibility(View.VISIBLE);
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {

                        String user_id = mAuth.getCurrentUser().getUid();
                        DatabaseReference current_user_db = mDatabaseReference.child("Users").child(user_id);
                        current_user_db.child("Name").setValue(name);
                        //current_user_db.child("image").setValue("default");

                        Intent mainIntent = new Intent(RegisterActivity.this, MainActivity.class);
                        mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        progressBar.setVisibility(View.INVISIBLE);
                        startActivity(mainIntent);
                    } else {
                        Toast.makeText(RegisterActivity.this, "Fill the text fields correctly", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        } else {
            Toast.makeText(this, "Fill all the text fields to register", Toast.LENGTH_SHORT).show();
        }
    }



    public void addListenerOnSpinnerItemSelection() {
        spfaculty = (Spinner) findViewById(R.id.spinner_faculty);
        spfaculty.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        ArrayList<String> list = new ArrayList<String>();
        if (adapterView.getItemAtPosition(i).toString().equals("Applied Sciences")) {
            list = listApplied;
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spdepartment.setAdapter(dataAdapter);
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
