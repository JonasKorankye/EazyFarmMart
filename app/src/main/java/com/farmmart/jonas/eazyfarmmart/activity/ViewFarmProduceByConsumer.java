package com.farmmart.jonas.eazyfarmmart.activity;

import android.Manifest;
import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.farmmart.jonas.eazyfarmmart.R;
import com.farmmart.jonas.eazyfarmmart.other.FarmProduce;
import com.farmmart.jonas.eazyfarmmart.other.SalesRecords;
import com.farmmart.jonas.eazyfarmmart.utils.CustomSimpleAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.ContentValues.TAG;


public class ViewFarmProduceByConsumer extends AppCompatActivity {

    LinearLayout linearLayout;
    private Toolbar toolbar;
    String user_id, phone, farm_name, cat, name,pushKey,date, price,consumer_name,u_id;
    ListView list;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_farm_produce_consumer);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        list = (ListView) findViewById(R.id.trans_list);
        linearLayout = (LinearLayout) findViewById(R.id.linear);


        user_id = getIntent().getExtras().getString("user_id").trim();
        farm_name = getIntent().getExtras().getString("farmName").trim();
        phone = getIntent().getExtras().getString("phone").trim();
        System.out.println("FONE::" + phone);
        getSupportActionBar().setTitle(farm_name);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
