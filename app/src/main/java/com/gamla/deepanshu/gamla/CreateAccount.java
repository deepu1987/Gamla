package com.gamla.deepanshu.gamla;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;

import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.view.LayoutInflater;
import android.view.View;

import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gamla.deepanshu.Function.FontsUtils;
import com.gamla.deepanshu.Function.Utility;
import com.sdsmdg.tastytoast.TastyToast;
import com.shashank.sony.fancydialoglib.Animation;
import com.shashank.sony.fancydialoglib.FancyAlertDialog;
import com.shashank.sony.fancydialoglib.FancyAlertDialogListener;
import com.shashank.sony.fancydialoglib.Icon;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cc.cloudist.acplibrary.ACProgressConstant;
import cc.cloudist.acplibrary.ACProgressFlower;

public class CreateAccount extends AppCompatActivity  implements View.OnClickListener{
    Button btnReg;
    EditText edtEmail,edtMobileno,edtname,edtpasword,edtconfirmPassword;
    String email,mobileno,name,password,confirmpassword;
    CheckBox chktrm;
    ProgressDialog progressDialog;
    RequestQueue rQueue;
    TextView txtTermCondition;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        FontsUtils fontsUtils = new FontsUtils(this);
        TextView txtgamlahubreg = findViewById(R.id.txtgamlasellerhubreg);
        txtgamlahubreg.setTypeface(fontsUtils.setFont(FontsUtils.Fontname));
        btnReg = findViewById(R.id.startSelling);
        btnReg.setOnClickListener(this);
        edtconfirmPassword = findViewById(R.id.android_hide_show_edittext_password1);
        edtEmail = findViewById(R.id.regemail);
        edtMobileno = findViewById(R.id.regphone);
        edtname = findViewById(R.id.regname);
        edtpasword = findViewById(R.id.android_hide_show_edittext_password);
        chktrm = findViewById(R.id.regcheckbox);
        txtTermCondition = findViewById(R.id.termandcondition);
        rQueue = Volley.newRequestQueue(CreateAccount.this);
        txtTermCondition.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent in = new Intent(CreateAccount.this,TermCondition.class);
                in.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivityForResult(in,101);
            }
        });
    }

    @Override
    public void onBackPressed() {
        finish();
        // overridePendingTransition(R.anim.push_up_in, R.anim.hold);
    }

    @Override
    public void onClick(View view) {

        email = edtEmail.getText().toString();
        mobileno = edtMobileno.getText().toString();
        name = edtname.getText().toString();
        password = edtpasword.getText().toString();
        confirmpassword = edtconfirmPassword.getText().toString();

        if(name.length()<=0)
        {
            edtname.setError("Enter name");
        }

        if(password.length()<=0)
        {
            edtpasword.setError("Enter password");
        }
       /* else if(!isValidPassword(password))

        {
            Toast.makeText(getApplicationContext(),"Password should have 1 Alphabet 1 number and 1 Special character",Toast.LENGTH_SHORT).show();
        }*/
        if(confirmpassword.length()<=0)
        {
            edtconfirmPassword.setError("Enter password");
        }
        if(mobileno.length()<=0)
        {
            edtMobileno.setError("Enter Mobile No.");
        }
        if(mobileno.length()>0&&confirmpassword.length()>0&&password.length()>0&&name.length()>0)
        {
            if(password.equals(confirmpassword))
            {
                if(chktrm.isChecked()) {
                    final String androidId = Settings.Secure.getString(getContentResolver(),
                            Settings.Secure.ANDROID_ID);
                    StringRequest request = new StringRequest(Request.Method.POST, Utility.BaseURl+"registerGamlaUser", new Response.Listener<String>(){
                        @Override
                        public void onResponse(String s) {
                            progressDialog.dismiss();

                            System.out.println("testing------------------->"+s);
                            if(s.equals("true")){
                                //Toast.makeText(CreateAccount.this, "Registration Successful", Toast.LENGTH_LONG).show();
                                TastyToast.makeText(getApplicationContext(), "Registration Successful", TastyToast.LENGTH_LONG, TastyToast.SUCCESS);
                                try {
                                    confirmOtp();
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            else if(s.equalsIgnoreCase("This Mobile number already registered"))
                            {
                                new FancyAlertDialog.Builder(CreateAccount.this)
                                        .setTitle("Registration")
                                        .setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark))  //Don't pass R.color.colorvalue
                                        .setMessage(s)
                                        .setNegativeBtnText("Cancel")
                                        .setPositiveBtnBackground(Color.parseColor("#FF4081"))  //Don't pass R.color.colorvalue
                                        .setPositiveBtnText("OK")
                                        .setNegativeBtnBackground(Color.parseColor("#FFA9A7A8"))  //Don't pass R.color.colorvalue
                                        .setAnimation(Animation.POP)
                                        .isCancellable(true)
                                        .setIcon(R.drawable.ic_star_border_black_24dp, Icon.Visible)
                                        .OnPositiveClicked(new FancyAlertDialogListener() {
                                            @Override
                                            public void OnClick() {

                                            }
                                        })
                                        .OnNegativeClicked(new FancyAlertDialogListener() {
                                            @Override
                                            public void OnClick() {

                                            }
                                        })
                                        .build();
                            }
                            else{
                               // Toast.makeText(CreateAccount.this, "Can't Register", Toast.LENGTH_LONG).show();
                                TastyToast.makeText(getApplicationContext(), "Can't Register", TastyToast.LENGTH_LONG, TastyToast.ERROR);
                            }
                        }
                    },new Response.ErrorListener(){
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            progressDialog.dismiss();
                            String message=null;
                            if (volleyError instanceof NetworkError) {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (volleyError instanceof ServerError) {
                                message = "The server could not be found. Please try again after some time!!";
                            } else if (volleyError instanceof AuthFailureError) {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (volleyError instanceof ParseError) {
                                message = "Parsing error! Please try again after some time!!";
                            } else if (volleyError instanceof NoConnectionError) {
                                message = "Cannot connect to Internet...Please check your connection!";
                            } else if (volleyError instanceof TimeoutError) {
                                message = "Connection TimeOut! Please check your internet connection.";
                            }



                           // Toast.makeText(CreateAccount.this, "Some error occurred -> "+message, Toast.LENGTH_LONG).show();;
                            TastyToast.makeText(getApplicationContext(), "Some error occurred -> "+message, TastyToast.LENGTH_LONG, TastyToast.ERROR);
                        }
                    }) {
                        @Override
                        protected Map<String, String> getParams() throws AuthFailureError {
                            Map<String, String> parameters = new HashMap<String, String>();
                            parameters.put("email", email);
                            parameters.put("password", password);
                            parameters.put("mobileno",mobileno);
                            parameters.put("name",name);
                            parameters.put("macadress",androidId);

                            return parameters;
                        }
                    };

                    request.setRetryPolicy(new DefaultRetryPolicy(
                            Utility.TimedOutTimeInMiliSec,
                            DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                            DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

                    rQueue.add(request);
                    progressDialog = new ProgressDialog(CreateAccount.this);
                    progressDialog.setMessage("Please Wait....");
                    progressDialog.show();

                }
                else
                {
                   // Toast.makeText(getApplicationContext(),"Please check term and condition",Toast.LENGTH_SHORT).show();
                    TastyToast.makeText(getApplicationContext(), "Please check term and condition", TastyToast.LENGTH_LONG, TastyToast.INFO);
                }
            }
            else
            {
                //Toast.makeText(getApplicationContext(),"Password and confirm password should be match",Toast.LENGTH_SHORT).show();
                TastyToast.makeText(getApplicationContext(), "Password and confirm password should be match", TastyToast.LENGTH_LONG, TastyToast.INFO);
            }

        }


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
                final ACProgressFlower dialog = new ACProgressFlower.Builder(CreateAccount.this)
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
                                    //Toast.makeText(CreateAccount.this,"Wrong OTP Please Try Again",Toast.LENGTH_LONG).show();
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
                                Toast.makeText(CreateAccount.this, error.getMessage(), Toast.LENGTH_LONG).show();
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
    public static boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;
        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[A-Z])(?=.*[@#$%^&+=!])(?=\\S+$).{4,}$";
        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }
}
