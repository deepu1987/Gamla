package com.gamla.deepanshu.gamla;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Icon;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
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
import com.gamla.deepanshu.Function.FontsUtils;
import com.gamla.deepanshu.Function.Utility;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class Login extends AppCompatActivity {
    EditText edtmobile,edtpassword;
    TextView txtForgotpassword;
    String password,mobileno;
    RequestQueue rQueue;
    DatabaseHandler dh;
    String emailid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        dh = new DatabaseHandler(this);
        edtmobile = findViewById(R.id.loginMobile);
        edtpassword = findViewById(R.id.android_hide_show_edittext_password_login);
        txtForgotpassword = findViewById(R.id.forgotpassword);
        TextView txtgalmlasellerhub = (TextView) findViewById(R.id.txtgamlasellerhub);
        Button login = findViewById(R.id.btnLogin);
        FontsUtils fu = new FontsUtils(this);
        txtgalmlasellerhub.setTypeface(fu.setFont("Grand_Aventure.otf"));
        Button llcretestore = findViewById(R.id.cretestore);
        rQueue = Volley.newRequestQueue(Login.this);
        llcretestore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Login.this,CreateAccount.class);
                startActivityForResult(i,101);
                overridePendingTransition(R.anim.puul_up_from_bottom, R.anim.hold);
            }
        });
        txtForgotpassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                ShowForgotPasswortDialog();
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mobileno = edtmobile.getText().toString();
                password = edtpassword.getText().toString();
                if(mobileno.length()<=0)
                {
                    edtmobile.setError("Enter MobileNo.");
                }
                if(password.length()<=0)
                {
                    edtpassword.setError("Enter password");
                }
                if(mobileno.length()>0&&password.length()>0)
                {
                    final String androidId = Settings.Secure.getString(getContentResolver(),Settings.Secure.ANDROID_ID);
                   // final ProgressDialog loading = ProgressDialog.show(Login.this, "Authenticating", "Please wait while we check the credential", false,false);
                    final ACProgressFlower dialog = new ACProgressFlower.Builder(Login.this)
                            .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                            .themeColor(Color.WHITE)

                            .fadeColor(Color.DKGRAY).build();
                    dialog.show();
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.LOGIN_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //if the server response is success
                                    if(response.contains("true")){
                                        //dismissing the progressbar
                                        dialog.dismiss();

                                        emailid = response.split("_")[1];
                                        dh.SaveUserRecord(mobileno,password,emailid);
                                        finish();

                                    }
                                    else if(response.equalsIgnoreCase("Mobile number not verified"))
                                    {
                                        dialog.dismiss();
                                        try {
                                            confirmOtp();
                                        }
                                        catch (Exception e)
                                        {
                                            e.printStackTrace();
                                        }
                                    }
                                    else{
                                        //Displaying a toast if the otp entered is wrong
                                        dialog.dismiss();
                                        Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();

                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    dialog.dismiss();
                                    Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            //Adding the parameters otp and username
                            params.put("mobile", mobileno);
                            params.put("password", password);
                            params.put("macadress",androidId);

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
        });


    }
    private void confirmOtp() throws JSONException {
        //Creating a LayoutInflater object for the dialog box
        LayoutInflater li = LayoutInflater.from(this);
        //Creating a view to get the dialog box
        View confirmDialog = li.inflate(R.layout.dialog_confirm, null);

        //Initizliaing confirm button fo dialog box and edittext of dialog box
        AppCompatButton buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonConfirm);
        final  EditText editTextConfirmOtp = (EditText) confirmDialog.findViewById(R.id.editTextOtp);

        //Creating an alertdialog builder
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        //Adding our dialog box to the view of alert dialog
        alert.setView(confirmDialog);

        //Creating an alert dialog
        final AlertDialog alertDialog = alert.create();

        //Displaying the alert dialog
        alertDialog.show();

        //On the click of the confirm button from alert dialog
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hiding the alert dialog
                alertDialog.dismiss();

                //Displaying a progressbar
                //final ProgressDialog loading = ProgressDialog.show(CreateAccount.this, "Authenticating", "Please wait while we check the entered code", false,false);
                final ACProgressFlower dialog = new ACProgressFlower.Builder(Login.this)
                        .direction(ACProgressConstant.DIRECT_CLOCKWISE)
                        .themeColor(Color.WHITE)

                        .fadeColor(Color.DKGRAY).build();
                dialog.show();
                //Getting the user entered otp from edittext
                final String otp = editTextConfirmOtp.getText().toString().trim();

                //Creating an string request
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.CONFIRM_URL,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {
                                //if the server response is success
                                if(response.equalsIgnoreCase("true")){
                                    //dismissing the progressbar
                                    dialog.dismiss();
                                    dh.SaveUserRecord(mobileno,password,emailid);
                                    //Starting a new activity
                                    finish();
/*
                                    Bundle bundle = new Bundle();
                                    bundle.putString("email",email);
                                    bundle.putString("name",name);
                                    bundle.putString("mobileno",mobileno);
                                    startActivity(new Intent(CreateAccount.this, Login.class).putExtras(bundle));*/
                                }else{
                                    //Displaying a toast if the otp entered is wrong
                                   // Toast.makeText(Login.this,"Wrong OTP Please Try Again",Toast.LENGTH_LONG).show();
                                    TastyToast.makeText(getApplicationContext(), "Wrong OTP Please Try Again", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                                    try {
                                        //Asking user to enter otp again
                                        confirmOtp();
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                alertDialog.dismiss();
                               // Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                TastyToast.makeText(getApplicationContext(), error.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        //Adding the parameters otp and username
                        params.put("otp", otp);
                        params.put("mobileno", mobileno);
                        return params;
                    }
                };

                //Adding the request to the queue
                stringRequest.setRetryPolicy(new DefaultRetryPolicy(
                        Utility.TimedOutTimeInMiliSec,
                        DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                        DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
                rQueue.add(stringRequest);
            }
        });
    }

    private void ShowForgotPasswortDialog()
    {
        LayoutInflater li = LayoutInflater.from(this);
        View confirmDialog = li.inflate(R.layout.forgotpassword, null);
        AppCompatButton buttonConfirm = (AppCompatButton) confirmDialog.findViewById(R.id.buttonSubmitForgot);
        final  EditText editTextMObileNUmber = (EditText) confirmDialog.findViewById(R.id.editMobileNumber);
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(confirmDialog);
        final AlertDialog alertDialog = alert.create();
        alertDialog.show();
        buttonConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                final ProgressDialog loading = ProgressDialog.show(Login.this, "Authenticating", "Please wait...", false,false);
                final String mobilenumber = editTextMObileNUmber.getText().toString().trim();
                StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.FORGOT_PASSWORD,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if(response.equalsIgnoreCase("true")){
                                    //dismissing the progressbar
                                    loading.dismiss();
                                   // Toast.makeText(Login.this,"Your password sent on your mobile number",Toast.LENGTH_LONG).show();
                                    TastyToast.makeText(getApplicationContext(), "Your password sent on your mobile number", TastyToast.LENGTH_LONG, TastyToast.INFO);

                                   // finish();
                                }else{
                                    //Displaying a toast if the otp entered is wrong
                                    loading.dismiss();
                                    //Toast.makeText(Login.this,"Mobile number not registered",Toast.LENGTH_LONG).show();
                                    TastyToast.makeText(getApplicationContext(), "Mobile number not registered", TastyToast.LENGTH_LONG, TastyToast.INFO);

                                }
                            }
                        },
                        new Response.ErrorListener() {
                            @Override
                            public void onErrorResponse(VolleyError error) {
                                alertDialog.dismiss();
                                loading.dismiss();
                                Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String,String> params = new HashMap<String, String>();
                        params.put("mobileno", mobilenumber);
                        params.put("flag","buyer");
                        return params;
                    }
                };

                //Adding the request to the queue
                rQueue.add(stringRequest);
            }
        });
    }

}
