package com.gamla.deepanshu.MyOrder;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.gamla.deepanshu.Database.DatabaseHandler;
import com.gamla.deepanshu.gamla.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

public class SearchOrder extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {


    // String[] status = {"Sekect Status","In Stock","Out of stock","Non live"};
    String[] catagory = {"Select Catagory","Indoor Plants","Outdoor Plants","Pots","Antiques With Plants","Fruit Plants","Green House Plants","Seeds","Landscapers","Gardening Tools"};
    EditText edtProductid,edtweight,edtproductname,edtskuid,edtDate;
    String var = "false";
    String status;
    Button submit,reset;
    RequestQueue rQueue;
    String searchquery="";
    DatabaseHandler dh;
    ArrayList<String> objArraylist;

    //ArrayList<HashMap<String,String>> objArrayList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_order);
        dh = new DatabaseHandler(this);
         objArraylist = dh.getUserRecord();

        rQueue = Volley.newRequestQueue(SearchOrder.this);
        status = getIntent().getStringExtra("status");
        System.out.println("status------->"+status);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Search Order");
        final Spinner spncategory = findViewById(R.id.searchCategory);
        edtProductid = findViewById(R.id.searchProductid);
        edtproductname = findViewById(R.id.searchproductname);
        edtskuid = findViewById(R.id.searchskuid);
        edtweight = findViewById(R.id.searchweight);
        submit = findViewById(R.id.searchsubmit);
        reset = findViewById(R.id.searchreset);
        edtDate = findViewById(R.id.searchplaceddate);
        //shipday.setOnItemSelectedListener(this);
        ArrayAdapter aa = new ArrayAdapter(this,android.R.layout.simple_spinner_item,catagory);
        aa.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //Setting the ArrayAdapter data on the Spinner
        spncategory.setAdapter(aa);

        edtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerFragment fragment = new DatePickerFragment();
                fragment.show(getFragmentManager(),null);
            }
        });
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent();
                i.putExtra("Searchquery", "BuyerMobile = "+objArraylist.get(0));
                setResult(RESULT_OK, i);
                finish();
            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                HashMap<String,String> map = new HashMap();
                if(spncategory.getSelectedItem().toString().equalsIgnoreCase("Select Catagory")&&edtProductid.getText().toString().length()<=0&&edtproductname.getText().toString().length()<=0&&edtskuid.getText().toString().length()<=0&&edtweight.getText().toString().length()<=0&&edtDate.getText().toString().length()<=0){
                    Toast.makeText(SearchOrder.this, "Enter at least one field", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    //  map.put("Status",status);
                    if(!spncategory.getSelectedItem().toString().equalsIgnoreCase("Select Catagory"))
                    {
                        map.put("CatagoryName",spncategory.getSelectedItem().toString());
                    }
                    if(edtProductid.getText().toString().length()>0)
                    {
                        map.put("ProuctId",edtProductid.getText().toString());
                    }
                    if(edtproductname.getText().toString().length()>0)
                    {
                        map.put("ProductName",edtproductname.getText().toString());
                    }
                    if(edtskuid.getText().toString().length()>0)
                    {
                        map.put("MerchantSkuId",edtskuid.getText().toString());
                    }
                    if(edtweight.getText().toString().length()>0)
                    {
                        map.put("Weight",edtweight.getText().toString());
                    }
                    if(edtDate.getText().toString().length()>0)
                    {
                        map.put("TimestampforSearch",edtDate.getText().toString());
                    }
                    // objArrayList.add(map);
                    int i = 1;
                    for(Map.Entry<String,String> entry : map.entrySet())
                    {
                        if(i<map.size()) {
                            searchquery = searchquery + entry.getKey() + "='" + entry.getValue() + "' and ";
                            i++;
                        }
                        else
                        {
                            searchquery = searchquery + entry.getKey() + "='" + entry.getValue() + "'";
                            i++;
                        }
                    }
                    System.out.println("searchquery------------->"+searchquery);
                    ArrayList<String> objArraylist = dh.getUserRecord();
                    searchquery = searchquery+" and BuyerMobile = "+objArraylist.get(0);
                    Intent intent = new Intent();
                    intent.putExtra("Searchquery",searchquery);
                    setResult(RESULT_OK,intent);
                    finish();

                }


            }
        });


    }

    private void setDate(final Calendar calendar) {
        final SimpleDateFormat sdf = new SimpleDateFormat("dd-MMM-yyyy");

        edtDate.setText(sdf.format(calendar.getTime()));

    }
    @Override
    public void onDateSet(DatePicker view, int year, int month, int day) {
        Calendar cal = new GregorianCalendar(year, month, day);
        setDate(cal);
    }

    public static class DatePickerFragment extends DialogFragment {
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(),(DatePickerDialog.OnDateSetListener)getActivity(), year, month, day);
        }

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        Intent i = new Intent();
        i.putExtra("Searchquery",searchquery);
        setResult(RESULT_OK,i);
        finish();
    }
}
