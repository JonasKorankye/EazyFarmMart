package com.farmmart.jonas.eazyfarmmart.activity;

/**
 * Created by USG0011 on 06/05/17.
 */

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.farmmart.jonas.eazyfarmmart.R;
import com.farmmart.jonas.eazyfarmmart.other.FarmProduce;
import com.farmmart.jonas.eazyfarmmart.other.User;
import com.farmmart.jonas.eazyfarmmart.utils.CustomSimpleAdapter;
import com.farmmart.jonas.eazyfarmmart.utils.ExpandableWalListAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;


import static android.content.ContentValues.TAG;

public class FindFarmerActivity extends AppCompatActivity {
    String details = null;
    ExpandableWalListAdapter listAdapter;
    ExpandableListView expListView;
    List<String> listDataHeader; List<String> listDataHeader1;
    HashMap<String, List<String>> listDataChild; HashMap<String, List<String>> listDataChild1;
    String[] array;
    String[] data;
    String farmerNameElement;
    String emailElement;
    String farmNameElement;
    String deliveryElement;
    String foneElement,aboutFarmElement,pushIDElement;



    SwipeRefreshLayout mSwipeRefreshLayout;
    String mesg = null;
    String response2 = null;
    TextView mText;
    int responseCode = 0;
    private Toolbar toolbar;
    String user_id,farmName,phone,returnValue = "";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_tel_main);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        getSupportActionBar().setTitle("Available Farmers ");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);

        // get the listview
        expListView = (ExpandableListView) findViewById(R.id.liWalExp);
        mText = (TextView) findViewById(R.id.text);

//        mSwipeRefreshLayout.setColorSchemeResources(R.color.mediumaquamarine);


        readProduceData();

        // preparing list data
//        prepareListData();
//
//        expandableToDo();