//        u_id = user.getUid();
        consumer_name = user.getDisplayName();

        System.out.println("Consumer_name::"+ consumer_name);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd  hh:mm:ss");
         date = sdf.format(new java.util.Date());

        readProduceData();


    }

    private void readProduceData() {

        DatabaseReference mDatabase = FirebaseDatabase
                .getInstance()
                .getReference("users/farmer/" + user_id);

//        System.out.println("pushKey::"+ pushKey);
        mDatabase.child("produce").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                FarmProduce prodDetails = dataSnapshot.getValue(FarmProduce.class);

//                    Object value = dataSnapshot.getValue();
//                    Log.d(TAG, "Value is: " + value);

//                Log.d(TAG, "Produce_category: " + prodDetails.getProduce_category() + ", produce_name " + prodDetails.getProduce_name());

                final List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    FarmProduce prodDetails = new FarmProduce();
                    prodDetails.setProduce_category(ds.getValue(FarmProduce.class).getProduce_category()); //set the cat
                    prodDetails.setProduce_name(ds.getValue(FarmProduce.class).getProduce_name()); //set the name
                    prodDetails.setProduce_sellprice(ds.getValue(FarmProduce.class).getProduce_sellprice()); //set the sale price
                    prodDetails.setProduce_qty(ds.getValue(FarmProduce.class).getProduce_qty()); //set the qty

                    //display all the information
                    Log.d(TAG, "showData: cat: " + prodDetails.getProduce_category());
                    Log.d(TAG, "showData: name: " + prodDetails.getProduce_name());
                    Log.d(TAG, "showData: sell price: " + prodDetails.getProduce_sellprice());
                    Log.d(TAG, "showData: qty: " + prodDetails.getProduce_qty());


                    HashMap<String, String> hm = new HashMap<String, String>();


                    hm.put("category", "Category: " + prodDetails.getProduce_category());
                    hm.put("name", "Product: " + prodDetails.getProduce_name() + " ");
                    hm.put("sellprice", "       " + "GHS" + " " + prodDetails.getProduce_sellprice());//took the place of transAmt
                    hm.put("qty", "Quantity: " + prodDetails.getProduce_qty());
                    //									hm.put("ledgerbal", "Ledger Bal                  " + transAmt);
                    //									hm.put("next", Integer.toString(next[0]) );

                    aList.add(hm);

//                    ArrayList<String> array  = new ArrayList<>();
//                    array.add(uInfo.getName());
//                    array.add(uInfo.getEmail());
//                    array.add(uInfo.getPhone_num());
//                    ArrayAdapter adapter = new ArrayAdapter(this,android.R.layout.simple_list_item_1,array);
//                    mListView.setAdapter(adapter);
                }


                String[] from = { /*"next",*/"category", "name", "sellprice", "qty"  /*, "ledgerbal"*/};

                // Ids of views in listview_layout
                int[] to = { /*R.id.flag,*/R.id.cat, R.id.name, R.id.price, R.id.qty /*, R.id.ledger_bal*/};

                // Instantiating an adapter to store each items
                // R.layout.listview_layout defines the layout of each item
                SimpleAdapter adapter = new CustomSimpleAdapter(ViewFarmProduceByConsumer.this, aList, R.layout.list_trans, from, to);
                list.setAdapter(adapter);

                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view,
                                            final int position, long id) {
                        // TODO Auto-generated method stub

                        System.out.println("itemList.get(position).values.entrySet()::" + aList.get(position).entrySet());
                        Set<Map.Entry<String, String>> setOfEntries = aList.get(position).entrySet();
                        for (Map.Entry<String, String> entry : setOfEntries) {
                            System.out.println("Key : " + entry.getKey() + "\t\t" +
                                    "Value : " + entry.getValue());


                            if (entry.getKey().contains("category")) {
                                System.out.println("entry for category::" + entry.getValue());
                                cat = entry.getValue();
                            }
                            if (entry.getKey().contains("name")) {
                                System.out.println("entry for name::" + entry.getValue());
                                name = entry.getValue().toString();
                            }
                            if (entry.getKey().contains("sellprice")) {
                                System.out.println("entry for qty::" + entry.getValue());

                                price = entry.getValue().toString();
                            }
                        }

                        Snackbar snackbar = Snackbar
                                .make(linearLayout, "Confirm Purchase?", Snackbar.LENGTH_LONG)
                                .setAction("YES", new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        System.out.println("details::" + aList.get(position).toString());
                                        confirmNewBuy();


                                    }
                                });

                        snackbar.show();


                    }
                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }






    public void confirmNewBuy() {

        // custom dialog
        final Dialog dialog = new Dialog(ViewFarmProduceByConsumer.this);
        dialog.setContentView(R.layout.dialog_alert_fundstransfer);
        //dialog.setTitle(R.string.confirmDepo);
        dialog.setCancelable(false);


        // set the custom dialog components - text, image and button
        TextView fromText = (TextView) dialog.findViewById(R.id.AccNumTextView);

        fromText.setText(cat);

        TextView toText = (TextView) dialog.findViewById(R.id.wiAmtTextView);

        toText.setText(name);

        TextView transAmtText = (TextView) dialog.findViewById(R.id.paCoTextView);
        transAmtText.setText("Price:" + " " + price);

//        TextView transByText = (TextView) dialog.findViewById(R.id.patransTextView);
//        transByText.setText(" " + narrationBy);


        Button dialogButtonCancel = (Button) dialog.findViewById(R.id.cancel_button);
        Button dialogButtonConfirm = (Button) dialog.findViewById(R.id.confirm_ok_button);

        // if button is clicked, close the custom dialog
        dialogButtonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

            }
        });

        dialogButtonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                addSalesRecord();

                //make call to number....
                Intent dialIntent = new Intent();
                dialIntent.setAction(Intent.ACTION_DIAL);
                dialIntent.setData(Uri.parse("tel:"+ phone));

                startActivity(dialIntent);
                dialog.dismiss();
            }
        });

        dialog.show();
        Window window = dialog.getWindow();
        window.setLayout(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }



    private void addSalesRecord(){


        SalesRecords sales = new SalesRecords(name,cat,price,date,consumer_name);

        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference("users/farmer/"+ user_id)
                .child("sales");

        DatabaseReference pushRef = ref.push();
        pushKey = pushRef.getKey();

        System.out.println("pushKey::"+ pushKey);

        pushRef.setValue(sales);

        Toast.makeText(ViewFarmProduceByConsumer.this, "Sales Records has been UPDATED!", Toast.LENGTH_LONG).show();

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        // TODO Auto-generated method stub
        Intent intent = new Intent(this,HomeConsumerActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        super.onBackPressed();
        finish();

    }


}
