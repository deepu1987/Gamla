package com.gamla.deepanshu.MyOrder;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.gamla.deepanshu.ProductList.ProductlistBean;
import com.gamla.deepanshu.gamla.R;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyOrderDetails extends AppCompatActivity {

    TextView txtOrderQuantatity,txtproductname,txtstatus,txttransactionid,txtproductUperPrice,txtordersubtotal,txtshipping,txtOrderTotal,txtAdress,txtDeliveryto;
    CircleImageView ivImage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order_details);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Order Details");
        ProductlistBean obj = (ProductlistBean) getIntent().getSerializableExtra("orderdetail") ;
        txtOrderQuantatity = findViewById(R.id.orderdetailsquntatity);
        txtproductname = findViewById(R.id.orderdetailsname);
        txtproductUperPrice = findViewById(R.id.orderdetailsprice);
        txtordersubtotal = findViewById(R.id.ordersubprice);
        txtshipping = findViewById(R.id.ordershipping);
        txtOrderTotal = findViewById(R.id.orderfinalprice);
        txtAdress = findViewById(R.id.orderdetailsdeliveryadress);
        txtstatus = findViewById(R.id.orderdetailsstatus);
        txttransactionid = findViewById(R.id.transactionid);
        ivImage = findViewById(R.id.orderdetailsimage);
        txtDeliveryto = findViewById(R.id.deliveryto);
        txtproductname.setText(obj.get_productName());
        txtOrderQuantatity.setText("YOU HAVE "+obj.get_productPurchaseQuantatity()+" ITEM IN YOUR ORDER");
        txtordersubtotal.setText(obj.get_sellingPrice()+" Rs.");
        txtshipping.setText("0 Rs.");
        int totalprice = Integer.parseInt( obj.get_sellingPrice());
        txtproductUperPrice.setText(totalprice+" Rs.");
        txtOrderTotal.setText(totalprice+" Rs.");
        txtDeliveryto.setText("Delivery to: "+obj.get_fullname());
        txtstatus.setText(obj.get_orderStatus());
        txtAdress.setText(obj.get_deliveryAdress());
        txttransactionid.setText("Transaction Id: "+obj.get_transactionId());

        Picasso.with(getApplicationContext())
                .load(obj.get_imagepath())
                .placeholder(R.mipmap.ic_launcher) // optional
                .error(R.mipmap.ic_launcher)         // optional
                .into(ivImage);

    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
