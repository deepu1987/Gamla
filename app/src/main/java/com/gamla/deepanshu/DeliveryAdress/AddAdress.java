package com.gamla.deepanshu.DeliveryAdress;

import android.graphics.Color;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gamla.deepanshu.Database.DatabaseHandler;
import com.gamla.deepanshu.Function.Utility;
import com.gamla.deepanshu.gamla.Login;
import com.gamla.deepanshu.gamla.R;
import com.github.angads25.toggle.LabeledSwitch;
import com.github.angads25.toggle.interfaces.OnToggledListener;
import com.sdsmdg.tastytoast.TastyToast;

import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class AddAdress extends AppCompatActivity {
    EditText edtfullname, edtmobilenumber, edtpincode, edtadressline1, edtadressline2, edtstate, edtcity;
    Button btnSave;
    String fullname, mobilenumber, pincode, adressline1, adressline2, state, city;
    String varCheck = "true";
    String status = "Not Default";
    RequestQueue rQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_adress);
        rQueue = Volley.newRequestQueue(AddAdress.this);
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
                if (isOn) {
                    status = "Default";
                } else {
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
                if (fullname.length() <= 0) {
                    varCheck = "false";
                    edtfullname.setError("Enter Name");
                }
                if (mobilenumber.length() <= 0) {
                    varCheck = "false";
                    edtmobilenumber.setError("Enter Mobile No.");
                }
                if (pincode.length() <= 0) {
                    varCheck = "false";
                    edtpincode.setError("Enter Pincode");
                }
                if (adressline1.length() <= 0) {
                    varCheck = "false";
                    edtadressline1.setError("Enter Adress");
                }
                if (adressline2.length() <= 0) {
                    varCheck = "false";
                    edtadressline2.setError("Enter Adress");
                }
                if (city.length() <= 0) {
                    varCheck = "false";
                    edtcity.setError("Enter City");
                }
                if (state.length() <= 0) {
                    varCheck = "false";
                    edtstate.setError("Enter State");
                }
                if (varCheck.equalsIgnoreCase("true")) {

                    checkpincodeavailability(pincode);
                }

            }


        });
    }

    private void checkpincodeavailability(final String pincode) {

        {

            // final ProgressDialog loading = ProgressDialog.show(Login.this, "Authenticating", "Please wait while we check the credential", false,false);
            final ACProgressFlower dialog = new ACProgressFlower.Builder(AddAdress.this)
                    .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                    .themeColor(Color.WHITE)

                    .fadeColor(Color.DKGRAY).build();
            dialog.show();
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.CHECK_PINCODE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            //if the server response is success
                            if(response.equalsIgnoreCase("true")){
                                //dismissing the progressbar
                                dialog.dismiss();
                                SaveRecordOnDatabse();

                            }

                            else{
                                //Displaying a toast if the otp entered is wrong
                                dialog.dismiss();
                               // Toast.makeText(AddAdress.this,"Service not available in your location",Toast.LENGTH_LONG).show();
                                TastyToast.makeText(getApplicationContext(), "Service not available in your location", TastyToast.LENGTH_LONG, TastyToast.INFO);
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            dialog.dismiss();
                            Toast.makeText(AddAdress.this, error.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    //Adding the parameters otp and username
                    params.put("pincode", pincode);


                    return params;
                }
            };
            stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                    Utility.TimedOutTimeInMiliSec,
                    DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                    DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
            //Adding the request to the queue
            rQueue.add(stringRequest);
        }
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
