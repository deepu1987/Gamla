package com.gamla.deepanshu.ShoppingCart;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gamla.deepanshu.ProductList.ProductlistBean;
import com.gamla.deepanshu.Database.DatabaseHandler;
import com.gamla.deepanshu.DeliveryAdress.DeliveryAdress;
import com.gamla.deepanshu.gamla.Login;
import com.gamla.deepanshu.gamla.R;
import com.gamla.deepanshu.Function.Utility;
import com.squareup.picasso.Picasso;
import com.travijuu.numberpicker.library.Enums.ActionEnum;
import com.travijuu.numberpicker.library.Interface.LimitExceededListener;
import com.travijuu.numberpicker.library.Interface.ValueChangedListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Deepanshu on 08-02-2018.
 */

public class ItemBagAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    View itemView;

    public Context context;
    ProductlistBean obj;
    DatabaseHandler dh;
    private ArrayList<ProductlistBean> objArrayList = new ArrayList<>();
    ActionBar actionBar;
    ArrayList<String> objArrylistuser;
    RequestQueue rQueue;
    FooterViewHolder footerHolder;
    private static final int TYPE_HEADER = 0;
    private static final int TYPE_FOOTER = 1;
    private static final int TYPE_ITEM = 2;
    String address,mobilenumber;


    public ItemBagAdapter(Context context, ArrayList<ProductlistBean> objArrayList, ActionBar actionBar,String address,String mobilenumber)
    {
        this.context = context;
        this.objArrayList = objArrayList;
        this.actionBar = actionBar;
        rQueue = Volley.newRequestQueue(context);
        dh = new DatabaseHandler(context);
        this.address = address;
        this.mobilenumber = mobilenumber;

       // Toast.makeText(context, "adress---->"+address, Toast.LENGTH_SHORT).show();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            //Inflating recycle view item layout
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bag, parent, false);
            return new ItemBagAdapter.ItemBagViewHolder(itemView);
        } /*else if (viewType == TYPE_HEADER) {
            //Inflating header view
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_header, parent, false);
            return new ItemBagAdapter.HeaderViewHolder(itemView);
        }*/ else if (viewType == TYPE_FOOTER) {
            //Inflating footer view
            View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bag_price_summary, parent, false);
            return new ItemBagAdapter.FooterViewHolder(itemView);
        } else return null;

      /*  itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.item_bag, parent, false);
        // itemView.setOnClickListener();

        return new ItemBagAdapter.ItemBagViewHolder(itemView);*/
    }
    @Override
    public int getItemViewType(int position) {
        /*if (position == 0) {
            return TYPE_HEADER;
        } else*/ if (position == objArrayList.size()) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {



       /* if (holder instanceof HeaderViewHolder) {
            HeaderViewHolder headerHolder = (HeaderViewHolder) holder;
            headerHolder.headerTitle.setText("Header View");
            headerHolder.headerTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(activity, "You clicked at Header View!", Toast.LENGTH_SHORT).show();
                }
            });
        } else*/ if (holder instanceof FooterViewHolder) {
            footerHolder = (FooterViewHolder) holder;

            String totalPrice = dh.getToatalAmmount();

            int shipping = objArrayList.size()*20;
            int finalprice=0;
            try {
                 finalprice = Integer.parseInt(totalPrice) + shipping;
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
            footerHolder.txtSubTotal.setText(totalPrice+" Rs.");
            footerHolder.txtShipping.setText("0 Rs");
            footerHolder.txtTotal.setText(totalPrice+" Rs.");
            footerHolder.txtAdress.setText(address);
            footerHolder.txtMobilenumber.setText(mobilenumber);
            footerHolder.btnChangeAdress.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ((Activity)context).startActivityForResult(new Intent(context,DeliveryAdress.class),102);
                    ((Activity)context).overridePendingTransition(R.anim.puul_up_from_bottom,R.anim.hold);
                }
            });
            /*footerHolder.footerText.setText("Footer View");
            footerHolder.footerText.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Toast.makeText(context, "You clicked at Footer View", Toast.LENGTH_SHORT).show();
                }
            });*/
        } else if (holder instanceof ItemBagViewHolder) {
            final ItemBagViewHolder itemViewHolder = (ItemBagViewHolder) holder;
            obj = objArrayList.get(position);
            final int totalprice = Integer.parseInt(obj.get_sellingPrice());
            itemViewHolder.txttotalprice.setText(totalprice+" Rs.");
            itemViewHolder.txtprice.setText(obj.get_sellingPrice()+" Rs.");
            itemViewHolder.txtname.setText(obj.get_productName());
            itemViewHolder.txtStatus.setText(obj.get_status());
            if(obj.get_status().equalsIgnoreCase("In Stock"))
            {
                itemViewHolder.txtStatus.setBackground(context.getResources().getDrawable(R.drawable.rounded_corner_soild_green_textview));
            }
            else
            {
                itemViewHolder.txtStatus.setBackground(context.getResources().getDrawable(R.drawable.rounded_corner_red_textview));

            }
            System.out.println("quantatity------------------------------>"+Integer.parseInt(obj.get_quantatity()));
            itemViewHolder.numberPicker.setValue(Integer.parseInt(obj.get_productPurchaseQuantatity()));
            itemViewHolder.numberPicker.setValueChangedListener(new ValueChangedListener() {
                @Override
                public void valueChanged(int value, ActionEnum action) {

                    ProductlistBean objnew = objArrayList.get(objArrayList.indexOf(itemViewHolder.itemView.getTag()));
                    int updatedprice = (Integer.parseInt(objnew.get_sellingPrice())/(Integer.parseInt( obj.get_productPurchaseQuantatity())))*value;
                    int updatedtotalprice = updatedprice;
                    itemViewHolder.txtprice.setText(updatedprice+" Rs");
                    itemViewHolder.txttotalprice.setText(updatedtotalprice+" Rs");

                    dh.updateQunatatity(objnew.get_id(),value+"",updatedprice+"");
                    String totalPrice = dh.getToatalAmmount();
                    int shipping = objArrayList.size()*20;
                    int finalprice = Integer.parseInt(totalPrice)+shipping;
                    footerHolder.txtSubTotal.setText(totalPrice+" Rs.");
                    footerHolder.txtShipping.setText("0 Rs");
                    footerHolder.txtTotal.setText(totalPrice+" Rs.");
                    footerHolder.txtAdress.setText(address);
                    footerHolder.txtMobilenumber.setText(mobilenumber);
                    //notifyDataSetChanged();



                }
            });
            itemViewHolder.numberPicker.setLimitExceededListener(new LimitExceededListener() {
                @Override
                public void limitExceeded(int limit, int exceededValue) {
                    String message = String.format("You can purchase only %d product", limit);
                    // Log.v(this.getClass().getSimpleName(), message);
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                }
            });
            itemViewHolder.txtremove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    dh.deleteRecordFromCart(objArrayList.get(objArrayList.indexOf(itemViewHolder.itemView.getTag())).get_id());
                    objArrayList.remove(objArrayList.indexOf(itemViewHolder.itemView.getTag()));
                    actionBar.setTitle(objArrayList.size()+" item in your bag");

                    if(objArrayList.size()==0)
                    {
                        ((Activity)context).finish();
                    }
                    else
                    {
                        notifyDataSetChanged();
                    }
                }
            });
            itemViewHolder.txtmoveToWishList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    objArrylistuser = dh.getUserRecord();
                    if(objArrylistuser.size()>0) {

                        int position = objArrayList.indexOf(itemViewHolder.itemView.getTag());
                        SaveWishListOnServer(position);
                        dh.deleteRecordFromCart(objArrayList.get(position).get_id());
                        objArrayList.remove(objArrayList.indexOf(itemViewHolder.itemView.getTag()));
                        actionBar.setTitle(objArrayList.size()+" item in your bag");
                        //txtLogin.setText(context.getResources().getString(R.string.deliveryadress));
                        notifyDataSetChanged();
                        if(objArrayList.size()==0)
                        {
                            ((Activity)context).finish();
                        }
                        else
                        {
                            notifyDataSetChanged();
                        }

                    }
                    else
                    {
                        System.out.println("---------------------------------===========================");
                        Activity ac = (Activity) context;
                        ac.startActivityForResult(new Intent(context,Login.class),101);
                        ac.overridePendingTransition(R.anim.puul_up_from_bottom,R.anim.hold);
                    }
                }
            });
           // itemViewHolder.txtSoldBy.setText(obj.get_RegisteredDisplayName());
            Picasso.with(context)
                    .load(obj.get_imagepath())
                    .placeholder(R.drawable.placeholder) // optional
                    .error(R.drawable.placeholder)       // optional
                    .into(itemViewHolder.ivimage);
            itemViewHolder.itemView.setTag(obj);
        }
    }
    @Override
    public int getItemCount() {
        return objArrayList.size()+1;
    }
    private class FooterViewHolder extends RecyclerView.ViewHolder {
        TextView txtSubTotal,txtShipping,txtTotal,txtAdress,txtMobilenumber;
        Button btnChangeAdress;

        public FooterViewHolder(View view) {
            super(view);
            txtSubTotal =  view.findViewById(R.id.subtotal);
            txtShipping =  view.findViewById(R.id.totalshipping);
            txtTotal    = view.findViewById(R.id.totalprice);
            txtAdress = view.findViewById(R.id.delivertfulladres);
            txtMobilenumber = view.findViewById(R.id.deliverymobileno);
            btnChangeAdress = view.findViewById(R.id.changeAdress);
        }
    }
    public  class ItemBagViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivimage;
        TextView txtprice,txttotalprice,txtname,txtSoldBy,txtremove,txtmoveToWishList,txtStatus;
        com.travijuu.numberpicker.library.NumberPicker numberPicker;
        public ItemBagViewHolder(View v) {

            super(v);

            txtprice = v.findViewById(R.id.txtprice);
            txtname = v.findViewById(R.id.textView10);
            txttotalprice = v.findViewById(R.id.txttotalPrice);
            ivimage = v.findViewById(R.id.imageView );
            numberPicker = v.findViewById(R.id.number_picker);
            txtSoldBy = v.findViewById(R.id.soldbybag);
            txtremove = v.findViewById(R.id.removefromcart);
            txtmoveToWishList = v.findViewById(R.id.movetowishlist);
            txtStatus = v.findViewById(R.id.productstatus);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
          //  mlistner.onPlantsItemClick(getAdapterPosition());
            //Toast.makeText(view.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }

    private void SaveWishListOnServer(final int position)
    {
        final ProgressDialog loading = ProgressDialog.show(context, "Gamla", "Please wait...", false,false);

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

                            Toast.makeText(context, "Item Added to wishlist", Toast.LENGTH_SHORT).show();
                        }


                        loading.dismiss();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
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
