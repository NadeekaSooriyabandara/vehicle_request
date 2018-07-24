package com.example.android.vehiclerequest;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.percent.PercentLayoutHelper;
import android.support.percent.PercentRelativeLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.util.ArrayList;
import java.util.Arrays;

public class SigninSignupActivity extends AppCompatActivity implements View.OnClickListener, android.widget.AdapterView.OnItemSelectedListener{

    private boolean isSigninScreen = true;
    private TextView tvSignupInvoker;
    private LinearLayout llSignup;
    private TextView tvSigninInvoker;
    private LinearLayout llSignin;
    private Button btnSignup;
    private Button btnSignin;
    LinearLayout llsignin,llsignup;

    private EditText loginEmail;
    private EditText loginPass;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabaseReference;
    private ProgressBar progressBarSignin, progressBarSignup;

    private Spinner spfaculty, spdepartment;
    private ArrayList<String> listApplied = new ArrayList<String>(Arrays.asList("Boteny", "Chemistry",
            "Computer Science", "Forestry and Environmental Sciences",
            "Food Science and Technology", "Mathematics", "Physics",
            "Statistics", "Zoology", "Sport Science"));
    private EditText nameField;
    private EditText passField;
    private EditText emailField, employeeField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin_signup);

        llSignin = (LinearLayout) findViewById(R.id.llSignin);
        llSignin.setOnClickListener(this);

        llSignup =(LinearLayout)findViewById(R.id.llSignup);
        llSignup.setOnClickListener(this);

        tvSignupInvoker = (TextView) findViewById(R.id.tvSignupInvoker);
        tvSigninInvoker = (TextView) findViewById(R.id.tvSigninInvoker);

        btnSignup= (Button) findViewById(R.id.btnSignup);
        btnSignin= (Button) findViewById(R.id.btnSignin);

        tvSignupInvoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSigninScreen = false;
                showSignupForm();
            }
        });

        tvSigninInvoker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isSigninScreen = true;
                showSigninForm();
            }
        });
        showSigninForm();

        loginEmail = (EditText) findViewById(R.id.login_email);
        loginPass = (EditText) findViewById(R.id.login_pass);
        progressBarSignin = (ProgressBar) findViewById(R.id.pb_signin);
        progressBarSignup = (ProgressBar) findViewById(R.id.pb_signup);

        spfaculty = (Spinner) findViewById(R.id.spinner_faculty);
        spdepartment = (Spinner) findViewById(R.id.spinner_department);
        addListenerOnSpinnerItemSelection();
        nameField = (EditText) findViewById(R.id.nameField);
        passField = (EditText) findViewById(R.id.passField);
        emailField = (EditText) findViewById(R.id.emailField);
        employeeField = (EditText) findViewById(R.id.employeeField);

        mAuth = FirebaseAuth.getInstance();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference();
    }

    public void signinClicked(View view) {
        Animation anticlockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_left_to_right);
        if(!isSigninScreen) {
            btnSignin.startAnimation(anticlockwise);
        }else {
            //TODO implement signin process here
            String email = loginEmail.getText().toString().trim();
            String pass = loginPass.getText().toString().trim();

            if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass)) {
                mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            progressBarSignin.setVisibility(View.VISIBLE);
                            checkUserExists();
                        } else {
                            Toast.makeText(SigninSignupActivity.this, task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Fill all the fields", Toast.LENGTH_SHORT).show();
            }
        }

    }

    public void signupClicked(View view) {
        Animation clockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_right_to_left);
        if(isSigninScreen) {
            btnSignup.startAnimation(clockwise);
        }else {
            //TODO implement signup process here
            final String name = nameField.getText().toString().trim();
            final String email = emailField.getText().toString().trim();
            String pass = passField.getText().toString();
            final String employee_id = employeeField.getText().toString().trim();
            final String faculty = spfaculty.getSelectedItem().toString().trim();
            final String department = spdepartment.getSelectedItem().toString().trim();

            if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(email) && !TextUtils.isEmpty(pass) && !TextUtils.isEmpty(employee_id)) {
                progressBarSignup.setVisibility(View.VISIBLE);
                mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            String user_id = mAuth.getCurrentUser().getUid();
                            DatabaseReference current_user_db = mDatabaseReference.child("Users").child(employee_id);
                            current_user_db.child("name").setValue(name);
                            current_user_db.child("userid").setValue(user_id);
                            current_user_db.child("email").setValue(email);
                            current_user_db.child("faculty").setValue(faculty);
                            current_user_db.child("department").setValue(department);
                            current_user_db.child("token").setValue(FirebaseInstanceId.getInstance().getToken());

                            DatabaseReference user_db = mDatabaseReference.child("UserIdentities");
                            user_db.child(user_id).setValue(employee_id);

                            DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Notifications");
                            reference.child("token").setValue(FirebaseInstanceId.getInstance()
                                    .getToken());

                            Intent mainIntent = new Intent(SigninSignupActivity.this, MainActivity.class);
                            mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK| Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(mainIntent);
                        } else {
                            progressBarSignup.setVisibility(View.INVISIBLE);
                            Toast.makeText(SigninSignupActivity.this, "Complete all the fields correctly", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            } else {
                Toast.makeText(this, "Complete all the fields to signup", Toast.LENGTH_SHORT).show();
            }
        }


    }

    private void showSignupForm() {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0.15f;
        llSignin.requestLayout();


        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.85f;
        llSignup.requestLayout();

        tvSignupInvoker.setVisibility(View.GONE);
        tvSigninInvoker.setVisibility(View.VISIBLE);
        Animation translate= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_right_to_left);
        llSignup.startAnimation(translate);

        Animation clockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_right_to_left);
        btnSignup.startAnimation(clockwise);

    }

    private void showSigninForm() {
        PercentRelativeLayout.LayoutParams paramsLogin = (PercentRelativeLayout.LayoutParams) llSignin.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoLogin = paramsLogin.getPercentLayoutInfo();
        infoLogin.widthPercent = 0.85f;
        llSignin.requestLayout();


        PercentRelativeLayout.LayoutParams paramsSignup = (PercentRelativeLayout.LayoutParams) llSignup.getLayoutParams();
        PercentLayoutHelper.PercentLayoutInfo infoSignup = paramsSignup.getPercentLayoutInfo();
        infoSignup.widthPercent = 0.15f;
        llSignup.requestLayout();

        Animation translate= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.translate_left_to_right);
        llSignin.startAnimation(translate);

        tvSignupInvoker.setVisibility(View.VISIBLE);
        tvSigninInvoker.setVisibility(View.GONE);
        Animation clockwise= AnimationUtils.loadAnimation(getApplicationContext(),R.anim.rotate_left_to_right);
        btnSignin.startAnimation(clockwise);
    }

    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.llSignin || v.getId() ==R.id.llSignup){
            InputMethodManager methodManager = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            methodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);

        }

    }

    private void checkUserExists() {
        final String user_id = mAuth.getCurrentUser().getUid();
        mDatabaseReference.child("UserIdentities").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.hasChild(user_id)) {
                    String indexNo = (String) dataSnapshot.child(user_id).getValue();
                    mDatabaseReference.child("Users").child(indexNo).child("token").setValue(FirebaseInstanceId
                            .getInstance().getToken()).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Intent loginIntent = new Intent(SigninSignupActivity.this, MainActivity.class);
                            loginIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(loginIntent);
                        }
                    });


                } else {
                    Toast.makeText(SigninSignupActivity.this, "User not found", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void addListenerOnSpinnerItemSelection() {
        //spfaculty = (Spinner) findViewById(R.id.spinner_faculty);
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
