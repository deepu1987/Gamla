package com.gamla.deepanshu.gamla;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gamla.deepanshu.Database.DatabaseHandler;
import com.gamla.deepanshu.Function.FontsUtils;
import com.gamla.deepanshu.Function.Utility;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity {
    EditText edtmobile,edtpassword;
    TextView txtForgotpassword;
    String password,mobileno;
    RequestQueue rQueue;
    DatabaseHandler dh;
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

                    final ProgressDialog loading = ProgressDialog.show(Login.this, "Authenticating", "Please wait while we check the credential", false,false);

                    StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.LOGIN_URL,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    //if the server response is success
                                    if(response.equalsIgnoreCase("true")){
                                        //dismissing the progressbar
                                        loading.dismiss();
                                        dh.SaveUserRecord(mobileno,password);
                                        finish();

                                    }else{
                                        //Displaying a toast if the otp entered is wrong
                                        loading.dismiss();
                                        Toast.makeText(Login.this,response,Toast.LENGTH_LONG).show();

                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    loading.dismiss();
                                    Toast.makeText(Login.this, error.getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }){
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String,String> params = new HashMap<String, String>();
                            //Adding the parameters otp and username
                            params.put("mobile", mobileno);
                            params.put("password", password);

                            return params;
                        }
                    };

                    //Adding the request to the queue
                    rQueue.add(stringRequest);
                }


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
                                    Toast.makeText(Login.this,"Your password sent on your mobile number",Toast.LENGTH_LONG).show();
                                   // finish();
                                }else{
                                    //Displaying a toast if the otp entered is wrong
                                    loading.dismiss();
                                    Toast.makeText(Login.this,"Mobile number not registered",Toast.LENGTH_LONG).show();

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
