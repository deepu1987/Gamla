package com.gamla.deepanshu.ProductList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamla.deepanshu.gamla.R;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by Deepanshu on 28-01-2018.
 */

public class FilterByProductAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private List<PlantsFilterByCategoryObject> mainDataList = null;
    private ArrayList<PlantsFilterByCategoryObject> arraylist;
    public FilterByProductAdapter(Context context, List<PlantsFilterByCategoryObject> mainDataList) {

        mContext = context;
        this.mainDataList = mainDataList;
        inflater = LayoutInflater.from(mContext);
        this.arraylist = new ArrayList<PlantsFilterByCategoryObject>();
        this.arraylist.addAll(mainDataList);


    }
    static class ViewHolder {
        protected TextView name;
        protected TextView number;
        protected CheckBox check;
        protected ImageView image;
    }
    @Override
    public int getCount() {
        return mainDataList.size();
    }
    @Override
    public PlantsFilterByCategoryObject getItem(int position) {
        return mainDataList.get(position);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    public View getView(final int position, View view, ViewGroup parent) {
        final ViewHolder holder;
        if (view == null) {
            holder = new ViewHolder();
            view = inflater.inflate(R.layout.filter_by_product_row, null);
            holder.name = (TextView) view.findViewById(R.id.contactname);

            holder.check = (CheckBox) view.findViewById(R.id.contactcheck);

            view.setTag(holder);
            view.setTag(R.id.contactname, holder.name);

            view.setTag(R.id.contactcheck, holder.check);
            holder.check
                    .setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton vw,
                                                     boolean isChecked) {
                            int getPosition = (Integer) vw.getTag();
                            mainDataList.get(getPosition).setSelected(
                                    vw.isChecked());
                        }
                    });
        } else {
            holder = (ViewHolder) view.getTag();
        }
        holder.check.setTag(position);

        holder.name.setText(mainDataList.get(position).get_name());






        holder.check.setChecked(mainDataList.get(position).isSelected());
        return view;
    }
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        mainDataList.clear();
        if (charText.length() == 0) {
            mainDataList.addAll(arraylist);
        } else {
            for (PlantsFilterByCategoryObject wp : arraylist) {
                if (wp.get_name().toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    mainDataList.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }




}
