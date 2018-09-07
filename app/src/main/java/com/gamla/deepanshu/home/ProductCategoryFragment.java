package com.gamla.deepanshu.home;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.gamla.deepanshu.ProductList.ProductListFragment;
import com.gamla.deepanshu.ProductDetail.PlantsGallary;
import com.gamla.deepanshu.ProductList.ProductlistBean;
import com.gamla.deepanshu.gamla.R;
import com.gamla.deepanshu.Function.Utility;
import com.gamla.deepanshu.ProductList.onPlantsItemClickListner;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

public class ProductCategoryFragment extends Fragment implements OnCategoryItemClickListner,OnGoldItemClickListner,onPlantsItemClickListner {


    private OnCategoryItemClickListner mClicklistner;
    private  OnGoldItemClickListner mGoldclicklistner;
    private onPlantsItemClickListner mPlantsclickListner;
    ArrayList<PromotionProductBean> slider_image_list;
    ArrayList<PromotionProductBean> promotion_image_list;
    ArrayList<ProductCatagoryListBean2> catagoryarraylist;
    SliderTopPromotionAdapter sliderPagerAdapter;
    private ViewPager vp_slider;
    private TextView[] dots;
    private LinearLayout ll_dots;
    int page_position = 0;
    LinearLayout promotionrootLayout;
    private static Context _context;
    RequestQueue rQueue;
    RecyclerView rccategoryList,rcpromotionList;
    ProductlistBean objBean;
    ArrayList<ProductlistBean> plantArrayList;
    private AdView mAdView;

    ArrayList<ArrayList<ProductlistBean>> finalplantArrayList;
    public ProductCategoryFragment() {


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.product_category_fragment, container, false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Home");
        _context = getActivity();
        // coment new new

     /*   mAdView = (AdView) v.findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);*/

        rQueue = Volley.newRequestQueue(_context);

        mClicklistner = this;
        mGoldclicklistner = this;
        mPlantsclickListner = this;
        rccategoryList = v.findViewById(R.id.shopbycategory);
        rcpromotionList = v.findViewById(R.id.promotionslider);

        vp_slider =  v.findViewById(R.id.vp_top_slider);
        ll_dots =  v.findViewById(R.id.ll_top_dots);
        initPremuiumSlider();

        initCategory();

        initGoldSlider();

        promotionrootLayout = v.findViewById(R.id.promotionlist);
        initsilverSlider();

       // slider_image_list = new ArrayList<>();



        return v;
    }

