package com.farmmart.jonas.eazyfarmmart.fragment;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.farmmart.jonas.eazyfarmmart.R;
import com.farmmart.jonas.eazyfarmmart.activity.HomeActivity;
import com.farmmart.jonas.eazyfarmmart.other.User;
import com.farmmart.jonas.eazyfarmmart.utils.ImageSliderAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import ir.apend.slider.model.Slide;
import ir.apend.slider.ui.Slider;
import me.relex.circleindicator.CircleIndicator;




/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link HomeFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public  HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
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

    private DatabaseReference myRef;
    private static ViewPager mPager;
    private static int currentPage = 0;
    public String u_id;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private static final Integer[] XMEN= {R.drawable.plantation,R.drawable.pineapples,R.drawable.farmer,R.drawable.pineapples,R.drawable.plantation};
    private ArrayList<Integer> XMENArray = new ArrayList<Integer>();
    private static final String TAG = HomeFragment.class.getSimpleName();

    EditText mFullname;
    EditText mEmail ;
    EditText mPhoneNumber ;
    EditText mDelivery;
    EditText mFarmName ;
    EditText mLoc;
    EditText mFarmAbout ;
    Button update_button ;

    String fullname ;
    String mail  ;
    String fone  ;
    String delivery;
    String farmName ;
    String loc;
    String aboutFarm;

    CoordinatorLayout coordinatorLayout;

    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        coordinatorLayout = (CoordinatorLayout) rootView.findViewById(R.id.coordinatorLayout);


         mFullname = rootView.findViewById(R.id.fullname);
         mEmail = rootView.findViewById(R.id.email);
         mPhoneNumber = rootView.findViewById(R.id.phoneNumber);
         mDelivery = rootView.findViewById(R.id.delivery);
         mFarmName = rootView.findViewById(R.id.farmName);
         mLoc = rootView.findViewById(R.id.loc);
         mFarmAbout = rootView.findViewById(R.id.farmAbout);
         update_button = rootView.findViewById(R.id.update_button);


        ImageSlider imageSlider = rootView.findViewById(R.id.image_slider);


        List<SlideModel> imageList = new ArrayList<>();

        // imageList.add(SlideModel("String Url" or R.drawable)
        // imageList.add(SlideModel("String Url" or R.drawable, "title") Also you can add title
        imageList.add(new SlideModel("https://fbsinnova.com//img/vue-mk-header.eae115b4.jpg", "Farm Produce Varieties "));
        imageList.add(new SlideModel(R.drawable.farmfresh, "Fresh Foods from the Farm"));
        imageList.add(new SlideModel("https://fbsinnova.com/img/innova3.d173da34.png", "Maize for Kenkey, Banku and Porridge"));
        imageList.add(new SlideModel("https://fbsinnova.com/img/innova1.239b3e1f.png","Cocoa for Healthy Living"));


        imageSlider.setImageList(imageList);

//        Slider slider = rootView.findViewById(R.id.slider);

