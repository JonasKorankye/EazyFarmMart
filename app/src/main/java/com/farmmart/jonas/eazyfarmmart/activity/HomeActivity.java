package com.farmmart.jonas.eazyfarmmart.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.farmmart.jonas.eazyfarmmart.R;
import com.farmmart.jonas.eazyfarmmart.fragment.EducateFragment;
import com.farmmart.jonas.eazyfarmmart.fragment.HomeFragment;
import com.farmmart.jonas.eazyfarmmart.fragment.SalesFragment;
import com.farmmart.jonas.eazyfarmmart.fragment.ProductsFragment;
import com.farmmart.jonas.eazyfarmmart.fragment.SettingsFragment;
import com.farmmart.jonas.eazyfarmmart.other.CircleTransform;
import com.farmmart.jonas.eazyfarmmart.utils.AlertResponses;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class HomeActivity extends AppCompatActivity
{

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;

    //Firebase db Stuff
//    private DatabaseReference mFirebaseDatabase;
    private FirebaseDatabase mFirebaseInstance;
    private DatabaseReference myRef;
    private  String userID,fireMail,fireName;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;


    // urls to load navigation header background image
    // and profile image
    private static final String urlNavHeaderBg = "http://api.androidhive.info/images/nav-menu-header-bg.jpg";
    private static final String urlProfileImg = "https://lh3.googleusercontent.com/eCtE_G34M9ygdkmOpYvCag1vBARCmZwnVS6rS5t4JLzJ6QgQSBquM0nuTsCpLhYbKljoyS-txg";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "My Profile";
    private static final String TAG_PHOTOS = "My Products";
    private static final String TAG_MOVIES = "My Sales Record";
    private static final String TAG_NOTIFICATIONS = "My Edu Content";
    private static final String TAG_SETTINGS = "Settings";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    FirebaseUser user;


    private static final String TAG = HomeActivity.class.getSimpleName();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
//        fab = (FloatingActionButton) findViewById(R.id.fab);

        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        txtWebsite = (TextView) navHeader.findViewById(R.id.website);
        imgNavHeaderBg = (ImageView) navHeader.findViewById(R.id.img_header_bg);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar1 titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

//        mFirebaseInstance = FirebaseDatabase.getInstance();

        // get reference to 'users' node
//        myRef = mFirebaseInstance.getReference("users");
//
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();
        System.out.println("U_ID:: "+ userID);
//
        fireMail = user.getEmail();
        fireName = user.getDisplayName();
        // name, email
        txtName.setText(fireName);
        txtWebsite.setText(fireMail);

//        mAuthListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                 user = firebaseAuth.getCurrentUser();
//                if (user != null) {
//                    // User is signed in
//                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());
//
//                    fireMail = user.getEmail();
//                    fireName = user.getDisplayName();
//                    // name, email
//                    txtName.setText(fireName);
//                    txtWebsite.setText(fireMail);
//                    System.out.println("Successfully signed in with: " + fireMail);
//                    AlertResponses.showSuccess(HomeActivity.this,"LOGGED IN","Successfully signed in with: " + fireMail);
//                } else {
//                    // User is signed out
//                    Log.d(TAG, "onAuthStateChanged:signed_out");
////                    AlertResponses.showSuccess(HomeActivity.this,"SIGNED OUT","Successfully signed out.");
//                }
//                // ...
//            }
//        };




//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Edit relevant fields in your profile.", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//
//            }
//        });

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }



    }

//    private void readUserData(){
//
//        mFirebaseDatabase.child(userID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
////                User user  = dataSnapshot.getValue(User.class);
////
////                Log.d(TAG, "showData: name: " + user.getName());
////                Log.d(TAG, "showData: fone: " + user.getUser_phone());
////                Log.d(TAG, "showData: about: " + user.getAbout_farm());
////                Log.d(TAG, "showData: name: " + user.getEmail());
////                Log.d(TAG, "showData: name: " + user.getFarm_name());
////                Log.d(TAG, "showData: name: " + user.getFarmer_delivery());
////                Log.d(TAG, "showData: name: " + user.getLocation());
////                Log.d(TAG, "showData: name: " + user.getPass());
////                Log.d(TAG, "showData: name: " + user.getFarm_products());
////                Log.d(TAG, "showData: name: " + user.getUser_type());
//
//                for (DataSnapshot childSnapshot: dataSnapshot.getChildren()) {
//                    User user = childSnapshot.getValue(User.class);
//
//                    Log.d(TAG, "showData: name: " + user.getName());
//                    Log.d(TAG, "showData: name: " + user.getUser_phone());
//                    Log.d(TAG, "showData: name: " + user.getAbout_farm());
//                    Log.d(TAG, "showData: name: " + user.getEmail());
//                    Log.d(TAG, "showData: name: " + user.getFarm_name());
//                    Log.d(TAG, "showData: name: " + user.getFarmer_delivery());
//                    Log.d(TAG, "showData: name: " + user.getLocation());
//                    Log.d(TAG, "showData: name: " + user.getPass());
//                    Log.d(TAG, "showData: name: " + user.getFarm_products());
//                    Log.d(TAG, "showData: name: " + user.getUser_type());
//                }
//
////                for(DataSnapshot ds : dataSnapshot.getChildren()){
////                    User userInfo = new User();
////                    userInfo.setName(ds.child(userID).getValue(User.class).getName()); //set the name
////                    userInfo.setEmail(ds.child(userID).getValue(User.class).getEmail()); //set the email
////                    userInfo.setUser_phone(ds.child(userID).getValue(User.class).getUser_phone()); //set the phone_num
////
//////                    //display all the information
//////                     Log.d(TAG, "showData: name: " + user.getName());
////                Log.d(TAG, "showData: fone: " + user.getUser_phone());
////                Log.d(TAG, "showData: about: " + user.getAbout_farm());
////                Log.d(TAG, "showData: name: " + user.getEmail());
////                Log.d(TAG, "showData: name: " + user.getFarm_name());
////                Log.d(TAG, "showData: name: " + user.getFarmer_delivery());
////                Log.d(TAG, "showData: name: " + user.getLocation());
////                Log.d(TAG, "showData: name: " + user.getPass());
////                Log.d(TAG, "showData: name: " + user.getFarm_products());
////                Log.d(TAG, "showData: name: " + user.getUser_type());
////
////
////                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError error) {
//                // Failed to read value
//                Log.w(TAG, "Failed to read value.", error.toException());
//            }
//        });
//
//    }

    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {
        // name, email

//        txtName.setText(fireName);
//        txtWebsite.setText(fireMail);

        // loading header background image
        Glide.with(this).load(R.drawable.nav_menu_header_zz)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgNavHeaderBg);

        // Loading profile image
        Glide.with(this).load(R.drawable.eazyfarm)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar1 title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
