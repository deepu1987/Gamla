package com.gamla.deepanshu.Function;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.telephony.TelephonyManager;
import android.widget.BaseAdapter;

import java.net.NetworkInterface;
import java.util.Collections;
import java.util.List;

/**
 * Created by Deepanshu on 21-03-2018.
 */

public class Utility {

    public static final String BaseURl = "http://10.0.2.2:8080/GamlaHub/rest/GamlaService/";
   // public static final String BaseURl = "http://172.25.141.199:8081/GamlaHub/rest/GamlaService/";


   // public static final String BaseURl = "http://www.gamlahub.com/GamlaHub/rest/GamlaService/";
    public static final String PRODUCTLIST_CATAGORYWISE_URL = BaseURl+"ProductListCatagoryWise";
    public static final String CONFIRM_URL = BaseURl+"confirmGamlaUser";
    public static final String LOGIN_URL = BaseURl+"LoginGamla";
    public static final String Add_WISHLIST = BaseURl+"SaveWishList";
    public static final String WISHLIST_URL = BaseURl+"GetWishList";
    public static final String REMOVE_WISHLIST = BaseURl+"RemoveFromWishList";
    public static final String GET_WISHLIST = BaseURl+"GetwishListOrNot";
    public static final String ORDER_PLACED = BaseURl+"OrderPlaced";
    public static final String ORDER_LIST = BaseURl+"OrderList";
    public static final String FORGOT_PASSWORD = BaseURl+"ForgotPassword";
    public static final String PREMIUM_PROMOTION_PRODUCT = BaseURl+"PremiumPromotionProduct";
    public static final String REMOVE_FROM_WISHLIST = BaseURl+"RemoveFromWishList";
    public static final String REGISTRATIONIDFORFCM = BaseURl+"RegistrationForFCM";
    public static final String PRODUCTTYPE = BaseURl+"ProductType";
    public static final String FETCH_MIN_AND_MAX_PRICE = BaseURl+"GetMinAndMaxPrice";
    public static final String CHECK_ORDER_STAUS = BaseURl+"CheckProductStatus";
    public static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 123;
    public static final String ORDERPLACED = "Order Placed";
    public static final int TimedOutTimeInMiliSec = 60000;
    public static boolean checkPermission(final Context context)
    {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if(currentAPIVersion>= Build.VERSION_CODES.M)
        {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.READ_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(context);
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("External storage permission is necessary");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions((Activity) context, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

}

