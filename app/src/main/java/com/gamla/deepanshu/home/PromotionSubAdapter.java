package com.gamla.deepanshu.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gamla.deepanshu.gamla.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Deepanshu on 11-02-2018.
 */

public class PromotionSubAdapter extends RecyclerView.Adapter<PromotionSubAdapter.ProductPromotionSubListViewHolder> {
    public Context context;
    View itemView;
    static OnGoldItemClickListner mlistner;
    ArrayList<PromotionProductBean> objArrayList;
    public PromotionSubAdapter(Context context, ArrayList<PromotionProductBean> objArrayList)
    {
        this.context = context;
        this.objArrayList = objArrayList;
    }
    public void setonClickListner(OnGoldItemClickListner mlistner)
    {
        this.mlistner = mlistner;
    }
    @Override
    public PromotionSubAdapter.ProductPromotionSubListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.product_promotion_row, parent, false);
        // itemView.setOnClickListener();

        return new PromotionSubAdapter.ProductPromotionSubListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PromotionSubAdapter.ProductPromotionSubListViewHolder holder, int position) {

        PromotionProductBean obj = objArrayList.get(position);
       // holder.txtname.setText(obj.get_catagoryName());
        //  holder.ivimage.setImageResource(R.drawable.fruit_plants);
        Picasso.with(context)
                .load(obj.get_promotionImageUrl())
                .placeholder(R.drawable.placeholder) // optional
                .error(R.drawable.placeholder)         // optional
                .into(holder.ivimage);

    }

    @Override
    public int getItemCount() {
        return objArrayList.size();
    }


    public static class ProductPromotionSubListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivimage;
       // TextView txtname;
        com.travijuu.numberpicker.library.NumberPicker numberPicker;

        public ProductPromotionSubListViewHolder(View v) {


            super(v);
            ivimage = v.findViewById(R.id.promotionimageview);
           // txtname = v.findViewById(R.id.categoryName);

            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mlistner.onGoldItemClick(getAdapterPosition());
            //Toast.makeText(view.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }

}
