package com.gamla.deepanshu.MyOrder;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gamla.deepanshu.ProductList.ProductlistBean;
import com.gamla.deepanshu.gamla.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ItemDetail extends AppCompatActivity {
    TextView txtName,txtstatus,txtstatusdate;
    CircleImageView imgImage;
    LinearLayout llOrderDetails;
    ProductlistBean obj;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_detail);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Item Detail");
        txtName = findViewById(R.id.itemdetailsname);
        txtstatus = findViewById(R.id.itemdetailsstatus);
        txtstatusdate = findViewById(R.id.itemdetailsstatusdate);
        imgImage = findViewById(R.id.itemdetailsimage);
        llOrderDetails = findViewById(R.id.vieworderdetails);
        obj  = (ProductlistBean) getIntent().getSerializableExtra("itemdetail");
        txtName.setText(obj.get_productName());
        txtstatus.setText(obj.get_orderStatus());
        SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");
        String date = null;
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTimeInMillis(Long.parseLong(obj.get_timestamp()));
            date  = sdf.format(cal.getTime());
            txtstatusdate.setText(date);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        Picasso.with(getApplicationContext())
                .load(obj.get_imagepath())
                .placeholder(R.mipmap.ic_launcher) // optional
                .error(R.mipmap.ic_launcher)         // optional
                .into(imgImage);

        llOrderDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivityForResult(new Intent(ItemDetail.this,MyOrderDetails.class).putExtra("orderdetail",obj),101);
                overridePendingTransition(R.anim.left_to_right,R.anim.hold);
            }
        });

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
