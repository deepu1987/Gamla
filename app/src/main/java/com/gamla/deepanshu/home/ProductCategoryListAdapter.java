package com.gamla.deepanshu.home;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamla.deepanshu.gamla.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Deepanshu on 11-02-2018.
 */

public class ProductCategoryListAdapter extends RecyclerView.Adapter<ProductCategoryListAdapter.ProductCategoryListViewHolder> {
    public Context context;
    View itemView;
    static OnCategoryItemClickListner mlistner;
    ArrayList<ProductCatagoryListBean2> objArrayList;
    public ProductCategoryListAdapter(Context context, ArrayList<ProductCatagoryListBean2> objArrayList)
    {
        this.context = context;
        this.objArrayList = objArrayList;
    }
    public void setonClickListner(OnCategoryItemClickListner mlistner)
    {
        this.mlistner = mlistner;
    }
    @Override
    public ProductCategoryListAdapter.ProductCategoryListViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.product_category_row, parent, false);
        // itemView.setOnClickListener();

        return new ProductCategoryListAdapter.ProductCategoryListViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ProductCategoryListAdapter.ProductCategoryListViewHolder holder, int position) {

        ProductCatagoryListBean2 obj = objArrayList.get(position);
        holder.txtname.setText(obj.get_catagoryName());
      //  holder.ivimage.setImageResource(R.drawable.fruit_plants);
       Picasso.with(context)
                .load(obj.get_categoryImage())
                .placeholder(R.drawable.placeholder) // optional
                .error(R.drawable.placeholder)         // optional
                .into(holder.ivimage);

    }

    @Override
    public int getItemCount() {
        return objArrayList.size();
    }


    public static class ProductCategoryListViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView ivimage;
        TextView txtname;
        com.travijuu.numberpicker.library.NumberPicker numberPicker;

        public ProductCategoryListViewHolder(View v) {


            super(v);
            ivimage = v.findViewById(R.id.catagoryimage);
            txtname = v.findViewById(R.id.categoryName);

            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
             mlistner.OnCategoryItemClick(getAdapterPosition());
            //Toast.makeText(view.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }

}
