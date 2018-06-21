package com.gamla.deepanshu.DeliveryAdress;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.gamla.deepanshu.gamla.R;

import java.util.ArrayList;

/**
 * Created by Deepanshu on 03-01-2018.
 */

public class AdressAdapter extends RecyclerView.Adapter<AdressAdapter.AdressViewHolder> {

    static onSelectAdressListner mlistner;
    View itemView;
    ArrayList<AdressBean> objArrayList;
    Context context;
    AdressBean obj;

    public AdressAdapter(ArrayList<AdressBean> objArraylist, Context context)
    {
        this.objArrayList = objArraylist;
        this.context = context;
    }

    @Override
    public AdressViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        itemView = LayoutInflater.
                from(parent.getContext()).
                inflate(R.layout.delivery_adress_row, parent, false);
        // itemView.setOnClickListener();

        return new AdressViewHolder(itemView);
    }
    public void setonClickListner(onSelectAdressListner mlistner)
    {
        this.mlistner = mlistner;
    }
    @Override

    public void onBindViewHolder(final AdressViewHolder holder, int position) {
        // holder.bind(items.get(position), listener);
        obj = objArrayList.get(position);
        holder.txtfullname.setText(obj.get_fullname());
        holder.txtadress.setText(obj.get_adress1()+", "+obj.get_adress2()+", "+obj.get_city()+", "+obj.get_state()+" - "+obj.get_pincode());
      //  holder.txtmobilenumber.setText(obj.get_mobilenumber());
        holder.btnSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int position = objArrayList.indexOf(holder.itemView.getTag());
                AdressBean obj = objArrayList.get(position);
                mlistner.onAddresClick(obj.get_adress1()+", "+obj.get_adress2()+", "+obj.get_city()+", "+obj.get_state()+" - "+obj.get_pincode(),obj.get_mobilenumber(),obj.get_fullname(),obj.get_id());
            }
        });
        holder.itemView.setTag(obj);




    }

    @Override
    public int getItemCount() {
        return objArrayList.size();
    }

    public static class AdressViewHolder extends RecyclerView.ViewHolder {

        private TextView txtfullname,txtadress,txtmobilenumber;
        private Button btnSelect;
        public AdressViewHolder(View v) {


            super(v);

            txtadress = v.findViewById(R.id.fulladress);
            txtfullname = v.findViewById(R.id.fname);
            btnSelect = v.findViewById(R.id.selectadress);
           // txtmobilenumber = v.findViewById(R.id.mobilenumberadress);


           // v.setOnClickListener(this);

        }


    }
}
