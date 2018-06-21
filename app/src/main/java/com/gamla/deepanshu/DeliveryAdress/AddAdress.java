package com.gamla.deepanshu.DeliveryAdress;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.gamla.deepanshu.Database.DatabaseHandler;
import com.gamla.deepanshu.gamla.R;
import com.github.angads25.toggle.LabeledSwitch;
import com.github.angads25.toggle.interfaces.OnToggledListener;

public class AddAdress extends AppCompatActivity {
    EditText edtfullname,edtmobilenumber,edtpincode,edtadressline1,edtadressline2,edtstate,edtcity;
    Button btnSave;
    String fullname,mobilenumber,pincode,adressline1,adressline2,state,city;
    String varCheck = "true";
    String status = "Not Default";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adress);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle("Add Address");
        edtfullname = findViewById(R.id.fullname);
        edtmobilenumber = findViewById(R.id.mobilenumber);
        edtpincode = findViewById(R.id.pincode);
        edtadressline1 = findViewById(R.id.adress1);
        edtadressline2 = findViewById(R.id.adress2);
        edtstate = findViewById(R.id.stateadress);
        edtcity = findViewById(R.id.cityadress);
        btnSave = findViewById(R.id.saveAdress);
        LabeledSwitch labeledSwitch = findViewById(R.id.defaultswitch);
        labeledSwitch.setOnToggledListener(new OnToggledListener() {
            @Override
            public void onSwitched(LabeledSwitch labeledSwitch, boolean isOn) {
               if(isOn)
               {
                   status = "Default";
               }
               else
               {
                   status = "Not Default";
               }

            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fullname = edtfullname.getText().toString();
                mobilenumber = edtmobilenumber.getText().toString();
                pincode = edtpincode.getText().toString();
                adressline1 = edtadressline1.getText().toString();
                adressline2 = edtadressline2.getText().toString();
                state = edtstate.getText().toString();
                city = edtcity.getText().toString();
                if(fullname.length()<=0)
                {
                    varCheck = "false";
                    edtfullname.setError("Enter Name");
                }
                if(mobilenumber.length()<=0)
                {
                    varCheck = "false";
                    edtmobilenumber.setError("Enter Mobile No.");
                }
                if(pincode.length()<=0)
                {
                    varCheck = "false";
                    edtpincode.setError("Enter Pincode");
                }
                if(adressline1.length()<=0)
                {
                    varCheck = "false";
                    edtadressline1.setError("Enter Adress");
                }
                if(adressline2.length()<=0)
                {
                    varCheck = "false";
                    edtadressline2.setError("Enter Adress");
                }
                if(city.length()<=0)
                {
                    varCheck = "false";
                    edtcity.setError("Enter City");
                }
                if(state.length()<=0)
                {
                    varCheck = "false";
                    edtstate.setError("Enter State");
                }
                if(varCheck.equalsIgnoreCase("true"))
                {
                    SaveRecordOnDatabse();
                }

            }
        });
    }

    private void SaveRecordOnDatabse()
    {
        DatabaseHandler dh = new DatabaseHandler(this);
        AdressBean objBean = new AdressBean();
        objBean.set_adress1(adressline1);
        objBean.set_adress2(adressline2);
        objBean.set_city(city);
        objBean.set_fullname(fullname);
        objBean.set_mobilenumber(mobilenumber);
        objBean.set_pincode(pincode);
        objBean.set_state(city);
        objBean.set_status(status);
        System.out.println("status---------------------->"+status);
        dh.SaveUserAdress(objBean);
        finish();
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
