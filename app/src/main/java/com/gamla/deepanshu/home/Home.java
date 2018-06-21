package com.gamla.deepanshu.home;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gamla.deepanshu.firebase.Config;
import com.gamla.deepanshu.firebase.NotificationUtils;
import com.gamla.deepanshu.gamla.AboutUs;
import com.gamla.deepanshu.Database.DatabaseHandler;
import com.gamla.deepanshu.ProductList.ProductListFragment;
import com.gamla.deepanshu.ShoppingCart.ItemBag;
import com.gamla.deepanshu.gamla.Login;
import com.gamla.deepanshu.MyOrder.MyOrder;
import com.gamla.deepanshu.gamla.MainActivity;
import com.gamla.deepanshu.gamla.PrivacyPolicy;
import com.gamla.deepanshu.gamla.R;
import com.gamla.deepanshu.WishList.WishList;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;


public class Home extends AppCompatActivity implements ProductListFragment.OnFragmentInteractionListener {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView imgNavHeaderBg, imgProfile;
    private TextView txtName, txtWebsite;
    private Toolbar toolbar;
    private FloatingActionButton fab;
    public static int navItemIndex = 0;
    private static final String TAG_HOME = "home";
    private static final String TAG_WISHLIST = "WishList";
    private static final String TAG_INDOOR_PLANTS = "Indoor Plants";
    private static final String TAG_OUTDOOR_PLANTS = "Outdoor Plants";
    private static final String TAG_ANTIQUIES = "Antiquies With Plants";
    private static final String TAG_FRUIT_PLANTS = "Fruit Plants";
    private static final String TAG_GREEN_HOUSE = "Green House Plants";
    private static final String TAG_SEEDS = "Seeds";
    private static final String TAG_lANDSCAPERS = "Landscapers";
    private static final String TAG_POTS = "Pots";
    private static final String TAG_GARDENING_TOOLS = "Gardening Tools";
    public static String CURRENT_TAG = TAG_HOME;
    private String[] activityTitles;
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;
    ArrayList<String> objUserArrayList = new ArrayList<>();
    private BroadcastReceiver mRegistrationBroadcastReceiver;
    private static final int MAKE_CALL_PERMISSION_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home);

        //=============================================================================//
        mRegistrationBroadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                // checking for type intent filter
                if (intent.getAction().equals(Config.REGISTRATION_COMPLETE)) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL);


                } else if (intent.getAction().equals(Config.PUSH_NOTIFICATION)) {
                    // new push notification is received

                    String message = intent.getStringExtra("message");

                    Toast.makeText(getApplicationContext(), "Push notification: " + message, Toast.LENGTH_LONG).show();

                    // txtMessage.setText(message);
                }
            }
        };

        //=============================================================================//
        DatabaseHandler dh = new DatabaseHandler(this);
        objUserArrayList = dh.getUserRecord();

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitleTextColor(Color.WHITE);
        toolbar.setSubtitleTextColor(Color.WHITE);
        mHandler = new Handler();
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navHeader = navigationView.getHeaderView(0);

        TextView txtUserName = navHeader.findViewById(R.id.useremailadress);
        try {
            txtUserName.setText(objUserArrayList.get(0));
        }catch (Exception e)
        {
            e.printStackTrace();
        }
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);
        loadNavHeader();
        setUpNavigationView();
        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }
    }

    private void loadNavHeader() {
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);

    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        selectNavMenu();
        setToolbarTitle();
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
        drawer.closeDrawers();
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {

            case 0:
                // home
              //  Toast.makeText(Home.this,"hello",Toast.LENGTH_SHORT).show();
                ProductCategoryFragment pFragment = new ProductCategoryFragment();
                return pFragment;
           /* case 0:
                // home
                ProductListFragment productListFragment = new ProductListFragment();
                return productListFragment;*/
            case 5:
                // photos
                 ProductListFragment productListFragment = ProductListFragment.newInstance("Indoor Plants","");
                return productListFragment;
            case 6:
                // movies fragment
                ProductListFragment productListFragment1 = ProductListFragment.newInstance("Outdoor Plants","");
                return productListFragment1;
            case 7:
                // notifications fragment
                ProductListFragment productListFragment2 = ProductListFragment.newInstance("Pots","");
                return productListFragment2;
            case 8:
                // notifications fragment
                ProductListFragment productListFragment7 = ProductListFragment.newInstance("Antiquies With Plants","");
                return productListFragment7;

            case 9:
                // settings fragment
                ProductListFragment productListFragment3 = ProductListFragment.newInstance("Fruit Plants","");
                return productListFragment3;
            case 10:
                // movies fragment
                ProductListFragment productListFragment4 = ProductListFragment.newInstance("Green House Plants","");
                return productListFragment4;
            case 11:
                // notifications fragment
                ProductListFragment productListFragment5 = ProductListFragment.newInstance("Seeds","");
                return productListFragment5;

            case 12:
                // settings fragment
                ProductListFragment productListFragment6 = ProductListFragment.newInstance("Landscapers","");
                return productListFragment6;
            case 13:
                // settings fragment
                ProductListFragment productListFragment8 = ProductListFragment.newInstance("Gardening Tools","");
                return productListFragment8;


            default:
                ProductCategoryFragment pFragment1 = new ProductCategoryFragment();
                return pFragment1;
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
      //  navigationView.getMenu().getItem(navItemIndex).setChecked(true);
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
                    case R.id.nav_home:
                      //  Toast.makeText(Home.this,"hello00",Toast.LENGTH_SHORT).show();
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_wishlislist:
                        navItemIndex = 3;
                        DatabaseHandler dh = new DatabaseHandler(Home.this);
                        ArrayList<String> objArrylist;
                        objArrylist = dh.getUserRecord();
                        if(objArrylist.size()>0) {

                            drawer.closeDrawers();

                            startActivityForResult(new Intent(Home.this,WishList.class),101);
                            overridePendingTransition(R.anim.left_to_right,R.anim.hold);
                        }
                        else
                        {
                            startActivityForResult(new Intent(Home.this,Login.class),101);
                            overridePendingTransition(R.anim.left_to_right,R.anim.hold);
                        }

                        return true;

                    case R.id.nav_cart:
                        navItemIndex = 1;
                        DatabaseHandler dh1 = new DatabaseHandler(Home.this);
                        ArrayList<String> objArrylist1;
                        objArrylist1 = dh1.getUserRecord();
                        if(objArrylist1.size()>0) {

                            drawer.closeDrawers();

                            startActivityForResult(new Intent(Home.this,ItemBag.class),101);
                            overridePendingTransition(R.anim.left_to_right,R.anim.hold);
                        }
                        else
                        {
                            startActivityForResult(new Intent(Home.this,Login.class),101);
                            overridePendingTransition(R.anim.left_to_right,R.anim.hold);
                        }

                        return true;
                    case R.id.nav_your_order:
                        navItemIndex = 2;
                        DatabaseHandler dh2 = new DatabaseHandler(Home.this);
                        ArrayList<String> objArrylist2;
                        objArrylist2 = dh2.getUserRecord();
                        if(objArrylist2.size()>0) {

                            drawer.closeDrawers();
                            startActivityForResult(new Intent(Home.this,MyOrder.class),101);
                            overridePendingTransition(R.anim.left_to_right,R.anim.hold);
                        }
                        else
                        {
                            startActivityForResult(new Intent(Home.this,Login.class),101);
                            overridePendingTransition(R.anim.left_to_right,R.anim.hold);
                        }

                        return true;

                    case R.id.nav_indoorplants:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_INDOOR_PLANTS;
                        break;
                    case R.id.nav_outdoorplants:
                        navItemIndex = 6;
                        CURRENT_TAG = TAG_OUTDOOR_PLANTS;
                        break;
                    case R.id.nav_Pots:
                        navItemIndex = 7;
                        CURRENT_TAG = TAG_POTS;
                        break;
                    case R.id.nav_Antiquies_with_plants:
                        navItemIndex = 8;
                        CURRENT_TAG = TAG_ANTIQUIES;
                        break;
                    case R.id.nav_FruitPlants:
                        navItemIndex = 9;
                        CURRENT_TAG = TAG_FRUIT_PLANTS;
                        break;
                    case R.id.nav_Greenhouse:
                        navItemIndex = 10;
                        CURRENT_TAG = TAG_GREEN_HOUSE;
                        break;
                    case R.id.nav_seeds:
                        navItemIndex = 11;
                        CURRENT_TAG = TAG_SEEDS;
                        break;
                    case R.id.nav_LandScaperes:
                        navItemIndex = 12;
                        CURRENT_TAG = TAG_lANDSCAPERS;
                        break;
                    case R.id.nav_gardening:
                        navItemIndex = 13;
                        CURRENT_TAG = TAG_GARDENING_TOOLS;
                        break;
                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                        startActivityForResult(new Intent(Home.this, AboutUs.class),111);
                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_contact_us:
                        // launch new intent instead of loading fragment
                     //   startActivity(new Intent(Home.this, AboutUs.class));

                        if (checkPermission(Manifest.permission.CALL_PHONE)) {
                            String dial = "tel:7991898496";
                            startActivity(new Intent(Intent.ACTION_CALL, Uri.parse(dial)));
                        } else {
                            Toast.makeText(Home.this, "Permission Call Phone denied", Toast.LENGTH_SHORT).show();
                        }
                        drawer.closeDrawers();
                        return true;
                    case R.id.nav_privacy_policy:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(Home.this, PrivacyPolicy.class));
                        drawer.closeDrawers();
                        return true;
                    default:

                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
               // menuItem.setChecked(true);

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

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
        }

        super.onBackPressed();
    }


    // show or hide the fab
    private void toggleFab() {
       /* if (navItemIndex == 0)
            fab.show();
        else
            fab.hide();*/
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){

        getMenuInflater().inflate(R.menu.cart, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.shoppingcartt:   //this item has your app icon
                System.out.println("------------------------------>>>"+item.getItemId());
                Intent i = new Intent(Home.this,ItemBag.class);
                startActivityForResult(i,101);
                overridePendingTransition(R.anim.slide_down,R.anim.hold);
                return true;





            default: return super.onOptionsItemSelected(item);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();

        // register GCM registration complete receiver
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.REGISTRATION_COMPLETE));

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                new IntentFilter(Config.PUSH_NOTIFICATION));

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(getApplicationContext());
    }

    @Override
    protected void onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver);
        super.onPause();
    }
    private boolean checkPermission(String permission) {
        return ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch(requestCode) {
            case MAKE_CALL_PERMISSION_REQUEST_CODE :
                if (grantResults.length > 0 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                   // Toast.makeText(this, "You can call the number by clicking on the button", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }
}