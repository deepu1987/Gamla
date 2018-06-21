package com.gamla.deepanshu.home;


import android.app.Activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.gamla.deepanshu.ProductList.ProductListFragment;
import com.gamla.deepanshu.gamla.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Deepanshu on 20-01-2018.
 */

public class SliderTopPromotionAdapter extends PagerAdapter {
    private LayoutInflater layoutInflater;
    Activity activity;
    ArrayList<PromotionProductBean> image_arraylist;

    public SliderTopPromotionAdapter(Activity activity, ArrayList<PromotionProductBean> image_arraylist) {
        this.activity = activity;
        this.image_arraylist = image_arraylist;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        layoutInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View view = layoutInflater.inflate(R.layout.plant_slider, container, false);
        ImageView im_slider = (ImageView) view.findViewById(R.id.im_slider);
        System.out.println("catagory image----------->"+image_arraylist.get(position).get_promotionName());
        Picasso.with(activity.getApplicationContext())
                .load(image_arraylist.get(position).get_promotionImageUrl())
                .placeholder(R.drawable.placeholder) // optional
                .error(R.drawable.placeholder)         // optional
                .into(im_slider);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PromotionProductBean obj =image_arraylist.get(position);

                Fragment fragment = new ProductListFragment();
                FragmentActivity ac = (FragmentActivity) activity;
                FragmentTransaction fragmentTransaction = ac.getSupportFragmentManager().beginTransaction().addToBackStack(null);
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
        });
        container.addView(view);

        return view;
    }

    @Override
    public int getCount() {
        return image_arraylist.size();
    }


    @Override
    public boolean isViewFromObject(View view, Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}