//create list of slides
//        List<Slide> slideList = new ArrayList<>();
//        slideList.add(new Slide(0,"http://cssslider.com/sliders/demo-20/data1/images/picjumbo.com_img_4635.jpg" , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));
//        slideList.add(new Slide(1,"http://cssslider.com/sliders/demo-12/data1/images/picjumbo.com_hnck1995.jpg" , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));
//        slideList.add(new Slide(2,"http://cssslider.com/sliders/demo-19/data1/images/picjumbo.com_hnck1588.jpg" , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));
//        slideList.add(new Slide(3,"http://wowslider.com/sliders/demo-18/data1/images/shanghai.jpg" , getResources().getDimensionPixelSize(R.dimen.slider_image_corner)));
//
////handle slider click listener
//        slider.setItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                //do what you want
//                Toast.makeText(getActivity(), "Selected : Picked", Toast.LENGTH_LONG).show();
//            }
//        });
//
////add slides to slider
//        slider.addSlides(slideList);

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
//                } else {
//                    System.out.println("Couldn't find User");
//                }
//            }
//        };

        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

              fullname =   mFullname.getText().toString();
              mail =     mEmail.getText().toString() ;
              fone =     mPhoneNumber.getText().toString() ;
              delivery =     mDelivery.getText().toString();
              farmName =    mFarmName.getText().toString() ;
              loc =    mLoc.getText().toString();
              aboutFarm =    mFarmAbout.getText().toString() ;

                Snackbar snackbar = Snackbar
                        .make(coordinatorLayout, "Update Profile?", Snackbar.LENGTH_LONG)
                        .setAction("YES", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                updateData();
                                Toast.makeText(getContext(), "Updated!", Toast.LENGTH_SHORT).show();
//                                Snackbar snackbar1 = Snackbar.make(coordinatorLayout, "Profile is UPDATED!", Snackbar.LENGTH_SHORT);
//                                snackbar1.show();
                            }
                        });

                snackbar.show();
            }
        });

    return rootView;
    }

    private void readData(){

        DatabaseReference mDatabase = FirebaseDatabase
                .getInstance()
                .getReference("users/farmer");

        mDatabase.child(u_id).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                User user = dataSnapshot.getValue(User.class);

//                    Object value = dataSnapshot.getValue();
//                    Log.d(TAG, "Value is: " + value);

                Log.d(TAG, "User name: " + user.getName() + ", email " + user.getEmail());

                mFullname.setText(user.getName());
                mEmail.setText(user.getEmail()); ;
                mPhoneNumber.setText(user.getUser_phone());
                mDelivery.setText(user.getFarmer_delivery());
                mFarmName.setText(user.getFarm_name());
                mLoc.setText(user.getLocation());
                mFarmAbout.setText(user.about_farm);




            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

    }

    private void updateData() {
        // updating the user via child nodes

        DatabaseReference mDatabase = FirebaseDatabase
                .getInstance()
                .getReference("users/farmer");

        if (!TextUtils.isEmpty(fullname))
            mDatabase.child(u_id).child("name").setValue(fullname);

        if (!TextUtils.isEmpty(mail))
            mDatabase.child(u_id).child("email").setValue(mail);

        if (!TextUtils.isEmpty(fone))
            mDatabase.child(u_id).child("user_phone").setValue(fone);

        if (!TextUtils.isEmpty(delivery))
            mDatabase.child(u_id).child("farmer_delivery").setValue(delivery);

        if (!TextUtils.isEmpty(farmName))
            mDatabase.child(u_id).child("farm_name").setValue(farmName);

        if (!TextUtils.isEmpty(loc))
            mDatabase.child(u_id).child("location").setValue(loc);

        if (!TextUtils.isEmpty(aboutFarm))
            mDatabase.child(u_id).child("about_farm").setValue(aboutFarm);



    }

//    @Override
//    public void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthListener);
//    }
//
//    @Override
//    public void onStop() {
//        super.onStop();
//        if (mAuthListener != null) {
//            mAuth.removeAuthStateListener(mAuthListener);
//        }
//    }



    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

//    private void init() {
//        for(int i=0;i<XMEN.length;i++)
//            XMENArray.add(XMEN[i]);
//
////        mPager = (ViewPager) rootView.findViewById(R.id.pager);
//        mPager.setAdapter(new ImageSliderAdapter(getActivity(),XMENArray));
//        CircleIndicator indicator = (CircleIndicator) rootView.findViewById(R.id.indicator);
//        indicator.setViewPager(mPager);
//
//        // Auto start of viewpager
//        final Handler handler = new Handler();
//        final Runnable Update = new Runnable() {
//            public void run() {
//                if (currentPage == XMEN.length) {
//                    currentPage = 0;
//                }
//                mPager.setCurrentItem(currentPage++, true);
//            }
//        };
//        Timer swipeTimer = new Timer();
//        swipeTimer.schedule(new TimerTask() {
//            @Override
//            public void run() {
//                handler.post(Update);
//            }
//        }, 2500, 2500);
//    }

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
