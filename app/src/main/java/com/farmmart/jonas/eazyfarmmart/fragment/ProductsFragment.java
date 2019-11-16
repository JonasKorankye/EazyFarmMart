package com.farmmart.jonas.eazyfarmmart.fragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.farmmart.jonas.eazyfarmmart.R;
import com.farmmart.jonas.eazyfarmmart.activity.ShowImagesActivity;
import com.farmmart.jonas.eazyfarmmart.activity.UploadFarmActivity;
import com.farmmart.jonas.eazyfarmmart.activity.ViewFarmProduce;
import com.farmmart.jonas.eazyfarmmart.other.FarmProduce;
import com.farmmart.jonas.eazyfarmmart.other.User;
import com.farmmart.jonas.eazyfarmmart.utils.CustomSimpleAdapter;
import com.farmmart.jonas.eazyfarmmart.utils.ViewUtils;
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

import static android.content.ContentValues.TAG;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductsFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Spinner catSpinner;
    EditText mProduceName,mSellingPrice,mQty;
    Button view_button,add_button;
    RadioButton radio_produceList,radio_uploads;
    RadioGroup radio_custToDetails;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    String prodName,selPrice,qty, pushKey ,prodCategory;
    public ArrayList<String> farm_products ;
    CardView depoChooseCardView;
    ListView list;

    String prodList[] = {"--Select Category--","Livestock & Diary", "Fruits", "Vegetables","Cereals & Grains","Tubers & Nuts","Cash Crops"};

    private OnFragmentInteractionListener mListener;

    CoordinatorLayout coordinatorLayout;

    public ProductsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductsFragment newInstance(String param1, String param2) {
        ProductsFragment fragment = new ProductsFragment();
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

    View rootView;
    public String u_id,userId;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
         rootView = inflater.inflate(R.layout.fragment_photos, container, false);


        catSpinner = (Spinner) rootView.findViewById(R.id.choose_farmCat);
        mProduceName = rootView.findViewById(R.id.editText1);//Produce Name
        mSellingPrice = rootView.findViewById(R.id.editText2);//Selling Price
        mQty = rootView.findViewById(R.id.editText3);//Available Qty
        view_button = rootView.findViewById(R.id.viewButton);//viewButton
        add_button = rootView.findViewById(R.id.addButton); //addButton

        list = (ListView) rootView.findViewById(R.id.trans_list);

        radio_produceList = (RadioButton) rootView. findViewById(R.id.radio_to_custNo);
        radio_uploads = (RadioButton) rootView.findViewById(R.id.radio_to_BBAN);
        radio_custToDetails = (RadioGroup) rootView.findViewById(R.id.radio_custToDetails);
//        depoChooseCardView = (CardView) rootView.findViewById(R.id.depoChooseCardView);
        radio_custToDetails.clearCheck();
        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);


        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        u_id = user.getUid();
        System.out.println("uID::"+ u_id);
        readData();

//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                FirebaseUser user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    u_id = user.getUid();
//                    System.out.println("uID::"+ u_id);
//                    readData();
//
//                } else {
//                    System.out.println("Couldn't find User");
//                }
//            }
//        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, prodList);

        // Drop down layout style - list view with radio button
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        catSpinner.setAdapter(adapter);

        catSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {

                switch (position) {
                    case 0:
                        Toast.makeText(parent.getContext(), "Select Category", Toast.LENGTH_LONG).show();
                    case 1:
                        prodCategory = "Livestock & Diary";
                        break;
                    case 2:
                        prodCategory = "Fruits";
                        break;
                    case 3:
                        prodCategory = "Vegetables";
                        break;
                    case 4:
                        prodCategory = "Cereals & Grains";
                        break;
                    case 5:
                        prodCategory = "Tubers & Nuts";
                        break;
                    case 6:
                        prodCategory = "Cash Crops";
                        break;


                    default:
                        break;
                }
            }

            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
                Toast.makeText(parent.getContext(), "Select Category", Toast.LENGTH_LONG).show();

            }
        });




        view_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                readProduceData();
            }
        });



        radio_custToDetails.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()
        {
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                // checkedId is the RadioButton selected
//                boolean checked = ((RadioButton) view).isChecked();

                // Check which radio button was clicked
                switch(checkedId) {
                    case R.id.radio_to_custNo:
                            Intent d = new Intent(getActivity(), ViewFarmProduce.class);
                            d.putExtra("user_id", u_id);

                            startActivity(d);
                        break;
                    case R.id.radio_to_BBAN:
                            ViewUtils.ActivityIn(getActivity(),ShowImagesActivity.class);
                        break;
            }
        }});

        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prodName = mProduceName.getText().toString();
                selPrice = mSellingPrice.getText().toString();
                qty = mQty.getText().toString();

                if (TextUtils.isEmpty(prodName)) {
                    mProduceName.setError("Enter Name of Farm Produce");
                }else if (TextUtils.isEmpty(selPrice)) {
                    mProduceName.setError("Enter Selling Price");
                }else if (TextUtils.isEmpty(qty)) {
                    mQty.setError("Enter Selling Price");
                }else {
//                addProduceRecord = prodCategory + ";" + prodName + ";" + selPrice + ";" + qty;
//                    addFarmerProduce();

                    Intent d = new Intent(getActivity(), UploadFarmActivity.class);
                    d.putExtra("user_id", u_id);
                    d.putExtra("prodName", prodName);
                    d.putExtra("prodCategory", prodCategory);
                    d.putExtra("selPrice", selPrice);
                    d.putExtra("qty", qty);

                    startActivity(d);

                    mProduceName.setText("");
                    mSellingPrice.setText("");
                    mQty.setText("");

//                    ViewUtils.ActivityIn(getActivity(),UploadFarmActivity.class);
                }
            }
        });


        return rootView;
    }



    private void addFarmerProduce(){



        FarmProduce prodProfile = new FarmProduce(prodName,prodCategory,selPrice,qty);

        DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference("users/farmer/"+ u_id)
                .child("produce");

        DatabaseReference pushRef = ref.push();
        pushKey = pushRef.getKey();

        System.out.println("pushKey::"+ pushKey);

        pushRef.setValue(prodProfile);

                mProduceName.setText("");
                mSellingPrice.setText("");
                mQty.setText("");
        Toast.makeText(getActivity(), "Farm Produce has been ADDED!", Toast.LENGTH_LONG).show();

//        Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Produce has been ADDED!", Snackbar.LENGTH_SHORT);
//        snackbar1.show();
    }



    private void readData(){

        DatabaseReference mDatabase = FirebaseDatabase
                .getInstance()
                .getReference("users/farmer");

        mDatabase.child(u_id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                User user = dataSnapshot.getValue(User.class);

                    Object value = dataSnapshot.getValue();
                    Log.d(TAG, "Value is: " + value);

//                Log.d(TAG, "User name: " + user.getName() + ", email " + user.getEmail());



            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




    }



    private void readProduceData(){

        Intent d = new Intent(getActivity(), ViewFarmProduce.class);
        d.putExtra("user_id", u_id);

        startActivity(d);

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

//    @Override
//    public void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
////
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }


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
