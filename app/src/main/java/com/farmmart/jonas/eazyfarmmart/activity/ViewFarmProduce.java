package com.farmmart.jonas.eazyfarmmart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.farmmart.jonas.eazyfarmmart.R;
import com.farmmart.jonas.eazyfarmmart.other.FarmProduce;
import com.farmmart.jonas.eazyfarmmart.utils.CustomSimpleAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.content.ContentValues.TAG;


public class ViewFarmProduce extends AppCompatActivity {


    private Toolbar toolbar;
    String user_id;
    ListView list;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_view_farm_produce);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("View Farm Produce");
        list = (ListView) findViewById(R.id.trans_list);


        user_id = getIntent().getExtras().getString("user_id").trim();

        readProduceData();


    }

    private void readProduceData(){

        DatabaseReference mDatabase = FirebaseDatabase
                .getInstance()
                .getReference("users/farmer/"+ user_id);

//        System.out.println("pushKey::"+ pushKey);
        mDatabase.child("produce").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                FarmProduce prodDetails = dataSnapshot.getValue(FarmProduce.class);

//                    Object value = dataSnapshot.getValue();
//                    Log.d(TAG, "Value is: " + value);

//                Log.d(TAG, "Produce_category: " + prodDetails.getProduce_category() + ", produce_name " + prodDetails.getProduce_name());

                List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();
                for(DataSnapshot ds : dataSnapshot.getChildren()){
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


                    HashMap<String, String> hm = new HashMap<String,String>();


                    hm.put("category", "Category: " + prodDetails.getProduce_category());
                    hm.put("name", " " + "Product: " + prodDetails.getProduce_name() + " ");
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




                String[] from = { /*"next",*/"category", "name","sellprice", "qty"  /*, "ledgerbal"*/ };

                // Ids of views in listview_layout
                int[] to = { /*R.id.flag,*/R.id.cat,R.id.name,R.id.price, R.id.qty /*, R.id.ledger_bal*/};

                // Instantiating an adapter to store each items
                // R.layout.listview_layout defines the layout of each item
                SimpleAdapter adapter = new CustomSimpleAdapter(ViewFarmProduce.this, aList, R.layout.list_trans, from, to);
                list.setAdapter(adapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

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

        super.onBackPressed();
        finish();

    }


}