    private void initsilverSlider()
    {
        finalplantArrayList = new ArrayList<>();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PREMIUM_PROMOTION_PRODUCT,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        System.out.println("ressssss------------->"+response);
                        JSONArray objArray = null;
                        JSONArray objProductArray;
                        JSONObject objpromoption=null;
                        JSONObject rootObj = new JSONObject();
                        PromotionProductBean objbean = null;
                        String res = response+"";
                        if(res.contains("false")){
                            //Toast.makeText(getActivity(), "Record not avilable", Toast.LENGTH_SHORT).show();
                        }else
                        {

                            try {
                                objArray = new JSONArray(res);
                                rootObj = objArray.getJSONObject(0);

                                Iterator<String> iterator = rootObj.keys();
                                int y=0;
                                while(iterator.hasNext())
                                {
                                    String key = iterator.next();
                                    if(rootObj.get(key) instanceof JSONArray)
                                    {
                                        plantArrayList = new ArrayList<>();
                                        objProductArray = new JSONArray(rootObj.get(key).toString());
                                        System.out.println("--------------------------------->"+objProductArray.length());
                                        for(int i = 0;i<objProductArray.length();i++) {
                                            objpromoption = objProductArray.getJSONObject(i);

                                            objBean = new ProductlistBean();
                                            objBean.set_productName(objpromoption.getString("ProductName"));
                                            objBean.set_quantatity(objpromoption.getString("Quantatity"));
                                            objBean.set_weight(objpromoption.getString("weight"));
                                            objBean.set_skuid(objpromoption.getString("MerchantSkuId"));
                                            objBean.set_shipinDays(objpromoption.getString("shipindays"));
                                            objBean.set_sellingPrice(objpromoption.getString("SellingPrice"));
                                            objBean.set_productCategory(objpromoption.getString("Categoryname"));
                                            objBean.set_mrp(objpromoption.getString("MRP"));
                                            objBean.set_id(objpromoption.getString("ProuctId"));
                                            objBean.set_desceription(objpromoption.getString("description"));
                                            objBean.set_imageName(objpromoption.getString("ImageName"));
                                            objBean.set_imagepath(objpromoption.getString("ImagePath"));
                                            objBean.set_mobilenumber(objpromoption.getString("mobileno"));
                                            objBean.set_status(objpromoption.getString("Status"));
                                            objBean.set_buisnessid(objpromoption.getString("BuisnessInformationID"));
                                            objBean.set_buisnessType(objpromoption.getString("BuisnessType"));
                                            objBean.set_city(objpromoption.getString("City"));
                                            objBean.set_displayName(objpromoption.getString("DisplayName"));
                                            objBean.set_pincode(objpromoption.getString("PinCode"));
                                            objBean.set_RegisteredDisplayAdress(objpromoption.getString("RegisteredBuisnessAdress"));
                                            objBean.set_RegisteredDisplayName(objpromoption.getString("RegisteredBuisnessName"));
                                            objBean.set_state(objpromoption.getString("State"));
                                            objBean.set_discount(objpromoption.getString("discount"));
                                            // rowcount = Integer.parseInt(objpromoption.getString("rowcount"));
                                            plantArrayList.add(objBean);

                                        }
                                        finalplantArrayList.add(plantArrayList);
                                        LinearLayout llsublayout = new LinearLayout(getActivity());
                                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 370);
                                        params.setMargins(16, 16, 16, 0);
                                        llsublayout.setOrientation(LinearLayout.VERTICAL);
                                        llsublayout.setBackgroundColor(getResources().getColor(R.color.white));

                                        TextView txtPromotionName = new TextView(getActivity());
                                        txtPromotionName.setText(key);
                                        LinearLayout.LayoutParams txtParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,60);
                                        txtPromotionName.setPadding(16,0,0,0);
                                        txtPromotionName.setTextColor(getResources().getColor(R.color.white));
                                        txtPromotionName.setTextSize(17);
                                        txtPromotionName.setGravity(Gravity.LEFT|Gravity.CENTER_VERTICAL);
                                        txtPromotionName.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                        llsublayout.addView(txtPromotionName,txtParams);

                                        RecyclerView rcPromotionrecycler = new RecyclerView(getActivity());
                                        rcPromotionrecycler.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                                        rcPromotionrecycler.setHasFixedSize(true);
                                        LinearLayoutManager llpromotionlist = new LinearLayoutManager(getActivity());
                                        llpromotionlist.setOrientation(LinearLayoutManager.HORIZONTAL);
                                        rcPromotionrecycler.setLayoutManager(llpromotionlist);
                                        PromotionSubListAdapter promotionSubListAdapter = new PromotionSubListAdapter(getActivity().getApplicationContext(),plantArrayList,y);
                                        promotionSubListAdapter.setonClickListner(mPlantsclickListner);
                                        // ca.setonClickListner(mClicklistner);
                                        rcPromotionrecycler.setAdapter(promotionSubListAdapter);
                                        LinearLayout.LayoutParams recyclerListviewparams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,310);
                                        recyclerListviewparams.setMargins(16,16,16,16);
                                        llsublayout.addView(rcPromotionrecycler,recyclerListviewparams);

                                        promotionrootLayout.addView(llsublayout, params);


                                    }
                                    y++;
                                }

                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();;
                            }


                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }){

            @Override
            protected Map<String, String> getParams() throws AuthFailureError {

                Map<String,String> params = new HashMap<String, String>();
                //Adding the parameters otp and username
                params.put("type", "Silver");

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
    private void initPremuiumSlider()

    {

            slider_image_list = new ArrayList<>();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PREMIUM_PROMOTION_PRODUCT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("res------------->"+response);
                            JSONArray objArray = null;
                            JSONObject objpromoption=null;
                            PromotionProductBean objbean = null;
                            String res = response+"";
                            if(res.contains("false")){
                                Toast.makeText(getActivity(), "Record not avilable", Toast.LENGTH_SHORT).show();
                            }else
                            {

                                try {
                                         objArray = new JSONArray(res);
                                         for(int i = 0;i<objArray.length();i++)
                                         {
                                             objpromoption = objArray.getJSONObject(i);
                                             objbean = new PromotionProductBean();
                                             objbean.set_discount(objpromoption.getString("Discount"));
                                             objbean.set_id(objpromoption.getString("id"));
                                             objbean.set_productid(objpromoption.getString("ProductId"));
                                             objbean.set_promotionCategoryId(objpromoption.getString("PromotionCategoryId"));
                                             objbean.set_promotionCategoryName(objpromoption.getString("PromotionCategoryName"));
                                             objbean.set_promotionImageUrl(objpromoption.getString("PromotionImageurl"));
                                             objbean.set_promotionName(objpromoption.getString("PromotionImageName"));
                                             slider_image_list.add(objbean);



                                         }
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();;
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
                                }, 300, 4000);
                                sliderPagerAdapter = new SliderTopPromotionAdapter(getActivity(), slider_image_list);
                                vp_slider.setAdapter(sliderPagerAdapter);
                                vp_slider.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

                                    @Override
                                    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                                    }

                                    @Override
                                    public void onPageScrollStateChanged(int state) {

                                    }

                                    @Override
                                    public void onPageSelected(int position) {
                                        addBottomDots(position);

                                    }
                                });


                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String,String> params = new HashMap<String, String>();
                    //Adding the parameters otp and username
                    params.put("type", "Premium");

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

    private void initCategory()

    {
        catagoryarraylist = new ArrayList<>();
        ProductCatagoryListBean2 obj = new ProductCatagoryListBean2();
        obj.set_catagoryName("Indoor Plants");
        obj.set_categoryImage(R.drawable.indoor);
        catagoryarraylist.add(obj);

        ProductCatagoryListBean2 obj1 = new ProductCatagoryListBean2();
        obj1.set_catagoryName("Outdoor Plants");
        obj1.set_categoryImage(R.drawable.outdoor);
        catagoryarraylist.add(obj1);

        ProductCatagoryListBean2 obj2 = new ProductCatagoryListBean2();
        obj2.set_catagoryName("Pots");
        obj2.set_categoryImage(R.drawable.pots);
        catagoryarraylist.add(obj2);

        ProductCatagoryListBean2 obj3 = new ProductCatagoryListBean2();
        obj3.set_catagoryName("Antiques With Plants");
        obj3.set_categoryImage(R.drawable.antiques);
        catagoryarraylist.add(obj3);
        ProductCatagoryListBean2 obj9 = new ProductCatagoryListBean2();
        obj9.set_catagoryName("Gardening Tools");
        obj9.set_categoryImage(R.drawable.gardening_tools);
        catagoryarraylist.add(obj9);


        ProductCatagoryListBean2 obj5 = new ProductCatagoryListBean2();
        obj5.set_catagoryName("Fertilizer and Pestisides");
        obj5.set_categoryImage(R.drawable.fertilizer_pestisides);
        catagoryarraylist.add(obj5);

        ProductCatagoryListBean2 obj6 = new ProductCatagoryListBean2();
        obj6.set_catagoryName("Seeds");
        obj6.set_categoryImage(R.drawable.seeds);
        catagoryarraylist.add(obj6);

        ProductCatagoryListBean2 obj8 = new ProductCatagoryListBean2();
        obj8.set_catagoryName("LandScapers");
        obj8.set_categoryImage(R.drawable.grass);
        catagoryarraylist.add(obj8);
        ProductCatagoryListBean2 obj4 = new ProductCatagoryListBean2();
        obj4.set_catagoryName("Fruit Plants");
        obj4.set_categoryImage(R.drawable.fruit_plants);
        catagoryarraylist.add(obj4);


        rccategoryList.setHasFixedSize(true);
        GridLayoutManager llm = new GridLayoutManager(getActivity(),3);
        llm.setOrientation(GridLayoutManager.VERTICAL);
        rccategoryList.setLayoutManager(llm);
        mClicklistner = this;
        ProductCategoryListAdapter productCategoryListAdapter = new ProductCategoryListAdapter(getActivity().getApplicationContext(), catagoryarraylist);
        productCategoryListAdapter.setonClickListner(mClicklistner);
        // ca.setonClickListner(mClicklistner);
        rccategoryList.setAdapter(productCategoryListAdapter);

    }

    private void initGoldSlider()

    {

            promotion_image_list = new ArrayList<>();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PREMIUM_PROMOTION_PRODUCT,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("res------------->"+response);
                            JSONArray objArray = null;
                            JSONObject objpromoption=null;
                            PromotionProductBean objbean = null;
                            String res = response+"";
                            if(res.contains("false")){
                                Toast.makeText(getActivity(), "Record not avilable", Toast.LENGTH_SHORT).show();
                            }else
                            {

                                try {
                                    objArray = new JSONArray(res);
                                    for(int i = 0;i<objArray.length();i++)
                                    {
                                        objpromoption = objArray.getJSONObject(i);
                                        objbean = new PromotionProductBean();
                                        objbean.set_discount(objpromoption.getString("Discount"));
                                        objbean.set_id(objpromoption.getString("id"));
                                        objbean.set_productid(objpromoption.getString("ProductId"));
                                        objbean.set_promotionCategoryId(objpromoption.getString("PromotionCategoryId"));
                                        objbean.set_promotionCategoryName(objpromoption.getString("PromotionCategoryName"));
                                        objbean.set_promotionImageUrl(objpromoption.getString("PromotionImageurl"));
                                        objbean.set_promotionName(objpromoption.getString("PromotionImageName"));
                                        promotion_image_list.add(objbean);



                                    }

                                    rcpromotionList.setHasFixedSize(true);
                                    LinearLayoutManager llpromotionlist = new LinearLayoutManager(getActivity());
                                    llpromotionlist.setOrientation(GridLayoutManager.HORIZONTAL);
                                    rcpromotionList.setLayoutManager(llpromotionlist);
                                    PromotionSubAdapter promotionSubAdapter = new PromotionSubAdapter(getActivity().getApplicationContext(), promotion_image_list);
                                    promotionSubAdapter.setonClickListner(mGoldclicklistner);
                                    // ca.setonClickListner(mClicklistner);
                                    rcpromotionList.setAdapter(promotionSubAdapter);
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();;
                                }


                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {

                            Toast.makeText(getActivity(), error.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }){

                @Override
                protected Map<String, String> getParams() throws AuthFailureError {

                    Map<String,String> params = new HashMap<String, String>();
                    //Adding the parameters otp and username
                    params.put("type", "Gold");

                    return params;
                }

            };

            //Adding the request to the queue
            rQueue.add(stringRequest);




    }

    @Override
    public void OnCategoryItemClick(int position) {
        ProductCatagoryListBean2 obj =catagoryarraylist.get(position);
        Fragment fragment = getHomeFragment();
        FragmentTransaction fragmentTransaction = getActivity().getSupportFragmentManager().beginTransaction().addToBackStack(null);
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,android.R.anim.fade_out);
        Bundle bundle = new Bundle();
        bundle.putString("Header Value",obj.get_catagoryName());
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commitAllowingStateLoss();

    }

    private Fragment getHomeFragment() {
        switch (0) {

            case 0:
                // home
                ProductListFragment productListFragment = new ProductListFragment();
                return productListFragment;
           /* case 0:
                // home
                ProductListFragment productListFragment = new ProductListFragment();
                return productListFragment;*/
          /*  case 1:
                // photos
                PhotosFragment photosFragment = new PhotosFragment();
                return photosFragment;
            case 2:
                // movies fragment
                MoviesFragment moviesFragment = new MoviesFragment();
                return moviesFragment;
            case 3:
                // notifications fragment
                NotificationFragment notificationsFragment = new NotificationFragment();
                return notificationsFragment;

            case 4:
                // settings fragment
                SettingsFragment settingsFragment = new SettingsFragment();
                return settingsFragment;
*/
            default:
                return new ProductListFragment();
        }
    }
    private void addBottomDots(int currentPage) {
        dots = new TextView[slider_image_list.size()];

        ll_dots.removeAllViews();
        for (int i = 0; i < dots.length; i++) {

            dots[i] = new TextView(_context);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dots[i].setTextColor(Color.RED);
            ll_dots.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(Color.parseColor("#33D7FF"));
    }

    @Override
    public void onGoldItemClick(int position) {
        PromotionProductBean obj =promotion_image_list.get(position);

        Fragment fragment = new ProductListFragment();
        FragmentTransaction fragmentTransaction = ((FragmentActivity)_context).getSupportFragmentManager().beginTransaction().addToBackStack(null);
        fragmentTransaction.setCustomAnimations(android.R.anim.slide_in_left,
                android.R.anim.fade_out);

        Bundle bundle = new Bundle();
        bundle.putString("Header Value",obj.get_promotionName());
        bundle.putString("productid",obj.get_productid());
        bundle.putString("discount",obj.get_discount());
        fragment.setArguments(bundle);
        fragmentTransaction.replace(R.id.frame, fragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onPlantsItemClick(int position,int groupPosition,int newprice) {
        ProductlistBean obj;
        obj = finalplantArrayList.get(groupPosition).get(position);
        System.out.println("gp------------------->"+groupPosition);
        Intent i = new Intent(getActivity(),PlantsGallary.class);
        Bundle bundle = new Bundle();
        if(newprice>0)
        {
            obj.set_sellingPrice(newprice+"");
        }
        bundle.putSerializable("PLantsDetail",obj);
        i.putExtras(bundle);
        getActivity().startActivity(i);
    }
}
