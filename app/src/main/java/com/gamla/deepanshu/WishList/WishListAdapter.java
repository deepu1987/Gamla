package com.gamla.deepanshu.WishList;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gamla.deepanshu.Database.DatabaseHandler;
import com.gamla.deepanshu.Function.Utility;
import com.gamla.deepanshu.MyOrder.MyOrder;
import com.gamla.deepanshu.ProductList.ProductlistBean;
import com.gamla.deepanshu.ProductList.onPlantsItemClickListner;
import com.gamla.deepanshu.ShoppingCart.ItemBag;
import com.gamla.deepanshu.gamla.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Deepanshu on 03-01-2018.
 */

public class WishListAdapter extends RecyclerView.Adapter<WishListAdapter.WishListViewHolder> {

    static onPlantsItemClickListner mlistner;
    View itemView;
    ArrayList<ProductlistBean> objArrayList;
    private Context context;
    private ActionBar actionBar;
    RequestQueue rQueue;
    ArrayList<String> objArrylistuser = new ArrayList<>();
    DatabaseHandler dh;
    public WishListAdapter(ArrayList<ProductlistBean> objArraylist, Context context,ActionBar actionBar)
    {
        this.objArrayList = objArraylist;
        this.context = context;
        this.actionBar = actionBar;
        rQueue = Volley.newRequestQueue(context);
        dh  = new DatabaseHandler(context);
        try {
            objArrylistuser = dh.getUserRecord();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    @Override
    public WishListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.wishlist_row, parent, false);
        // itemView.setOnClickListener();

        return new WishListViewHolder(itemView);
    }
    public void setonClickListner(onPlantsItemClickListner mlistner)
    {
        this.mlistner = mlistner;
    }
    @Override

    public void onBindViewHolder(final WishListViewHolder holder, int position) {
        // holder.bind(items.get(position), listener);
        ProductlistBean obj = objArrayList.get(position);
        Picasso.with(context)
                .load(obj.get_imagepath())
                .placeholder(R.mipmap.ic_launcher) // optional
                .error(R.mipmap.ic_launcher)         // optional
                .into(holder.ivImage);
        holder.txtPrice.setText("Rs. "+obj.get_sellingPrice());
        holder.txtname.setText(obj.get_productName());
       // holder.txtadress.setText("by : "+obj.get_RegisteredDisplayName());
        holder.txtadress.setText("by : TrueGarden Agro Pvt. Ltd.");
        holder.txtRemove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = objArrayList.indexOf(holder.itemView.getTag());
                DeleteRecordFromServer(position,"false");
            }
        });
        holder.txtMovetoBag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = objArrayList.indexOf(holder.itemView.getTag());
                DeleteRecordFromServer(position,"true");
            }
        });

        holder.itemView.setTag(obj);



    }

    @Override
    public int getItemCount() {
        return objArrayList.size();
    }

    public static class WishListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtPrice,txtname,txtadress,txtRemove,txtMovetoBag;
        private CircleImageView ivImage;
        public WishListViewHolder(View v) {


            super(v);
            txtname = (TextView) v.findViewById(R.id.wishproductname);
            txtPrice = (TextView) v.findViewById(R.id.wishprice);
            ivImage = v.findViewById(R.id.wishimage);
            txtadress = v.findViewById(R.id.wishadress);
            txtMovetoBag = v.findViewById(R.id.wishlistmovetobag);
            txtRemove = v.findViewById(R.id.wishlistremove);


            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mlistner.onPlantsItemClick(getAdapterPosition(),0,0);
            //Toast.makeText(view.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }
    private void DeleteRecordFromServer(final int position,final String flag)
    {
        final ProgressDialog loading = ProgressDialog.show(context, "WishList", "Please wait...", false,false);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.REMOVE_WISHLIST,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //if the server response is success
                        loading.dismiss();
                        if(response.equalsIgnoreCase("Item Removed to wishlist")){


                            objArrayList.get(position).set_productPurchaseQuantatity("1");
                            try {
                                dh.savecartRecord(objArrayList.get(position));
                                objArrayList.remove(position);
                                actionBar.setTitle(objArrayList.size()+" item in your wishlist");
                            }
                            catch (Exception e)
                            {
                                e.printStackTrace();
                            }
                            if(objArrayList.size()==0)
                            {
                                ((Activity)context).finish();
                            }
                            else
                            {
                                notifyDataSetChanged();
                            }

                        }else{
                            Toast.makeText(context, response, Toast.LENGTH_LONG).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String,String> params = new HashMap<String, String>();
                //Adding the parameters otp and username
                params.put("mobileno",objArrylistuser.get(0));
                params.put("productid",objArrayList.get(position).get_id());
                return params;
            }
        };

        //Adding the request to the queue
        rQueue.add(stringRequest);
    }
}
