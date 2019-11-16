package com.farmmart.jonas.eazyfarmmart.activity;

/**
 * Created by JONAS on 5/28/2019.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.farmmart.jonas.eazyfarmmart.R;
import com.farmmart.jonas.eazyfarmmart.other.User;
import com.farmmart.jonas.eazyfarmmart.utils.ViewUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class LoginActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword;
    private FirebaseAuth auth;
    private ProgressBar progressBar;
    private Button btnSignup, btnLogin, btnReset;
    public  String user_id = "";
    Button button;
    AlertDialog alertDialog1;
    CharSequence[] values = {" Farmer "," Consumer"};

    private static final String TAG = LoginActivity.class.getSimpleName();

    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // set the view now
        setContentView(R.layout.activity_login);

//        Toolbar toolbar1 = (Toolbar) findViewById(R.id.toolbar1);
//        setSupportActionBar(toolbar1);

        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnSignup = (Button) findViewById(R.id.btn_signup);
        btnLogin = (Button) findViewById(R.id.btn_login);
        btnReset = (Button) findViewById(R.id.btn_reset_password);

        progressBar.setVisibility(View.VISIBLE);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();


        mAuthListener = new FirebaseAuth.AuthStateListener() {

            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
//                System.out.println("userAlive::" + user.getUid());

                if (user != null) {
                    user_id = user.getUid();
                    System.out.println("ulogID::"+ user_id);
                    findUserType();
//                    ViewUtils.ActivityIn(LoginActivity.this,HomeActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                    startActivity(intent);
//                    finish();
                }else{
                    progressBar.setVisibility(View.INVISIBLE);
                }
            }

        };



        btnSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                CreateAlertDialogWithRadioButtonGroup();

            }
        });

        btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ViewUtils.ActivityIn(LoginActivity.this,ResetPasswordActivity.class);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                loginWithPassword();

            }
        });
    }

    private void loginWithPassword() {
        String email = inputEmail.getText().toString();
        final String password = inputPassword.getText().toString();

        if (TextUtils.isEmpty(email)) {
            inputEmail.setError("Enter email address!");
//            Toast.makeText(getApplicationContext(), "Enter email address!", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(password)) {
            inputPassword.setError("Enter password!");
//            Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);

        //authenticate user
        auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        progressBar.setVisibility(View.GONE);
                        if (!task.isSuccessful()) {
                            // there was an error
                            if (password.length() < 6) {
                                inputPassword.setError(getString(R.string.minimum_password));
                            } else {
                                Toast.makeText(LoginActivity.this, getString(R.string.auth_failed), Toast.LENGTH_LONG).show();
                                showAlert(getString(R.string.auth_failed));
                            }
                        }
                    }
                });



    }

    private void findUserType() {
        System.out.println("findUdertype::");

        DatabaseReference mDatabase = FirebaseDatabase
                .getInstance()
                .getReference("users/farmer");

        mDatabase.child(user_id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                User user = dataSnapshot.getValue(User.class);

                Object value = dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + value);


                if (value == null){
                    System.out.println("Not found in farmer group");

                }else{
                    //rediect to farmer nav drawer
                    ViewUtils.ActivityIn(LoginActivity.this,HomeActivity.class);
                }

//                Log.d(TAG, "User name: " + user.getName() + ", email " + user.getEmail());



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //check if user is a consumer
        DatabaseReference cDatabase = FirebaseDatabase
                .getInstance()
                .getReference("users/consumer");

        cDatabase.child(user_id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                User consumer = dataSnapshot.getValue(User.class);

                Object value = dataSnapshot.getValue();
                Log.d(TAG, "Value is: " + value);


                if (value == null){
                    System.out.println("Not found in consumer group");

                }else{
                    //rediect to farmer nav drawer
                    ViewUtils.ActivityIn(LoginActivity.this,HomeConsumerActivity.class);
                }

//                Log.d(TAG, "User name: " + user.getName() + ", email " + user.getEmail());



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });


    }



    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            auth.removeAuthStateListener(mAuthListener);
        }
    }

    public void CreateAlertDialogWithRadioButtonGroup(){


        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);

        builder.setTitle("Register as a: ");

        builder.setSingleChoiceItems(values, 0,new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int item) {

                switch(item)
                {
                    case 0:


                        ViewUtils.ActivityIn(LoginActivity.this,SignupFarmerActivity.class);
                        break;
                    case 1:

                        Toast.makeText(LoginActivity.this, "Selected : Consumer", Toast.LENGTH_LONG).show();
                        ViewUtils.ActivityIn(LoginActivity.this,SignupConsumerActivity.class);
                        break;
                    case 2:

                        Toast.makeText(LoginActivity.this, "Third Item Clicked", Toast.LENGTH_LONG).show();
                        break;
                }
                alertDialog1.dismiss();
            }
        });
        alertDialog1 = builder.create();
        alertDialog1.show();

    }


    /**
     * Method to show alert dialog
     * */
    private void showAlert(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message)
                .setTitle("Response ")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }


}