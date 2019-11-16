package com.farmmart.jonas.eazyfarmmart.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import com.farmmart.jonas.eazyfarmmart.R;
import com.farmmart.jonas.eazyfarmmart.other.FarmProduce;
import com.farmmart.jonas.eazyfarmmart.other.SalesRecords;
import com.farmmart.jonas.eazyfarmmart.utils.CustomFragmentSimpleAdapter;
import com.farmmart.jonas.eazyfarmmart.utils.CustomSimpleAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SalesFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SalesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SalesFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    View rootView;
    private FirebaseAuth mAuth;
    String user_id,price, farm_name, cat, name;
    ListView list;

    private OnFragmentInteractionListener mListener;

    public SalesFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment SalesFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static SalesFragment newInstance(String param1, String param2) {
        SalesFragment fragment = new SalesFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_sales, container, false);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        user_id = user.getUid();
        System.out.println("uID::"+ user_id);

        list = (ListView) rootView.findViewById(R.id.trans_list);


        readProduceSales();



        return rootView;
    }


    private void readProduceSales() {

        DatabaseReference mDatabase = FirebaseDatabase
                .getInstance()
                .getReference("users/farmer/" + user_id);

//        System.out.println("pushKey::"+ pushKey);
        mDatabase.child("sales").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                FarmProduce prodDetails = dataSnapshot.getValue(FarmProduce.class);

//                    Object value = dataSnapshot.getValue();
//                    Log.d(TAG, "Value is: " + value);

//                Log.d(TAG, "Produce_category: " + prodDetails.getProduce_category() + ", produce_name " + prodDetails.getProduce_name());

                final List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    SalesRecords salesRecords = new SalesRecords();
                    salesRecords.setProduce_category(ds.getValue(SalesRecords.class).getProduce_category()); //set the cat
                    salesRecords.setAmount_sold(ds.getValue(SalesRecords.class).getAmount_sold()); //set the name
                    salesRecords.setDate_sold(ds.getValue(SalesRecords.class).getDate_sold()); //set the sale price
                    salesRecords.setBought_by(ds.getValue(SalesRecords.class).getBought_by()); //set the qty
                    salesRecords.setProduce_name(ds.getValue(SalesRecords.class).getProduce_name()); //set the qty

                    //display all the information
                    Log.d(TAG, "showData: Cat: " + salesRecords.getProduce_category());
                    Log.d(TAG, "showData: Name: " + salesRecords.getProduce_name());
                    Log.d(TAG, "showData: Amount: " + salesRecords.getAmount_sold());
                    Log.d(TAG, "showData: DateSold: " + salesRecords.getDate_sold());
                    Log.d(TAG, "showData: Bought by: " + salesRecords.getBought_by());


                    HashMap<String, String> hm = new HashMap<String, String>();


                    hm.put("category",  salesRecords.getProduce_category());
                    hm.put("name", salesRecords.getProduce_name());
                    hm.put("sellprice", salesRecords.getAmount_sold());//took the place of transAmt
                    hm.put("dateSold", salesRecords.getDate_sold());
                    hm.put("boughtBuy", "Buyer: " + salesRecords.getBought_by());
                    //									hm.put("next", Integer.toString(next[0]) );

                    aList.add(hm);

                }


                String[] from = { /*"next",*/"category", "name", "sellprice", "dateSold"  , "boughtBuy"};

                // Ids of views in listview_layout
                int[] to = { /*R.id.flag,*/R.id.cat, R.id.name, R.id.price, R.id.qty , R.id.botBuy};

                // Instantiating an adapter to store each items
                // R.layout.listview_layout defines the layout of each item
                SimpleAdapter adapter = new CustomFragmentSimpleAdapter(getContext(), aList, R.layout.list_trans_sales, from, to);
                list.setAdapter(adapter);

//                list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view,
//                                            final int position, long id) {
//                        // TODO Auto-generated method stub
//
//                        System.out.println("itemList.get(position).values.entrySet()::" + aList.get(position).entrySet());
//                        Set<Map.Entry<String, String>> setOfEntries = aList.get(position).entrySet();
//                        for (Map.Entry<String, String> entry : setOfEntries) {
//                            System.out.println("Key : " + entry.getKey() + "\t\t" +
//                                    "Value : " + entry.getValue());
//
//
//                            if (entry.getKey().contains("category")) {
//                                System.out.println("entry for category::" + entry.getValue());
//                                cat = entry.getValue();
//                            }
//                            if (entry.getKey().contains("name")) {
//                                System.out.println("entry for name::" + entry.getValue());
//                                name = entry.getValue().toString();
//                            }
//                            if (entry.getKey().contains("sellprice")) {
//                                System.out.println("entry for qty::" + entry.getValue());
//
//                                price = entry.getValue().toString();
//                            }
//                        }
//
//                        Snackbar snackbar = Snackbar
//                                .make(linearLayout, "Confirm Purchase?", Snackbar.LENGTH_LONG)
//                                .setAction("YES", new View.OnClickListener() {
//                                    @Override
//                                    public void onClick(View view) {
//                                        System.out.println("details::" + aList.get(position).toString());
//                                        confirmNewBuy();
//
//
//                                    }
//                                });
//
//                        snackbar.show();
//
//
//                    }
//                });

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }




    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
//        if (context instanceof OnFragmentInteractionListener) {
//            mListener = (OnFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnFragmentInteractionListener");
//        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
