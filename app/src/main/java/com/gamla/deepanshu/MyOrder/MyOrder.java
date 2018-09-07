package com.gamla.deepanshu.MyOrder;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
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
import com.gamla.deepanshu.ProductList.ProductlistBean;
import com.gamla.deepanshu.ProductList.onPlantsItemClickListner;
import com.gamla.deepanshu.gamla.R;
import com.gamla.deepanshu.Function.Utility;
import com.gamla.deepanshu.home.Home;
import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MyOrder extends AppCompatActivity implements onPlantsItemClickListner {
    RequestQueue rQueue;
    DatabaseHandler dh;
    ArrayList<String> objArrayListUser;
    ArrayList<ProductlistBean> objArrayList;
    ProductlistBean objBean;
    RecyclerView recList;
    onPlantsItemClickListner mClicklistner;
    LinearLayoutManager llm;
    int limitstart =0;
    int limitend = 15;
    int rowcount;
    String searchqury="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        rQueue = Volley.newRequestQueue(MyOrder.this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("My Orders");
        objArrayList = new ArrayList<>();
        mClicklistner = this;
        dh = new DatabaseHandler(this);
        objArrayListUser = dh.getUserRecord();
        recList = (RecyclerView) findViewById(R.id.orderlist);
        recList.setHasFixedSize(true);
        llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.addOnScrollListener(createInfiniteScrollListener());
        FetchOrderListFromServer(limitstart,limitend);

    }

    private void FetchOrderListFromServer(final int limitstart1, final int limitend1)
    {

        final ProgressDialog loading = ProgressDialog.show(MyOrder.this, "My Order", "Please wait...", false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.ORDER_LIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //if the server response is success

                        if(response.contains("Record Not found"))
                        {
                            loading.dismiss();
                            Toast.makeText(MyOrder.this, "Record Not found", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            try {
                                JSONArray objJsonArray = new JSONArray(response);
                                for(int i = 0;i<objJsonArray.length();i++)
                                {
                                    JSONObject obj = objJsonArray.getJSONObject(i);
                                    objBean = new ProductlistBean();
                                    objBean.set_orderId(obj.getString("OrderId"));
                                    objBean.set_imagepath(obj.getString("ImagePath"));
                                    objBean.set_productCategory(obj.getString("CatagoryName"));
                                    objBean.set_skuid(obj.getString("MerchantSkuId"));
                                    objBean.set_mrp(obj.getString("MRP"));
                                    objBean.set_sellingPrice(obj.getString("SellingPrice"));
                                    objBean.set_weight(obj.getString("Weight"));
                                    objBean.set_shipinDays(obj.getString("Shipindays"));
                                    objBean.set_desceription(obj.getString("Description"));
                                    objBean.set_mobilenumber(obj.getString("SellerMobile"));
                                    objBean.set_productName(obj.getString("ProductName"));
                                    objBean.set_imageName(obj.getString("ImageName"));
                                    objBean.set_productPurchaseQuantatity(obj.getString("Quantatity"));
                                    objBean.set_orderStatus(obj.getString("Status"));
                                    objBean.set_timestamp(obj.getString("Timestamp"));
                                    objBean.set_buyerMobile(obj.getString("BuyerMobile"));
                                    objBean.set_id(obj.getString("ProductID"));
                                    objBean.set_deliveryAdress(obj.getString("DeliveryAdress"));
                                    objBean.set_UserMobileNumber(obj.getString("UserAdressMobileNumber"));
                                    objBean.set_fullname(obj.getString("UserName"));
                                    rowcount = Integer.parseInt(obj.getString("rowcount"));
                                    objArrayList.add(objBean);
                                }

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                                loading.dismiss();
                            }
                        }
                        limitstart = limitstart+15;

                           MyOrderAdapter ca = new MyOrderAdapter(objArrayList, MyOrder.this);
                           ca.setonClickListner(mClicklistner);
                           recList.setAdapter(ca);


                        loading.dismiss();

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(MyOrder.this, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {


                Map<String,String> params = new HashMap<String, String>();
                //Adding the parameters otp and username
                params.put("mobilenumber", objArrayListUser.get(0));
                params.put("flag","Buyer");
                params.put("limitstart",limitstart1+"");
                params.put("limitend",limitend1+"");
                params.put("searchquery",searchqury);
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
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
    private InfiniteScrollListener createInfiniteScrollListener() {
        return new InfiniteScrollListener(15, llm) {

            @Override public void onScrolledToEnd(final int firstVisibleItemPosition) {
                System.out.println("firdtvisibleitem------------>"+firstVisibleItemPosition);
                // load your items here
                // logic of loading items will be different depending on your specific use case

                // when new items are loaded, combine old and new items, pass them to your adapter
                // and call refreshView(...) method from InfiniteScrollListener class to refresh RecyclerView
                if(limitstart<rowcount) {
                    FetchOrderListFromServer(limitstart, limitend);
                    refreshView(recList, null, firstVisibleItemPosition);
                }
            }
        };
    }
    @Override
    public void onBackPressed() {
        finish();
        startActivity(new Intent(MyOrder.this,Home.class));


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.search, menu);
        return true;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.searchorder:

                Intent srachIntent = new Intent(MyOrder.this,SearchOrder.class);
              //  srachIntent.putExtra("status",statusarray[currentitem]);
                startActivityForResult(srachIntent,102);
                overridePendingTransition(R.anim.push_down_in, R.anim.hold);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(requestCode==102)
        {
            searchqury = data.getStringExtra("Searchquery");
            System.out.println("search query------------->"+searchqury);
            objArrayList = new ArrayList<>();
            FetchOrderListFromServer(0,15);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onPlantsItemClick(int position,int gp,int newprice) {
        // gp for home page recyclerview
        ProductlistBean obj = objArrayList.get(position);
        Bundle bundle = new Bundle();
        bundle.putSerializable("itemdetail",obj);
        startActivityForResult(new Intent(MyOrder.this,ItemDetail.class).putExtras(bundle),101);
        overridePendingTransition(R.anim.left_to_right,R.anim.hold);

    }
}
