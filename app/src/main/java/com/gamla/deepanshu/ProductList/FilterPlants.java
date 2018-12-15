package com.gamla.deepanshu.ProductList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
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
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.gamla.deepanshu.Database.DatabaseHandler;
import com.gamla.deepanshu.Function.Utility;
import com.gamla.deepanshu.ProductDetail.PlantsGallary;
import com.gamla.deepanshu.gamla.R;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class FilterPlants extends AppCompatActivity {

    private RelativeLayout rlCategoty,rlProductType,rlcolor;
    ArrayList<String> objArraylist;
    String catgaory;
    RecyclerView rcList;
    DatabaseHandler dh;
    LinearLayout llDone,llResetAll;
    RequestQueue rQueue;
    String minprice;
    String maxprice;
    CrystalRangeSeekbar rangeSeekbar;
    TextView tvMin,tvMax;
    ArrayList<String> objArraylistprice = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_product);
        rQueue = Volley.newRequestQueue(this);
        llDone = findViewById(R.id.filterdone);
        llResetAll = findViewById(R.id.resetall);
        dh = new DatabaseHandler(this);
        objArraylist = new ArrayList<>();
        objArraylist = dh.getSelectedProductTypeRecord();
        rcList = findViewById(R.id.filterproduct);
        LinearLayoutManager llpromotionlist = new LinearLayoutManager(this);
        llpromotionlist.setOrientation(LinearLayoutManager.HORIZONTAL);
        rcList.setLayoutManager(llpromotionlist);
        SelectedFilterProductTypeListAdapter adapter = new SelectedFilterProductTypeListAdapter(FilterPlants.this,objArraylist);

        rcList.setAdapter(adapter);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Filter By");
        catgaory = getIntent().getStringExtra("category");
        System.out.println("filter catagory---------------------->"+catgaory);
       // rlCategoty = findViewById(R.id.catogry);
       // rlcolor = findViewById(R.id.color);
        rlProductType = findViewById(R.id.producttype);
        rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbar1);
        tvMin = (TextView) findViewById(R.id.min);
        tvMax = (TextView) findViewById(R.id.max);
        objArraylistprice = dh.GetFilterPriceValue();
        System.out.println("my size------->"+objArraylistprice.size());
        if(objArraylistprice.size()>0) {
            rangeSeekbar.setMinValue(Float.parseFloat(objArraylistprice.get(0)));
            rangeSeekbar.setMaxValue(Float.parseFloat(objArraylistprice.get(1)));
            System.out.println("my min price------->"+objArraylistprice.get(2)+"   my macprice--------------->"+objArraylistprice.get(3));
            tvMin.setText(objArraylistprice.get(2));
            tvMax.setText(objArraylistprice.get(3));

        }
        else
        {
            FetchMinAndMaxPrice();
        }

         // get min and max text view

        // set listener

        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.valueOf(minValue));
                tvMax.setText(String.valueOf(maxValue));
              //  dh.UpdateSelectedMinAndMaxValue(String.valueOf(minValue),String.valueOf(maxValue));
            }
        });
        // set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
                dh.UpdateSelectedMinAndMaxValue(String.valueOf(minValue),String.valueOf(maxValue));
            }
        });
        rlProductType.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilterPlants.this,FilterByProduct.class);
                i.putExtra("headername","Product Type");
                i.putExtra("catagory",catgaory);
                startActivityForResult(i,101);
                overridePendingTransition(R.anim.left_to_right,R.anim.hold);

            }
        });
        llDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("minvalue",tvMin.getText().toString());
                i.putExtra("maxvalue",tvMax.getText().toString());
                setResult(Activity.RESULT_OK,i);
                finish();
                overridePendingTransition(R.anim.hold, R.anim.push_out_to_bottom);
            }
        });
        llResetAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dh.deleteTable(DatabaseHandler.TABLE_FILTER_PRODUCT_TYPE);
                Intent i = new Intent();
                i.putExtra("minvalue",minprice);
                i.putExtra("maxvalue",maxprice);
                setResult(Activity.RESULT_OK,i);
                finish();
                overridePendingTransition(R.anim.hold, R.anim.push_out_to_bottom);
            }
        });
       /* rlcolor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilterPlants.this,FilterByProduct.class);
                i.putExtra("headername","Color");
                startActivityForResult(i,101);
                overridePendingTransition(R.anim.left_to_right,R.anim.hold);

            }
        });
        rlCategoty.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilterPlants.this,FilterByProduct.class);
                i.putExtra("headername","Category");
                startActivityForResult(i,101);
                overridePendingTransition(R.anim.left_to_right,R.anim.hold);


            }
        });*/


    }

    private void FetchMinAndMaxPrice()
    {
        {
           // final ProgressDialog loading = ProgressDialog.show(FilterPlants.this, "Gamla", "Please wait...", false,false);
            final ACProgressFlower dialog = new ACProgressFlower.Builder(FilterPlants.this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)

                    .fadeColor(Color.DKGRAY).build();
            dialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.FETCH_MIN_AND_MAX_PRICE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("res------------->"+response);
                            String res = response+"";
                            if(res.contains("false")){
                                dialog.dismiss();
                                //Toast.makeText(FilterPlants.this, "Record not avilable", Toast.LENGTH_SHORT).show();

                            }else{
                                try {
                                    String[] pricearray = res.split(",");
                                    minprice = pricearray[0];
                                    maxprice = pricearray[1];
                                    rangeSeekbar.setMinValue(Float.parseFloat(minprice));
                                    rangeSeekbar.setMaxValue(Float.parseFloat(maxprice));

                                    tvMin.setText(minprice);
                                    tvMax.setText(maxprice);
                                    dh.SaveFilterPriceRecord(minprice, maxprice, minprice, maxprice);
                                }
                                catch(Exception e)
                                {
                                   e.printStackTrace();
                                }
                            }
                            dialog.dismiss();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                           // Toast.makeText(FilterPlants.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            TastyToast.makeText(getApplicationContext(),  error.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    //Adding the parameters otp and username

                    params.put("catagory",catgaory);
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
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        overridePendingTransition(R.anim.hold, R.anim.push_out_to_bottom);
        return true;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==101)
        {
            try{
                objArraylist = new ArrayList<>();
                objArraylist = dh.getSelectedProductTypeRecord();

               /* String producttype = data.getStringExtra("filtervalue");
                String [] array = producttype.split(",");
                for(int i=0;i<array.length;i++)
                {
                    objArraylist.add(array[i]);
                }*/

                SelectedFilterProductTypeListAdapter adapter = new SelectedFilterProductTypeListAdapter(FilterPlants.this,objArraylist);

                rcList.setAdapter(adapter);


            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }
}
