package com.gamla.deepanshu.ProductList;

import android.content.Context;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.gamla.deepanshu.Database.DatabaseHandler;
import com.gamla.deepanshu.gamla.R;
import com.sdsmdg.tastytoast.TastyToast;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Deepanshu on 03-01-2018.
 */

public class PlantsAdapter extends RecyclerView.Adapter<PlantsAdapter.PlantsViewHolder> {

    static onPlantsItemClickListner mlistner;
    View itemView;
    ArrayList<ProductlistBean> objArrayList;
    Context context;
    String discount;
     int newprice = 0;
    public PlantsAdapter(ArrayList<ProductlistBean> objArraylist, Context context,String discount)
    {
        this.objArrayList = objArraylist;
        this.context = context;
       // this.discount = discount;
    }

    @Override
    public PlantsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.cardview_for_plants, parent, false);
           // itemView.setOnClickListener();

        return new PlantsViewHolder(itemView);
    }
    public void setonClickListner(onPlantsItemClickListner mlistner)
    {
         this.mlistner = mlistner;
    }
    @Override

    public void onBindViewHolder(final PlantsViewHolder holder, int position) {
       // holder.bind(items.get(position), listener);
      ProductlistBean obj = objArrayList.get(position);
        Picasso.with(context)
                .load(obj.get_imagepath())
                .placeholder(R.drawable.placeholder) // optional
                .error(R.drawable.placeholder)         // optional
                .into(holder.ivImage);
      holder.txtPrice.setText(obj.get_sellingPrice());

      holder.txtname.setText(obj.get_productName());
      holder.itemView.setTag(obj);
      discount = obj.get_discount();
      if(discount.equalsIgnoreCase("0"))
      {
          holder.txtDiscount.setVisibility(View.GONE);
          holder.txtnewPrice.setVisibility(View.GONE);
      }
      else
      {
         holder.txtDiscount.setVisibility(View.VISIBLE);
         holder.txtnewPrice.setVisibility(View.VISIBLE);
         holder.txtPrice.setPaintFlags(holder.txtPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
         newprice = Integer.parseInt(obj.get_sellingPrice())-(Integer.parseInt(obj.get_sellingPrice())*(Integer.parseInt(discount)))/100;
         holder.txtDiscount.setText(discount+"%");
         holder.txtnewPrice.setText(newprice+"");
      }
      holder.btnAddToCart.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View view) {
              int position = objArrayList.indexOf(holder.itemView.getTag());
              DatabaseHandler dh = new DatabaseHandler(context);
              ProductlistBean objBean = objArrayList.get(position);
              objBean.set_productPurchaseQuantatity("1");
              objBean.set_sellingPrice(newprice+"");
              try {
                  dh.savecartRecord(objBean);
                 // Toast.makeText(context, "Product Added to Cart", Toast.LENGTH_SHORT).show();
                  TastyToast.makeText(context,  "Product Added to Cart", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
              }
              catch (Exception e)
              {
                  //e.printStackTrace();
                 // Toast.makeText(context, "Product already added to cart", Toast.LENGTH_SHORT).show();
                  TastyToast.makeText(context,  "Product already added to cart", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
              }
          }
      });



    }

    @Override
    public int getItemCount() {
        return objArrayList.size();
    }

    public static class PlantsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtPrice,txtname,txtDiscount,txtnewPrice;
        private Button btnAddToCart;
        private ImageView ivImage;
        public PlantsViewHolder(View v) {


            super(v);
            txtname = (TextView) v.findViewById(R.id.imagename);
            txtPrice = (TextView) v.findViewById(R.id.txtprice);
            ivImage = v.findViewById(R.id.imageplant);
            btnAddToCart = v.findViewById(R.id.btnAddToCartPlants);
            txtDiscount = v.findViewById(R.id.discountonProduct);
            txtnewPrice = v.findViewById(R.id.txtnewprice);
            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            TextView txtnewprice = view.findViewById(R.id.txtnewprice);
            if(txtDiscount.getVisibility()==View.VISIBLE)
            {
                mlistner.onPlantsItemClick(getAdapterPosition(),0,Integer.parseInt(txtnewPrice.getText().toString()));
            }
            else
            {
                mlistner.onPlantsItemClick(getAdapterPosition(),0,0);
            }

            //Toast.makeText(view.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}
