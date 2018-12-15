package com.gamla.deepanshu.ShoppingCart;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.circulardialog.CDialog;
import com.example.circulardialog.extras.CDConstants;
import com.gamla.deepanshu.Function.CustomAlert;
import com.gamla.deepanshu.ProductList.ProductlistBean;
import com.gamla.deepanshu.DeliveryAdress.AdressBean;
import com.gamla.deepanshu.Database.DatabaseHandler;
import com.gamla.deepanshu.DeliveryAdress.DeliveryAdress;
import com.gamla.deepanshu.gamla.Login;
import com.gamla.deepanshu.MyOrder.MyOrder;

import com.gamla.deepanshu.Function.Utility;
import com.gamla.deepanshu.gamla.R;
import com.gamla.deepanshu.payment.PaymentActivity;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;
import com.sdsmdg.tastytoast.TastyToast;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.LimitExceededListener;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;


public class ItemBag extends AppCompatActivity implements LimitExceededListener,ValueChangedListener,PaymentResultListener {
    private TextView txtprice;
    int price = 49;
    ProgressDialog loading;
    ArrayList<ProductlistBean> objArrayList = new ArrayList<>();
    ArrayList<String> objUserArrayList = new ArrayList<>();
    RecyclerView recList;
    TextView txtLogintoproced;
    LinearLayout llLogin;
    String address = "Select Delivery Address";
    String mobilenumber="";
    String fullname = "";
    RequestQueue rQueue;
    String message,messageforseller;
    DatabaseHandler dh;
    ArrayList<AdressBean> objdefaultadress = new ArrayList<>();
    String items [] = {"Cash on Delivery","Netbanking/CreditCard/DebitCard/Wallet"};
     ACProgressFlower dialogOrder;
    private static final String TAG = PaymentActivity.class.getSimpleName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.item_bag_main);
        Checkout.preload(getApplicationContext());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        rQueue = Volley.newRequestQueue(ItemBag.this);
        dh = new DatabaseHandler(ItemBag.this);
        new FetchRecordFromDatabase().execute();

        txtLogintoproced = findViewById(R.id.logintoproceed);
        llLogin = findViewById(R.id.loginItemBag);
        recList = (RecyclerView) findViewById(R.id.itembaglist);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        objdefaultadress = dh.GetUserAdress("Default");
        if(objdefaultadress.size()>0)
        {
            AdressBean objbean = objdefaultadress.get(0);
            address = objbean.get_adress1()+", "+objbean.get_adress2()+", "+objbean.get_city()+", "+objbean.get_state()+" - "+objbean.get_pincode();
            mobilenumber = objbean.get_mobilenumber();
        }

        llLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(txtLogintoproced.getText().toString().equalsIgnoreCase(getResources().getString(R.string.logintoproceed)))
                {
                    startActivityForResult(new Intent(ItemBag.this,Login.class),101);
                    overridePendingTransition(R.anim.puul_up_from_bottom,R.anim.hold);
                }
                else if(txtLogintoproced.getText().toString().equalsIgnoreCase("Checkout"))
                {
                    //==============check current item Stock Status================================//
                        CheckItemStockStatusOnServer();
                    //=============================================================================//

                }
                else
                {
                    startActivityForResult(new Intent(ItemBag.this,DeliveryAdress.class),102);
                    overridePendingTransition(R.anim.puul_up_from_bottom,R.anim.hold);
                }
            }
        });

    }
    private void CheckItemStockStatusOnServer()
    {
        final JSONArray objarray = new JSONArray();
        for(int i=0;i<objArrayList.size();i++)
        {
            JSONObject jsonObject = new JSONObject();
            ProductlistBean objBean = objArrayList.get(i);
            try {
                jsonObject.put("ProductId", objBean.get_id());
                objarray.put(jsonObject);
            }
            catch(Exception e)
            {
                e.printStackTrace();
            }
        }
        if(objarray.length()>0)
        {
            String totalPrice = dh.getToatalAmmount();
            View child = getLayoutInflater().inflate(R.layout.item_bag_price_summary, null);
            TextView txtTotalprice = child.findViewById(R.id.totalprice);
            final int totalprice = Integer.parseInt(totalPrice)*100;
           // final ProgressDialog loading = ProgressDialog.show(ItemBag.this, "Checkout", "Please wait while we check product availabilty", false,false);
            final ACProgressFlower dialog = new ACProgressFlower.Builder(this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)

                    .fadeColor(Color.DKGRAY).build();
            dialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.CHECK_ORDER_STAUS,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //if the server response is success
                            dialog.dismiss();
                            System.out.println("response---------->"+response);
                            if(!response.contains("Out Stock")&&!response.contains("Non Live")){
                               /*
                          You need to pass current activity in order to let Razorpay create CheckoutActivity
                         */

                                new AlertDialog.Builder(ItemBag.this)
                                        .setSingleChoiceItems(items, 0, null)
                                        .setTitle("Select Payment Mode")
                                        .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                            public void onClick(DialogInterface dialog, int whichButton) {
                                                dialog.dismiss();
                                                int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                                                // Do something useful withe the position of the selected radio button
                                                if(selectedPosition==0)
                                                {
                                                    orderPlaced("cod");
                                                }
                                                else{

                                                    final Activity activity = ItemBag.this;
                                                    final Checkout co = new Checkout();
                                                    try {

                                                        JSONObject options = new JSONObject();
                                                        options.put("name", "GamlaHub");
                                                        options.put("description", "");
                                                        //You can omit the image option to fetch the image from dashboard
                                                        options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                                                        options.put("currency", "INR");


                                                        options.put("amount", totalprice+"");//paise

                                                        JSONObject preFill = new JSONObject();
                                                        preFill.put("email", objUserArrayList.get(2));
                                                        preFill.put("contact", mobilenumber);

                                                        options.put("prefill", preFill);

                                                        co.open(activity, options);
                                                    } catch (Exception e) {
                                                        Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                                        e.printStackTrace();
                                                    }
                                                }
                                            }
                                        })
                                        .show();


                               /* final Activity activity = ItemBag.this;
                                final Checkout co = new Checkout();
                                try {
                                    JSONObject options = new JSONObject();
                                    options.put("name", "GamlaHub");
                                    options.put("description", "");
                                    //You can omit the image option to fetch the image from dashboard
                                    options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
                                    options.put("currency", "INR");


                                    options.put("amount", totalprice+"");//paise

                                    JSONObject preFill = new JSONObject();
                                    preFill.put("email", "xyz@xyz.com");
                                    preFill.put("contact", mobilenumber);

                                    options.put("prefill", preFill);

                                    co.open(activity, options);
                                } catch (Exception e) {
                                    Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                    e.printStackTrace();
                                }
*/

                          /*  new CDialog(ItemBag.this).createAlert(response,
                                    CDConstants.SUCCESS,   // Type of dialog
                                    CDConstants.LARGE)    //  size of dialog
                                    .setAnimation(CDConstants.SCALE_FROM_BOTTOM_TO_TOP)     //  Animation for enter/exit
                                    .setDuration(2000)   // in milliseconds
                                    .setTextSize(CDConstants.LARGE_TEXT_SIZE)  // CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                                    .show();
*/
                            }else{
                                try {
                                    JSONArray objArray = new JSONArray(response);
                                    dh.UpdateProductStatus(objArray);
                                    Snackbar.make(findViewById(android.R.id.content), " Remove 'out of stock product' from your shopping cart list ", Snackbar.LENGTH_LONG)
                                            .setAction("Action", null).show();
                                    new FetchRecordFromDatabase().execute();


                                }
                                catch(Exception e)
                                {
                                    e.printStackTrace();
                                }
/*
                                new CDialog(ItemBag.this).createAlert(response,
                                        CDConstants.SUCCESS,   // Type of dialog
                                        CDConstants.LARGE)    //  size of dialog
                                        .setAnimation(CDConstants.SCALE_FROM_BOTTOM_TO_TOP)     //  Animation for enter/exit
                                        .setDuration(2000)   // in milliseconds
                                        .setTextSize(CDConstants.LARGE_TEXT_SIZE)  // CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                                        .show();*/
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismiss();
                            Toast.makeText(ItemBag.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {


                    Map<String,String> params = new HashMap<String, String>();
                    //Adding the parameters otp and username
                    params.put("data", objarray+"");

                    return params;
                }
            };

            //Adding the request to the queue
            rQueue.add(stringRequest);
        }

    }
    private void PlacedOrder(final JSONArray objOrderArray)
    {


        System.out.println("======================order placed============================");
        //final ProgressDialog loading = ProgressDialog.show(ItemBag.this, "Authenticating", "Please wait while we placed order", false,false);
        final ACProgressFlower dialogOrder = new ACProgressFlower.Builder(this)
                .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                .themeColor(Color.WHITE)

                .fadeColor(Color.DKGRAY).build();
        dialogOrder.show();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.ORDER_PLACED,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //if the server response is success
                       // loading.dismiss();
                        dialogOrder.dismiss();
                        if(response.equalsIgnoreCase("Item Placed Sucessfully")){
                           // Toast.makeText(ItemBag.this, response, Toast.LENGTH_LONG).show();
                            TastyToast.makeText(getApplicationContext(),  "Item Placed Sucessfully", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);

                          /*  new CDialog(ItemBag.this).createAlert(response,
                                    CDConstants.SUCCESS,   // Type of dialog
                                    CDConstants.LARGE)    //  size of dialog
                                    .setAnimation(CDConstants.SCALE_FROM_BOTTOM_TO_TOP)     //  Animation for enter/exit
                                    .setDuration(2000)   // in milliseconds
                                    .setTextSize(CDConstants.LARGE_TEXT_SIZE)  // CDConstants.LARGE_TEXT_SIZE, CDConstants.NORMAL_TEXT_SIZE
                                    .show();
*/
                            dh.deleteTable(DatabaseHandler.TABLE_CART);
                            finish();
                            Intent i = new Intent(ItemBag.this,MyOrder.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);

                        }else{
                            CustomAlert customAlert = new CustomAlert(ItemBag.this);
                            customAlert.ShowDialog("Information","Your order was not placed for some reason,we will refund your money (if you pay through online) with in 5 working days", false, "false");
                            //Toast.makeText(ItemBag.this, response, Toast.LENGTH_LONG).show();

                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(ItemBag.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String,String> params = new HashMap<String, String>();
                //Adding the parameters otp and username
                params.put("data", objOrderArray+"");

                return params;
            }
        };

        //Adding the request to the queue
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                Utility.TimedOutTimeInMiliSec,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        rQueue.add(stringRequest);
    }
    private class FetchRecordFromDatabase extends AsyncTask<Void,Void,String>
    {
         ACProgressFlower dialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           // loading = ProgressDialog.show(ItemBag.this, "Gamla", "Please wait...", false,false);
             dialog = new ACProgressFlower.Builder(ItemBag.this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)

                    .fadeColor(Color.DKGRAY).build();
            dialog.show();
        }

        @Override
        protected String doInBackground(Void... voids) {
            DatabaseHandler dh = new DatabaseHandler(ItemBag.this);
            objArrayList = dh.getCartRecord();
            objUserArrayList = dh.getUserRecord();



            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            getSupportActionBar().setTitle(objArrayList.size()+" item in your bag");
            ItemBagAdapter ca = new ItemBagAdapter(ItemBag.this,objArrayList,getSupportActionBar(),address,mobilenumber);
            // ca.setonClickListner(mClicklistner);
            recList.setAdapter(ca);
            dialog.dismiss();


             if(objUserArrayList.size()>0)
            {

                if(objArrayList.size()>0&&!address.equalsIgnoreCase("Select Delivery Address"))
                {
                    txtLogintoproced.setText("Checkout");
                }
                else
                {
                    txtLogintoproced.setText(getResources().getString(R.string.deliveryadress));
                }


            }
            else
            {
                txtLogintoproced.setText(getResources().getString(R.string.logintoproceed));
            }
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        try {
            if (!dialogOrder.isShowing()) {
                finish();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            finish();
        }
    }

    @Override
    public void limitExceeded(int limit, int exceededValue) {
        String message = String.format("You can purchase only %d product", limit);
       // Log.v(this.getClass().getSimpleName(), message);
        //Toast.makeText(ItemBag.this, message, Toast.LENGTH_SHORT).show();
        TastyToast.makeText(getApplicationContext(),  message, TastyToast.LENGTH_LONG, TastyToast.INFO);
    }

    @Override
    public void valueChanged(int value, ActionEnum action) {
        price = 49*value;
        txtprice.setText(price+ " "+"Rs");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==101)
        {
            new FetchRecordFromDatabase().execute();
        }
        else if(requestCode==102)
        {
            try {
                address = data.getStringExtra("Adress");
                mobilenumber = data.getStringExtra("mobile");
                fullname = data.getStringExtra("fullname");
                new FetchRecordFromDatabase().execute();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

           //Toast.makeText(ItemBag.this, adress, Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * The name of the function has to be
     * onPaymentSuccess
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")
    @Override
    public void onPaymentSuccess(String razorpayPaymentID) {
        orderPlaced(razorpayPaymentID);
    }

    /**
     * The name of the function has to be
     * onPaymentError
     * Wrap your code in try catch, as shown, to ensure that this method runs correctly
     */
    @SuppressWarnings("unused")



    public void orderPlaced(String paymentid)
    {
        try {
            // Toast.makeText(this, "Payment Successful: " + razorpayPaymentID, Toast.LENGTH_SHORT).show();


            objArrayList = dh.getCartRecord();
            JSONArray objarray = new JSONArray();
            for(int i = 0;i<objArrayList.size();i++) {
                ProductlistBean objBean = objArrayList.get(i);
                long time = System.currentTimeMillis();
                SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
                try {
                    Calendar cal = Calendar.getInstance();
                    cal.setTimeInMillis(time);
                    String date  = sdf.format(cal.getTime());
                    objBean.set_timeSpanForSearch(date);

                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }

                String orderid = "OD"+time+i;
                String finalplacedamount = (Integer.parseInt(objBean.get_sellingPrice()))+"";
                objBean.set_orderId(orderid);
                objBean.set_status(Utility.ORDERPLACED);
                objBean.set_buyerMobile(objUserArrayList.get(0));
                objBean.set_timestamp(time+"");
                message = "Order Placed: Your Order for "+objBean.get_productName()+" with Order ID "+orderid+" \n amounting to Rs."+finalplacedamount+" has been recived. We will send you an update when your order is packed/shipped.";
                messageforseller="You have Recived new order for "+objBean.get_productName()+" with Order Id "+orderid+" \n amounting to Rs. "+finalplacedamount+". Please ship Product asap.";
                JSONObject obj = new JSONObject();
                try {
                    obj.put("OrderId", objBean.get_orderId());
                    obj.put("ImagePath",objBean.get_imagepath());
                    obj.put("CatagoryName",objBean.get_productCategory());
                    obj.put("MerchantSkuId",objBean.get_skuid());
                    obj.put("MRP",objBean.get_mrp());
                    obj.put("SellingPrice",objBean.get_sellingPrice());//selling price is final amount
                    obj.put("Weight",objBean.get_weight());
                    obj.put("Shipindays",objBean.get_shipinDays());
                    obj.put("Description",objBean.get_desceription());
                    obj.put("SellerEmail","");//put seller email id;
                    obj.put("SellerMobile",objBean.get_mobilenumber());
                    obj.put("ProductName",objBean.get_productName());
                    obj.put("ImageName",objBean.get_imageName());
                    obj.put("Quantatity",objBean.get_productPurchaseQuantatity());
                    obj.put("Status",objBean.get_status());
                    obj.put("Timestamp",objBean.get_timestamp());
                    obj.put("BuyerMobile",objBean.get_buyerMobile());
                    obj.put("ProductID",objBean.get_id());
                    obj.put("DeliveryAdress",address);
                    obj.put("UserName",fullname);
                    obj.put("UserAdressMobileNumber",mobilenumber);
                    obj.put("TimestampforSearch",objBean.get_timeSpanForSearch());
                    obj.put("message",message);
                    obj.put("messageForSeller",messageforseller);
                    obj.put("transactionId",paymentid);
                    obj.put("ExpectedPayout",objBean.get_expectedPayout());
                    obj.put("Height",objBean.get_height());
                    obj.put("Width",objBean.get_width());
                  //  obj.put("PaymentType",objBean.get_width());
                    System.out.println("test------------------------------->>>>>>>>>"+objBean.get_length());
                    obj.put("Length",objBean.get_length());

                    objarray.put(obj);

                }
                catch (Exception e)
                {

                }




            }
            PlacedOrder(objarray);



        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentSuccess", e);
        }
    }
    @Override
    public void onPaymentError(int code, String response) {
        try {
            Toast.makeText(this, "Payment failed: " + code + " " + response, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Log.e(TAG, "Exception in onPaymentError", e);
        }
    }


}
