package com.gamla.deepanshu.MyOrder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.gamla.deepanshu.ProductList.ProductlistBean;
import com.gamla.deepanshu.ProductList.onPlantsItemClickListner;
import com.gamla.deepanshu.gamla.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Deepanshu on 03-01-2018.
 */

public class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.OrderViewHolder> {

    static onPlantsItemClickListner mlistner;
    View itemView;
    ArrayList<ProductlistBean> objArrayList;
    Context context;

    public MyOrderAdapter(ArrayList<ProductlistBean> objArraylist, Context context)
    {
        this.objArrayList = objArraylist;
        this.context = context;
    }

    @Override
    public OrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.order_list_row, parent, false);
        // itemView.setOnClickListener();

        return new OrderViewHolder(itemView);
    }
    public void setonClickListner(onPlantsItemClickListner mlistner)
    {
        this.mlistner = mlistner;
    }
    @Override

    public void onBindViewHolder(OrderViewHolder holder, int position) {
        // holder.bind(items.get(position), listener);
        ProductlistBean obj = objArrayList.get(position);
        Picasso.with(context)
                .load(obj.get_imagepath())
                .placeholder(R.mipmap.ic_launcher) // optional
                .error(R.mipmap.ic_launcher)         // optional
                .into(holder.ivImage);
        holder.txtPrice.setText("Rs. "+obj.get_sellingPrice());
        holder.txtname.setText(obj.get_productName());
        holder.txtorderid.setText(obj.get_orderId());
        holder.txtStatus.setText(obj.get_orderStatus());
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM yyyy HH:mm a");
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.parseLong(obj.get_timestamp()));
            String date  = sdf.format(cal.getTime());
            holder.txtdate.setText(date+"");
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }



    }

    @Override
    public int getItemCount() {
        return objArrayList.size();
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView txtPrice,txtname,txtorderid,txtdate,txtStatus;
        private CircleImageView ivImage;
        public OrderViewHolder(View v) {


            super(v);
            txtname = (TextView) v.findViewById(R.id.orderproductname);
            txtPrice = (TextView) v.findViewById(R.id.orderrs);
            txtdate = v.findViewById(R.id.orderdate);
            txtorderid = v.findViewById(R.id.ordernumber);
            txtStatus = v.findViewById(R.id.orderstatus);
            ivImage = v.findViewById(R.id.orderimage);


            v.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mlistner.onPlantsItemClick(getAdapterPosition(),0,0);
            //Toast.makeText(view.getContext(), "position = " + getPosition(), Toast.LENGTH_SHORT).show();
        }
    }
}