//        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
//            @Override
//            public void onRefresh() {
//
//                mesg = MyMainActivity.userPhoneNum + "~" + MyMainActivity.userPassword;
//                new fireTellTrans().execute();
//                mSwipeRefreshLayout.setRefreshing(false);
//
//            }
//        });

    }

    private void readProduceData(){

        DatabaseReference mDatabase = FirebaseDatabase
                .getInstance()
                .getReference("users/farmer");

//        System.out.println("pushKey::"+ pushKey);
        mDatabase.addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Object value = dataSnapshot.getValue();
                    Log.d(TAG, "Farms Value is:: " + value);

//                List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
                List<String> top250 = new ArrayList<String>();
                listDataHeader = new ArrayList<String>();
                listDataChild = new HashMap<String, List<String>>();

                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    int count = 0;int number = 0;
                    count = count + 1;
                    User uDetails = new User();
                    uDetails.setName(ds.getValue(User.class).getName()); //set the cat
                    uDetails.setEmail(ds.getValue(User.class).getEmail()); //set the name
                    uDetails.setFarm_name(ds.getValue(User.class).getFarm_name()); //set the sale price
                    uDetails.setFarmer_delivery(ds.getValue(User.class).getFarmer_delivery()); //set the qty
                    uDetails.setUser_phone(ds.getValue(User.class).getUser_phone()); //set the qty
                    uDetails.setAbout_farm(ds.getValue(User.class).getAbout_farm()); //set the qty
                    uDetails.setPushId(ds.getValue(User.class).getPushId()); //set the qty

                    //display all the information
                    Log.d(TAG, "showData: name: " + uDetails.getName());
                    Log.d(TAG, "showData: mail: " + uDetails.getEmail());
                    Log.d(TAG, "showData: farmName: " + uDetails.getFarm_name());
                    Log.d(TAG, "showData: delivery: " + uDetails.getFarmer_delivery());
                    Log.d(TAG, "showData: phone details: " + uDetails.getUser_phone());
                    Log.d(TAG, "showData: about: " + uDetails.getAbout_farm());
                    Log.d(TAG, "showData: userID: " + uDetails.getPushId());
                    user_id = uDetails.getPushId();
                    farmName = uDetails.getFarm_name();
                    phone = uDetails.getUser_phone();
                    System.out.println("pushID::"+user_id);

                    returnValue = returnValue +"|"+ uDetails.getName()+ "~" +uDetails.getEmail()+ "~" +uDetails.getFarm_name()
                            + "~" + uDetails.getFarmer_delivery()+ "~" +  uDetails.getUser_phone() +  "~" + uDetails.getAbout_farm() + "~" + uDetails.getPushId()
                            ;
                    System.out.println("returnMehn::"+ returnValue);

                }

                prepareListData();

                expandableToDo();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }



    /*
     * Preparing the list data
     */
    private void prepareListData() {


         //Adding child data

        listDataHeader = new ArrayList<String>();
        listDataChild = new HashMap<String, List<String>>();
        // Adding child data
//        List<String> top250 = new ArrayList<String>();
        System.out.println("Do");
        int count = 0; int number = 0;
       data = returnValue.split("\\|");
        int k=1;
      while(k<data.length){
          List<String> top250 = new ArrayList<String>();
          System.out.println(data[k]);
          String subData = data[k];
          System.out.println(subData);
          count = count + 1;


          StringTokenizer tokenSub = new StringTokenizer(subData, "~");
//            for (int j=0; j<=tokenSub.countTokens();j++) {

          farmerNameElement = tokenSub.nextToken();//farmer name
          System.out.println(farmerNameElement);

          emailElement = tokenSub.nextToken();//email
          System.out.println(emailElement);

          farmNameElement = tokenSub.nextToken();//farm name
          System.out.println(farmNameElement);


          deliveryElement = tokenSub.nextToken();//delivery?
          System.out.println(deliveryElement);

          foneElement = tokenSub.nextToken();//fone
          System.out.println(foneElement);

//          aboutFarmElement = tokenSub.nextToken();//about farm
//          System.out.println(aboutFarmElement);


//          pushIDElement = tokenSub.nextToken();//pushID
//          System.out.println(pushIDElement);

          // Adding Header data
          listDataHeader.add("#" + count + " " + farmNameElement );

          // Adding child data
          // List<String> top250 = new ArrayList<String>();
          top250.add("Name: " + farmerNameElement);
          top250.add("Email: " + emailElement);
          top250.add("Phone Number: " + foneElement);
          top250.add("Delivery: " + deliveryElement);
//          top250.add("Tap to view ");



          // Header, Child data
          listDataChild.put(listDataHeader.get(number), top250);
            number++;
          k++;
      }

    }

    private void expandableToDo(){

        listAdapter = new ExpandableWalListAdapter(this, listDataHeader, listDataChild);

        // setting list adapter
        expListView.setAdapter(listAdapter);

        // Listview Group collasped listener
        expListView.setOnGroupCollapseListener(new ExpandableListView.OnGroupCollapseListener() {

            @Override
            public void onGroupCollapse(int groupPosition) {
//                Toast.makeText(getApplicationContext(),
//                        listDataHeader.get(groupPosition) + " Collapsed",
//                        Toast.LENGTH_SHORT).show();
                alerts( listDataHeader.get(groupPosition) );

            }
        });


        // Listview on child click listener
//        expListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
//
//
//            public boolean onChildClick(ExpandableListView parent, View v,
//                                        int groupPosition, int childPosition, long id) {
//
//                alerts(listDataChild.get(
//                        listDataHeader.get(groupPosition)).get(
//                        0));
//
//                Toast.makeText(
//                        getApplicationContext(),
//                        listDataHeader.get(groupPosition)
//                                + " : "
//                                + listDataChild.get(
//                                listDataHeader.get(groupPosition)).get(
//                                0), Toast.LENGTH_SHORT)
//                        .show();
//                return false;
//            }
//        });


    }




    //dialog for BBAN Selected
    public void alerts(final String selectedBatch) {
        android.support.v7.app.AlertDialog.Builder alertDialog = new android.support.v7.app.AlertDialog.Builder(
                FindFarmerActivity.this);
//
//        String [] mFarmName = selectedBatch.split(" ");
//        mFarmName[0];

        // Setting Dialog Title
        alertDialog.setTitle("View Products?");
        alertDialog.setMessage("You have selected : " + selectedBatch);

        alertDialog.setPositiveButton("OK",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog

//                        Intent intent = new Intent();
//                        intent.putExtra("editTextValue", sBBAN);
//                        setResult(RESULT_OK, intent);
//                        System.out.println("Am here::");
//                        finish();

//                        dialog.dismiss();

                        Intent transEnquiryIntent = new Intent(FindFarmerActivity.this, ViewFarmProduceByConsumer.class);
                        transEnquiryIntent.putExtra("user_id", user_id);
                        transEnquiryIntent.putExtra("farmName", farmName);
                        transEnquiryIntent.putExtra("phone", phone);
                        startActivity(transEnquiryIntent);



                    }
                });
//         Setting Negative "NO" Btn
        alertDialog.setNegativeButton("CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // Write your code here to execute after dialog
                        //
                        dialog.cancel();


                    }
                });

        // Showing Alert Dialog
        alertDialog.show();
    }









    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                FindFarmerActivity.this.finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }



    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub

        super.onBackPressed();
        finish();

    }
}
