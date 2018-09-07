package com.gamla.deepanshu.WishList;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gamla.deepanshu.Database.DatabaseHandler;
import com.gamla.deepanshu.ProductDetail.PlantsGallary;
import com.gamla.deepanshu.ProductList.ProductlistBean;
import com.gamla.deepanshu.ProductList.onPlantsItemClickListner;
import com.gamla.deepanshu.gamla.R;
import com.gamla.deepanshu.Function.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class WishList extends AppCompatActivity implements onPlantsItemClickListner {

    RecyclerView recList;
    RequestQueue rQueue;
    DatabaseHandler dh;
    ArrayList<String> objUserArraylist;
    ArrayList<ProductlistBean> plantArrayList;
    ProductlistBean objBean;
    private onPlantsItemClickListner mClicklistner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wish_list);
        mClicklistner = this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        dh = new DatabaseHandler(this);
        objUserArraylist = dh.getUserRecord();
        rQueue = Volley.newRequestQueue(WishList.this);
        recList = findViewById(R.id.wishlist);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        FetchRecordServer();

    }
    private void FetchRecordServer()
    {
        plantArrayList = new ArrayList<>();
        final ProgressDialog loading = ProgressDialog.show(WishList.this, "Gamla", "Please wait...", false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.WISHLIST_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //if the server response is success

                            System.out.println("res------------->"+response);
                            //if the server response is success
                            String res = response+"";
                            if(res.contains("false")){
                                loading.dismiss();
                                Toast.makeText(WishList.this, "Record not avilable", Toast.LENGTH_SHORT).show();


                            }else{
                                try {
                                    JSONArray objArray = new JSONArray(res);
                                    JSONObject obj;
                                    for(int i =0;i<objArray.length();i++)
                                    {
                                        obj = objArray.getJSONObject(i);
                                        objBean = new ProductlistBean();
                                        objBean.set_productName(obj.getString("ProductName"));
                                        objBean.set_quantatity(obj.getString("Quantatity"));
                                        objBean.set_weight(obj.getString("weight"));
                                        objBean.set_skuid(obj.getString("MerchantSkuId"));
                                        objBean.set_shipinDays(obj.getString("shipindays"));
                                        objBean.set_sellingPrice(obj.getString("SellingPrice"));
                                        objBean.set_productCategory(obj.getString("Categoryname"));
                                        objBean.set_mrp(obj.getString("MRP"));
                                        objBean.set_id(obj.getString("ProuctId"));
                                        objBean.set_desceription(obj.getString("description"));
                                        objBean.set_imageName(obj.getString("ImageName"));
                                        objBean.set_imagepath(obj.getString("ImagePath"));
                                        objBean.set_mobilenumber(obj.getString("mobileno"));
                                        objBean.set_status(obj.getString("Status"));
                                        objBean.set_buisnessid(obj.getString("BuisnessInformationID"));
                                        objBean.set_buisnessType(obj.getString("BuisnessType"));
                                        objBean.set_city(obj.getString("City"));
                                        objBean.set_displayName(obj.getString("DisplayName"));
                                        objBean.set_pincode(obj.getString("PinCode"));
                                        objBean.set_RegisteredDisplayAdress(obj.getString("RegisteredBuisnessAdress"));
                                        objBean.set_RegisteredDisplayName(obj.getString("RegisteredBuisnessName"));
                                        objBean.set_state(obj.getString("State"));
                                        plantArrayList.add(objBean);


                                    }

                                }
                                catch (Exception e)
                                {

                                }



                            }
                            getSupportActionBar().setTitle(plantArrayList.size()+" item in your wishlist");
                            WishListAdapter ca = new WishListAdapter(plantArrayList,WishList.this,getSupportActionBar());
                            ca.setonClickListner(mClicklistner);
                            recList.setAdapter(ca);

                            loading.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(WishList.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                //Adding the parameters otp and username
                params.put("mobile", objUserArraylist.get(0));
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

    @Override
    public void onPlantsItemClick(int position,int gp,int newprice) {
        ProductlistBean obj;
        obj = plantArrayList.get(position);
        Intent i = new Intent(WishList.this,PlantsGallary.class);
        Bundle bundle = new Bundle();

        bundle.putSerializable("PLantsDetail",obj);
        i.putExtras(bundle);
        startActivityForResult(i,101);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==101)
        {
            FetchRecordServer();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}
