package com.gamla.deepanshu.ProductList;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SearchView;
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
import com.gamla.deepanshu.Database.DatabaseHandler;
import com.gamla.deepanshu.ProductDetail.PlantsGallary;
import com.gamla.deepanshu.gamla.R;
import com.gamla.deepanshu.Function.Utility;
import com.github.pwittchen.infinitescroll.library.InfiniteScrollListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProductListFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProductListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProductListFragment extends Fragment implements onPlantsItemClickListner {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "Header Value";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private FrameLayout llfilter,llsortby;
    private OnFragmentInteractionListener mListener;
    private onPlantsItemClickListner mClicklistner;
    CharSequence [] items = {"None","Price : High to Low","Price : Low to High"};
    int checkedSortItem = 0;
    ArrayList<ProductlistBean> plantArrayList= new ArrayList<>();
    RequestQueue rQueue;
    ProductlistBean objBean;
    RecyclerView recList;
    GridLayoutManager llm;
    int limitstart =0;
    int limitend = 15;
    int rowcount;
    String productid;
    String discount;
    TextView txtSortByText,txtFilterBYTexrt;
    DatabaseHandler dh;
    String FilterQuery = "";
    String FilterPriceQuery = "";
    ArrayList<String> objArraylist = new ArrayList<>();
    ImageView ivRedIndicator;
    public ProductListFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProductListFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProductListFragment newInstance(String param1, String param2) {
        ProductListFragment fragment = new ProductListFragment();
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        mClicklistner = this;
        setHasOptionsMenu(true);
        View v = inflater.inflate(R.layout.fragment_home,container,false);
        rQueue = Volley.newRequestQueue(getActivity());
        llsortby = v.findViewById(R.id.sortby);
        llfilter = v.findViewById(R.id.filter);
        txtSortByText = v.findViewById(R.id.sortbytext);
        txtFilterBYTexrt = v.findViewById(R.id.filtertext);
        ivRedIndicator = v.findViewById(R.id.redindicator);
        dh = new DatabaseHandler(getActivity());
        //init();
        Bundle bundle = getArguments();
        String headervalue = bundle.getString("Header Value");
        productid = bundle.getString("productid","0");
        discount = bundle.getString("discount","0");
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(mParam1);

        recList = (RecyclerView) v.findViewById(R.id.cardList);
        recList.setHasFixedSize(true);
        llm = new GridLayoutManager(getActivity(),2);
        llm.setOrientation(GridLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        recList.addOnScrollListener(createInfiniteScrollListener());
        FetchProductFromServer(0,"","","",limitstart,limitend);



        llsortby.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(getContext())
                        .setSingleChoiceItems(items, checkedSortItem, null)
                        .setIcon(R.drawable.gamla)
                        .setTitle("Sort By")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                                int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();

                                checkedSortItem = selectedPosition;
                                if(checkedSortItem==0)
                                {
                                   txtSortByText.setText("Relevance");
                                }
                                else if(checkedSortItem==1)
                                {
                                    txtSortByText.setText("Price: High To Low");
                                }
                                else
                                {
                                    txtSortByText.setText("Price: Low To High");
                                }
                                plantArrayList = new ArrayList<>();
                                FetchProductFromServer(checkedSortItem,"",FilterQuery,FilterPriceQuery,0,limitstart);
                                // Do something useful withe the position of the selected radio button
                            }
                        })
                        .show();
            }
        });
        llfilter.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent i = new Intent(getActivity(),FilterPlants.class);
                i.putExtra("category",mParam1);
                startActivityForResult(i,101);
                getActivity().overridePendingTransition(R.anim.puul_up_from_bottom, R.anim.hold);
            }
        });
        return v;
    }

    private void FetchProductFromServer(int pos, final String Search, final String filterQqery ,final String filterpricequery, final int limitstart1, final int limitend1)
    {


           final ProgressDialog loading = ProgressDialog.show(getActivity(), "Gamla", "Please wait...", false,false);
       /* final SpotsDialog loading = new SpotsDialog(getActivity());
        loading.show();

        loading.dismiss();*/
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRODUCTLIST_CATAGORYWISE_URL,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("res------------->"+response);
                            String res = response+"";
                            if(res.contains("false")){
                                loading.dismiss();
                                Toast.makeText(getActivity(), "Record not avilable", Toast.LENGTH_SHORT).show();


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
                                        rowcount = Integer.parseInt(obj.getString("rowcount"));
                                        plantArrayList.add(objBean);
                                    }
                                }
                                catch (Exception e)
                                {

                                }
                            }
                            limitstart = limitstart+15;
                            PlantsAdapter ca = new PlantsAdapter(plantArrayList,getActivity().getApplicationContext(),discount);
                            ca.setonClickListner(mClicklistner);
                            recList.setAdapter(ca);
                            loading.dismiss();
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
                    JSONObject obj = new JSONObject();
                    try {
                        obj.put("status","In Stock");
                        obj.put("catagory",mParam1);
                        obj.put("productid",productid);
                        obj.put("Sort",checkedSortItem);
                        obj.put("Search",Search);
                        obj.put("filterpricequery",filterpricequery);
                        obj.put("filterQuery",filterQqery);
                        obj.put("limitstart",limitstart1+"");
                        obj.put("limitend",limitend1+"");
                    }
                    catch (Exception e)
                    {

                    }
                    Map<String,String> params = new HashMap<String, String>();
                    //Adding the parameters otp and username
                    params.put("data", obj+"");

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
    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
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
                    FetchProductFromServer(0,"","","",limitstart,limitend);
                    refreshView(recList, null, firstVisibleItemPosition);
                }
            }
        };
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            //
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
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

    @Override
    public void onPlantsItemClick(int position,int groupPosition,int newprice) {
        ProductlistBean obj;
        obj = plantArrayList.get(position);
        Intent i = new Intent(getActivity(),PlantsGallary.class);
        Bundle bundle = new Bundle();
        if(newprice>0) {
            obj.set_sellingPrice(newprice+"");
        }
        bundle.putSerializable("PLantsDetail",obj);

        i.putExtras(bundle);
        getActivity().startActivity(i);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);


        MenuItem searchViewItem = menu.findItem(R.id.search);
        final SearchView searchViewAndroidActionBar = (SearchView) MenuItemCompat.getActionView(searchViewItem);
        searchViewAndroidActionBar.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchViewAndroidActionBar.clearFocus();
                System.out.println("==========================="+query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                System.out.println("========>>>>>>>>>>>>>>>>>>>>>>>==================="+newText);
                limitstart = 0;
                if(newText.length()>0)
                {

                    String SerachString = "%"+newText+"%";
                    String Searchquery =  "and ProductName Like '"+SerachString+"'";
                    plantArrayList = new ArrayList<>();
                    FetchProductFromServer(checkedSortItem,Searchquery,FilterQuery,FilterPriceQuery,limitstart,limitend);
                }
                else
                {
                    plantArrayList = new ArrayList<>();
                    FetchProductFromServer(checkedSortItem,newText,FilterQuery,FilterPriceQuery,limitstart,limitend);
                }

                return true;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        String minvalue = data.getStringExtra("minvalue");
        String maxvalue = data.getStringExtra("maxvalue");


        objArraylist = dh.getSelectedProductTypeRecord();
        limitstart=0;
        String s ="";
        if(objArraylist.size()>0)
        {

            txtFilterBYTexrt.setText("Product Type");

            for (int i=0;i<objArraylist.size();i++) {
                  if(i<objArraylist.size()-1) {
                      s = s + "'"+objArraylist.get(i)+"'" + ",";
                  }
                  else
                  {
                      s = s + "'"+objArraylist.get(i)+"'";
                  }

            }

            ivRedIndicator.setVisibility(View.VISIBLE);
            FilterQuery = "and ProductName IN("+s+")";
            FilterPriceQuery = "and (SellingPrice BETWEEN "+minvalue+" And "+maxvalue+")";
            System.out.println("!!!!!!!!!!!!!!!!!==========>"+FilterQuery);
            plantArrayList = new ArrayList<>();
            FetchProductFromServer(checkedSortItem,"",FilterQuery,FilterPriceQuery,limitstart,limitend);
        }
        else
        {
            ivRedIndicator.setVisibility(View.GONE);
            txtFilterBYTexrt.setText("Select");
            FilterQuery = "";
            if(minvalue!=null&&maxvalue!=null)
            {
                FilterPriceQuery = "and (SellingPrice BETWEEN "+minvalue+" And "+maxvalue+")";
            }
            else{
                FilterPriceQuery = "";
            }

            System.out.println("filter proce query==========>"+FilterPriceQuery);
            plantArrayList = new ArrayList<>();
            FetchProductFromServer(checkedSortItem,"",FilterQuery,FilterPriceQuery,limitstart,limitend);
        }
    }
}
