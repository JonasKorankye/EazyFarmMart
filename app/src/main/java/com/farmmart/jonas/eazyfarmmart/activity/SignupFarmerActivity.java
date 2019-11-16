package com.farmmart.jonas.eazyfarmmart.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.farmmart.jonas.eazyfarmmart.R;
import com.farmmart.jonas.eazyfarmmart.other.User;
import com.farmmart.jonas.eazyfarmmart.utils.AlertResponses;
import com.farmmart.jonas.eazyfarmmart.utils.ViewUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;


public class SignupFarmerActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword,inputFullname,inputPhoneNumber,inputFarmName,inputloc;
    private Button btnSignIn, btnSignUp, btnResetPassword;
    private ProgressBar progressBar;
    private FirebaseAuth auth;
    private Toolbar toolbar;
    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private String user_type = "Farmer";
    private String userId = "", fullname,farmName,location,number,password,email,pushId;
    public ArrayList<String> farm_products ;


    private static final String TAG = SignupFarmerActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_farmer_signup);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Register Farmer");
//        toolbar.setTitle("Register Farmer");
        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        btnSignIn = (Button) findViewById(R.id.sign_in_button);
        btnSignUp = (Button) findViewById(R.id.sign_up_button);
        inputEmail = (EditText) findViewById(R.id.email);
        inputPassword = (EditText) findViewById(R.id.password);
        inputFullname = (EditText) findViewById(R.id.fullname);
        inputPhoneNumber = (EditText) findViewById(R.id.phoneNumber);
        inputFarmName = (EditText) findViewById(R.id.farmName);
        inputloc = (EditText) findViewById(R.id.loc);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        btnResetPassword = (Button) findViewById(R.id.btn_reset_password);

//        mFirebaseInstance = FirebaseDatabase.getInstance();
//
//        // get reference to 'users' node
//        mFirebaseDatabase = mFirebaseInstance.getReference("users");
//
//        // store app title to 'app_title' node
//        mFirebaseInstance.getReference("app_title").setValue("EazyFarmMart");
//
//        // app_title change listener
//        mFirebaseInstance.getReference("app_title").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                Log.e(TAG, "App title updated");
//
//                String appTitle = dataSnapshot.getValue(String.class);
//
//                // update toolbar title
//                getSupportActionBar().setTitle(appTitle);
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.e(TAG, "Failed to read app title value.", error.toException());
//            }
//        });

        btnResetPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(SignupFarmerActivity.this, ResetPasswordActivity.class));
                ViewUtils.ActivityIn(SignupFarmerActivity.this,ResetPasswordActivity.class);
                System.out.println("Am in SignUpActivity");
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                startActivity(new Intent(SignupFarmerActivity.this, LoginActivity.class));
                ViewUtils.ActivityIn(SignupFarmerActivity.this,LoginActivity.class);
//                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                 email = inputEmail.getText().toString().trim();
                 password = inputPassword.getText().toString().trim();
                 fullname = inputFullname.getText().toString().trim();
                 farmName = inputFarmName.getText().toString().trim();
                 location = inputloc.getText().toString().trim();
                 number = inputPhoneNumber.getText().toString().trim();


                if (TextUtils.isEmpty(fullname)) {
                    Toast.makeText(getApplicationContext(), "Enter Full Name!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(getApplicationContext(), "Enter Email Address!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(getApplicationContext(), "Password too short, enter minimum 6 characters!", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(getApplicationContext(), "Enter password!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(number)) {
                    Toast.makeText(getApplicationContext(), "Enter Phone Number!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(farmName)) {
                    Toast.makeText(getApplicationContext(), "Enter Farm Name!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (TextUtils.isEmpty(location)) {
                    Toast.makeText(getApplicationContext(), "Enter Farm Location!", Toast.LENGTH_SHORT).show();
                    return;
                }

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupFarmerActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Toast.makeText(SignupFarmerActivity.this, "createUserWithEmail:onComplete:" + task.isSuccessful(), Toast.LENGTH_SHORT).show();
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {

                                    Toast.makeText(SignupFarmerActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    Log.d(TAG, "Authentication successful");
                                    createFirebaseUserProfile(task.getResult().getUser());

                                    FirebaseUser user = auth.getCurrentUser();
                                    userId = user.getUid();
                                    saveRegisteredFarmer();
                                }
                            }
                        });



//                    userId = auth.getCurrentUser().getUid();
//                    FirebaseUser user = auth.getCurrentUser();
//                    userId = user.getUid();
//                    createUser(fullname,  email, password, farmName , location,number );
//                    ViewUtils.ActivityIn(SignupFarmerActivity.this,HomeActivity.class);
//                    finish();


            }
        });
    }

    private void createFirebaseUserProfile(final FirebaseUser user) {
        UserProfileChangeRequest addProfileName = new UserProfileChangeRequest.Builder()
                .setDisplayName(fullname)
                .build();
        user.updateProfile(addProfileName)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            System.out.println("FirebaseUserProfile::"+ user.getDisplayName());
//                            Log.d(TAG, user.getDisplayName());
                        }
                    }
                });
    }


    private void saveRegisteredFarmer(){
//        farm_products = new ArrayList<>();
//        farm_products.add("");

            User userProfile = new User(fullname,  email, password, user_type,  number,  location,  farmName,"Yes","", farm_products);
        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference("users/farmer")
                .child(userId);
        System.out.println("userid::"+ userId);
//        DatabaseReference pushRef = ref.push();
//        pushId = pushRef.getKey();
        userProfile.setPushId(userId);
        ref.setValue(userProfile);


        Toast.makeText(SignupFarmerActivity.this, "Saved", Toast.LENGTH_SHORT).show();
//        ViewUtils.ActivityIn(SignupFarmerActivity.this,HomeActivity.class);
//                    finish();

        readData();

    }

    private void readData(){

        DatabaseReference mDatabase = FirebaseDatabase
                .getInstance()
                .getReference("users/farmer");

        mDatabase.child(userId).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

//                    Object value = dataSnapshot.getValue();
//                    Log.d(TAG, "Value is: " + value);

                    Log.d(TAG, "User name: " + user.getName() + ", email " + user.getEmail());



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




    }




    /**
     * Creating new user node under 'users'
     */
    private void createUser(String fullname,String email,String password,String farmName,String location,String number) {
         farm_products = new ArrayList<>();
         farm_products.add("wheat");

            System.out.println("uId::"+ userId);
//            userId = mFirebaseDatabase.push().getKey();


        User user = new User(fullname,  email, password, user_type,  number,  location,  farmName,"Yes","", farm_products);

        mFirebaseDatabase.child(userId).setValue(user);

        addUserChangeListener();
    }

    /**
     * User data change listener
     */
    private void addUserChangeListener() {
        // User data change listener
        mFirebaseDatabase.child(userId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = dataSnapshot.getValue(User.class);

                // Check for null
                if (user == null) {
                    Log.e(TAG, "User data is null!");
                    return;
                }

                Log.e(TAG, "User data is changed!" + user.name + ", " + user.email);
//                AlertResponses.showSuccess(SignupFarmerActivity.this,"User data is changed!","Updated Name/Email are "+ user.name + ", " + user.email);



            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.e(TAG, "Failed to read user", error.toException());
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }


    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this,LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(this,LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