//        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
//            drawer.closeDrawers();
//
//            // show or hide the fab button
////            toggleFab();
//            return;
//        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        // show or hide the fab button
//        toggleFab();

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                HomeFragment homeFragment = new HomeFragment();
                return homeFragment;
            case 1:
                // photos
                ProductsFragment productsFragment = new ProductsFragment();
                return productsFragment;
            case 2:
                // movies fragment
                SalesFragment salesFragment = new SalesFragment();
                return salesFragment;
            case 3:
                // notifications fragment
                EducateFragment educateFragment = new EducateFragment();
                return educateFragment;

//            case 4:
//                // settings fragment
//                SettingsFragment settingsFragment = new SettingsFragment();
//                return settingsFragment;
            default:
                return new HomeFragment();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Setting Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_photos:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_PHOTOS;
                        break;
//                    startActivity(new Intent(HomeActivity.this, ProductsActivity.class));
//                    drawer.closeDrawers();
//                    return true;
                    case R.id.nav_movies:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_MOVIES;
                        break;
                    case R.id.nav_notifications:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_NOTIFICATIONS;
                        break;
//                    case R.id.nav_settings:
//                        navItemIndex = 4;
//                        CURRENT_TAG = TAG_SETTINGS;
//                        break;
                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(HomeActivity.this, AboutUsActivity.class));
                        drawer.closeDrawers();
                        return true;
//                    case R.id.nav_privacy_policy:
//                        // launch new intent instead of loading fragment
//                        startActivity(new Intent(HomeActivity.this, PrivacyPolicyActivity.class));
//                        drawer.closeDrawers();
//                        return true;
                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Setting the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }
        logout();
        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
//        if (shouldLoadHomeFragOnBackPress) {
//            // checking if user is on other navigation menu
//            // rather than home
//            if (navItemIndex != 0) {
//                navItemIndex = 0;
//                CURRENT_TAG = TAG_HOME;
//                loadHomeFragment();
//                return;
//            }
//        }

        super.onBackPressed();
    }




    private void logout() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        Toast.makeText(HomeActivity.this, "You just logged Out!", Toast.LENGTH_SHORT).show();
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.

        // show menu only when home fragment is selected
        if (navItemIndex == 0) {
            getMenuInflater().inflate(R.menu.main, menu);
        }

        // when fragment is notifications, load the menu created for notifications
//        if (navItemIndex == 3) {
//            getMenuInflater().inflate(R.menu.notifications, menu);
//        }
        return true;
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


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_logout) {
            logout();
//            Toast.makeText(getApplicationContext(), "Logout user!", Toast.LENGTH_LONG).show();
            return true;
        }

        // user is in notifications fragment
        // and selected 'Mark all as Read'
//        if (id == R.id.action_mark_all_read) {
//            Toast.makeText(getApplicationContext(), "All notifications marked as read!", Toast.LENGTH_LONG).show();
//        }

        // user is in notifications fragment
        // and selected 'Clear All'
//        if (id == R.id.action_clear_notifications) {
//            Toast.makeText(getApplicationContext(), "Clear all notifications!", Toast.LENGTH_LONG).show();
//        }

        return super.onOptionsItemSelected(item);
    }

    // show or hide the fab
//    private void toggleFab() {
//        if (navItemIndex == 0)
//            fab.show();
//        else
//            fab.hide();
//    }

//    @Override
//    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
//
//        int id = menuItem.getItemId();
//
////        if (id == R.id.open_account) {
////            // Handle the camera action
////            Intent accountsIntent = new Intent(QueryActivity.this, AccountOpening.class);
////            accountsIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
////            startActivity(accountsIntent);
////        } else if (id == R.id.treasury_in) {
////            Intent intent = new Intent(QueryActivity.this, TreasuryIn.class);
////            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//////            intent.putExtra("accountNum", accountNum);
////            startActivity(intent);
////        } else if (id == R.id.treasury_out) {
////            Intent intent = new Intent(QueryActivity.this, TreasuryOut.class);
////            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//////            intent.putExtra("accountNum", accountNum);
////            startActivity(intent);
////        }
//
//
//            DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        drawer.closeDrawer(GravityCompat.START);
//        return true;
//    }
}
