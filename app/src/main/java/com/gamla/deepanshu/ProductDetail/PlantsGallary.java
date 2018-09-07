package com.gamla.deepanshu.ProductDetail;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
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
import com.gamla.deepanshu.ProductList.ProductlistBean;
import com.gamla.deepanshu.Database.DatabaseHandler;
import com.gamla.deepanshu.ShoppingCart.ItemBag;
import com.gamla.deepanshu.gamla.Login;
import com.gamla.deepanshu.gamla.R;
import com.gamla.deepanshu.Function.Utility;
import com.google.android.gms.tasks.OnFailureListener;
import com.ms.square.android.expandabletextview.ExpandableTextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class PlantsGallary extends AppCompatActivity{

    private ViewPager vp_slider;
    private LinearLayout ll_dots;
    SliderPagerAdapter sliderPagerAdapter;
    ArrayList<String> slider_image_list;
    private TextView[] dots;
    int page_position = 0;
    ImageView imgWishlist;
    LinearLayout llBuy;
    ProductlistBean obj;
    TextView txtprice,txtname,txtbuyprice,txtsoldby,txtAddToCart;
    ExpandableTextView expTv1;
    FrameLayout shopingcarticon;
    TextView txtshopingcart;
    DatabaseHandler dh;
    int rowcount=0;
    RequestQueue rQueue;
    ArrayList<String> objArrylistuser=new ArrayList<>();
    private Context _context;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.plants_gallary);




        _context = this;
        rQueue = Volley.newRequestQueue(this);
        dh = new DatabaseHandler(this);

        rowcount = dh.getRowCount(DatabaseHandler.TABLE_CART);
        expTv1 = (ExpandableTextView) findViewById(R.id.expand_text_view);
        Bundle bundle = getIntent().getExtras();
        obj = (ProductlistBean) bundle.getSerializable("PLantsDetail");
        init();
        try {
            objArrylistuser = dh.getUserRecord();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        if(objArrylistuser.size()>0) {
            getwishlistOrNot();
        }
        addBottomDots(0);
        final Handler handler = new Handler();
        final Runnable update = new Runnable() {
            public void run() {
                if (page_position == slider_image_list.size()) {
                    page_position = 0;
                } else {
                    page_position = page_position + 1;
                }
                vp_slider.setCurrentItem(page_position, true);
            }
        };

        new Timer().schedule(new TimerTask() {

            @Override
            public void run() {
                handler.post(update);
            }
        }, 300, 5000);
    }

    private void init() {
        Toolbar actionbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(actionbar);
        if (null != actionbar) {
            actionbar.setNavigationIcon(R.drawable.ic_arrow_back_black_24dp);
            actionbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onBackPressed();
                }
            });
        }
        vp_slider = (ViewPager) findViewById(R.id.vp_slider);
        ll_dots = (LinearLayout) findViewById(R.id.ll_dots);
        imgWishlist = (ImageView) findViewById(R.id.wishlistimage);
        txtname = findViewById(R.id.textView3);
        txtprice = findViewById(R.id.textView4);
        txtAddToCart = findViewById(R.id.AddToCart);
        txtbuyprice = findViewById(R.id.buyprice);
        llBuy = findViewById(R.id.buy);
        txtsoldby = findViewById(R.id.soldbyproduct);
        shopingcarticon = findViewById(R.id.shopingcarticon);
        txtshopingcart = findViewById(R.id.mark);

        txtprice.setText("Price  : "+obj.get_sellingPrice()+ " Rs");
        txtname.setText("Name : "+obj.get_productName());
        txtbuyprice.setText("Buy for Rs. "+(Integer.parseInt(obj.get_sellingPrice())+20));
        txtsoldby.setText(obj.get_RegisteredDisplayName());
        expTv1.setText(obj.get_desceription());
        txtshopingcart.setText(rowcount+"");
        shopingcarticon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PlantsGallary.this,ItemBag.class);
                startActivityForResult(i,101);
                overridePendingTransition(R.anim.slide_down,R.anim.hold);
            }
        });

        imgWishlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                if(objArrylistuser.size()>0) {

                    if(view.getTag().toString().equalsIgnoreCase("0"))
                    {
                        imgWishlist.setBackground(getResources().getDrawable(R.drawable.red_heart));
                        view.setTag(1);
                        AddToWishList();
                        dh.deleteRecordFromCart(obj.get_id());
                       // Toast.makeText(PlantsGallary.this, "Item added to wishlist", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        imgWishlist.setBackground(getResources().getDrawable(R.drawable.white_heart));
                        view.setTag(0);
                        RemoveFromWishList();
                        //Toast.makeText(PlantsGallary.this, "Item remove from wishlist", Toast.LENGTH_SHORT).show();
                    }




                }
                else
                {
                    System.out.println("---------------------------------===========================");

                    startActivityForResult(new Intent(PlantsGallary.this,Login.class),102);
                    overridePendingTransition(R.anim.puul_up_from_bottom,R.anim.hold);
                }




            }
        });
        llBuy.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                DatabaseHandler dh = new DatabaseHandler(PlantsGallary.this);

                obj.set_productPurchaseQuantatity("1");
                try {
                    dh.savecartRecord(obj);
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                Intent i = new Intent(PlantsGallary.this,ItemBag.class);
                startActivityForResult(i,101);
                overridePendingTransition(R.anim.puul_up_from_bottom,R.anim.hold);
            }
        });
        txtAddToCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler dh = new DatabaseHandler(_context);

                obj.set_productPurchaseQuantatity("1");
                try {
                    dh.savecartRecord(obj);
                    Toast.makeText(_context, "Product Added to Cart", Toast.LENGTH_SHORT).show();
                    rowcount = dh.getRowCount(DatabaseHandler.TABLE_CART);
                    txtshopingcart.setText(rowcount+"");

                }
                catch (Exception e)
                {
                    //e.printStackTrace();
                    Toast.makeText(_context, "Product already added to cart", Toast.LENGTH_SHORT).show();
                }
            }
        });
        slider_image_list = new ArrayList<>();
        slider_image_list.add(obj.get_imagepath());
        sliderPagerAdapter = new SliderPagerAdapter(PlantsGallary.this, slider_image_list);
        vp_slider.setAdapter(sliderPagerAdapter);
        vp_slider.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                addBottomDots(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_list.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.parseColor("#3abf3a"));
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#33D7FF"));
    }

    private void AddToWishList()
    {
        final ProgressDialog loading = ProgressDialog.show(PlantsGallary.this, "Gamla", "Please wait...", false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.Add_WISHLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("res------------->"+response);
                        //if the server response is success
                        String res = response+"";
                        if(res.contains("false")){
                            loading.dismiss();



                        }else{

                            Toast.makeText(PlantsGallary.this, "Item Added to wishlist", Toast.LENGTH_SHORT).show();
                        }


                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(PlantsGallary.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String, String>();
                //Adding the parameters otp and username
                params.put("mobileno",objArrylistuser.get(0));
                params.put("productid",obj.get_id());
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
    private void RemoveFromWishList(){
        final ProgressDialog loading = ProgressDialog.show(PlantsGallary.this, "Gamla", "Please wait...", false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.REMOVE_WISHLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("res------------->"+response);
                        //if the server response is success
                        String res = response+"";
                        if(res.contains("false")){
                            loading.dismiss();



                        }else{

                            Toast.makeText(PlantsGallary.this, "Item Removed to wishlist", Toast.LENGTH_SHORT).show();
                        }


                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(PlantsGallary.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String, String>();
                //Adding the parameters otp and username
                params.put("mobileno",objArrylistuser.get(0));
                params.put("productid",obj.get_id());
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
    private void  getwishlistOrNot()
    {
       // final ProgressDialog loading = ProgressDialog.show(PlantsGallary.this, "Gamla", "Please wait...", false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.GET_WISHLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("res------------->"+response);
                        //if the server response is success
                        String res = response+"";
                        if(res.contains("false")){
                           // loading.dismiss();

                              imgWishlist.setTag(0);
                              imgWishlist.setBackground(getResources().getDrawable(R.drawable.white_heart));

                        }else{
                            imgWishlist.setTag(1);
                            imgWishlist.setBackground(getResources().getDrawable(R.drawable.red_heart));
                           // Toast.makeText(PlantsGallary.this, "Item Removed to wishlist", Toast.LENGTH_SHORT).show();
                        }


                       // loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(PlantsGallary.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String, String>();
                //Adding the parameters otp and username
                params.put("mobileno",objArrylistuser.get(0));
                params.put("productid",obj.get_id());
                return params;
            }

        };

        //Adding the request to the queue
        rQueue.add(stringRequest);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==101)
        {
            rowcount = dh.getRowCount(DatabaseHandler.TABLE_CART);
            txtshopingcart.setText(rowcount+"");
        }
        else if(requestCode==102)
        {
            try {
                objArrylistuser = dh.getUserRecord();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}