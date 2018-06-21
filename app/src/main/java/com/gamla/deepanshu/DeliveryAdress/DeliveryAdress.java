package com.gamla.deepanshu.DeliveryAdress;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;

import com.gamla.deepanshu.Database.DatabaseHandler;
import com.gamla.deepanshu.gamla.R;

import java.util.ArrayList;

public class DeliveryAdress extends AppCompatActivity implements onSelectAdressListner {
    DatabaseHandler dh;
    ArrayList<AdressBean> objArrayList = new ArrayList<>();
    RecyclerView recList;
    Button btnSelect;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delivery_adress);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Delivery Address");
        dh = new DatabaseHandler(this);
        recList = findViewById(R.id.deliveryAdress);
        btnSelect = findViewById(R.id.selectadress);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);
        new FetchAdressFromDatabse().execute();


    }



    private class FetchAdressFromDatabse extends AsyncTask<Void,Void,String>
    {
        @Override
        protected String doInBackground(Void... voids) {
            objArrayList = dh.GetUserAdress("");
            return null;
        }

        @Override
        protected void onPostExecute(String s) {

            AdressAdapter ad = new AdressAdapter(objArrayList,DeliveryAdress.this);
            ad.setonClickListner(DeliveryAdress.this);
            recList.setAdapter(ad);
            super.onPostExecute(s);
        }
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
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_adress, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addproduct:

                Intent i = new Intent(DeliveryAdress.this,AddAdress.class);
                startActivityForResult(i,101);
                overridePendingTransition(R.anim.slide_down, R.anim.hold);
                return true;


            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public void onAddresClick(String adress, String mobile,String fullname,String id) {

      //  Toast.makeText(this, "dfgdf-->"+adress, Toast.LENGTH_SHORT).show();

        dh.UpdateAddressStatus(id,"Default");
        Intent intent = new Intent();
        intent.putExtra("Adress",adress);
        intent.putExtra("mobile",mobile);
        intent.putExtra("fullname",fullname);

        setResult(Activity.RESULT_OK, intent);
        finish();


    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode==101)
        {
            new FetchAdressFromDatabse().execute();
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
