package com.gamla.deepanshu.ProductList;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarChangeListener;
import com.crystal.crystalrangeseekbar.interfaces.OnRangeSeekbarFinalValueListener;
import com.crystal.crystalrangeseekbar.widgets.CrystalRangeSeekbar;
import com.gamla.deepanshu.gamla.R;

public class FilterPlants extends AppCompatActivity {

    private RelativeLayout rlCategoty,rlProductType,rlcolor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.filter_product);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Filter By");
        rlCategoty = findViewById(R.id.catogry);
        rlcolor = findViewById(R.id.color);
        rlProductType = findViewById(R.id.producttype);
        final CrystalRangeSeekbar rangeSeekbar = (CrystalRangeSeekbar) findViewById(R.id.rangeSeekbar1);
        rangeSeekbar.setMinValue(200.0f);
        rangeSeekbar.setMaxValue(450.0f);

// get min and max text view
        final TextView tvMin = (TextView) findViewById(R.id.min);
        final TextView tvMax = (TextView) findViewById(R.id.max);

// set listener
        rangeSeekbar.setOnRangeSeekbarChangeListener(new OnRangeSeekbarChangeListener() {
            @Override
            public void valueChanged(Number minValue, Number maxValue) {
                tvMin.setText(String.valueOf(minValue));
               tvMax.setText(String.valueOf(maxValue));
            }
        });

// set final value listener
        rangeSeekbar.setOnRangeSeekbarFinalValueListener(new OnRangeSeekbarFinalValueListener() {
            @Override
            public void finalValue(Number minValue, Number maxValue) {
                Log.d("CRS=>", String.valueOf(minValue) + " : " + String.valueOf(maxValue));
            }
        });
        rlProductType.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                Intent i = new Intent(FilterPlants.this,FilterByProduct.class);
                i.putExtra("headername","Product Type");


                startActivityForResult(i,101);
                overridePendingTransition(R.anim.left_to_right,R.anim.hold);

            }
        });
        rlcolor.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilterPlants.this,FilterByProduct.class);
                i.putExtra("headername","Color");
                startActivityForResult(i,101);
                overridePendingTransition(R.anim.left_to_right,R.anim.hold);

            }
        });
        rlCategoty.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Intent i = new Intent(FilterPlants.this,FilterByProduct.class);
                i.putExtra("headername","Category");
                startActivityForResult(i,101);
                overridePendingTransition(R.anim.left_to_right,R.anim.hold);


            }
        });


    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        overridePendingTransition(R.anim.hold, R.anim.push_out_to_bottom);
        return true;
    }
}
