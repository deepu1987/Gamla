package com.gamla.deepanshu.ProductList;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.gamla.deepanshu.Database.DatabaseHandler;
import com.gamla.deepanshu.Function.Utility;
import com.gamla.deepanshu.gamla.R;
import com.sdsmdg.tastytoast.TastyToast;

import org.json.JSONArray;
import org.json.JSONObject;

public class FilterByProduct extends AppCompatActivity {
    Context context = null;
    FilterByProductAdapter objAdapter;
    ListView lv = null;
    EditText edtSearch = null;
    LinearLayout llContainer = null;
    Button btnOK = null;
    String headername;
    RelativeLayout rlPBContainer = null;
    String catagory;
    RequestQueue rQueue;
    String s="";
    public ArrayList<PlantsFilterByCategoryObject> phoneList = new ArrayList<PlantsFilterByCategoryObject>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        setContentView(R.layout.filter_by_product);

        Bundle bundle = getIntent().getExtras();
        catagory = bundle.getString("catagory");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setTitle(bundle.getString("headername"));
        getWindow().setSoftInputMode( WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        rlPBContainer = (RelativeLayout) findViewById(R.id.pbcontainer);
        edtSearch = (EditText) findViewById(R.id.input_search);
        llContainer = (LinearLayout) findViewById(R.id.data_container);
        btnOK = (Button) findViewById(R.id.ok_button);
        btnOK.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                getSelectedContacts();
                DatabaseHandler dh = new DatabaseHandler(context);
                dh.deleteTable(DatabaseHandler.TABLE_FILTER_PRODUCT_TYPE);
                dh.SaveProductTypeRecord(phoneList);
                Intent resultIntent = new Intent();

                resultIntent.putExtra("filtervalue", s);
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            }
        });
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2,int arg3) {
                // When user changed the Text
                String text = edtSearch.getText().toString().toLowerCase(Locale.getDefault());
                objAdapter.filter(text);
            }
            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1,int arg2, int arg3) {
                // TODO Auto-generated method stub
            }
            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
            }
        });
        rQueue = Volley.newRequestQueue(context);
        DatabaseHandler dh = new DatabaseHandler(context);
        phoneList = dh.getProdyctTypeRecord();
        if(phoneList.size()<=0) {
            FetchProductTypeFromServer();
        }
        else
        {
            addContactsInList();
        }

    }

    private void FetchProductTypeFromServer()
    {
            final ProgressDialog loading = ProgressDialog.show(FilterByProduct.this, "Gamla", "Please wait...", false,false);
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Utility.PRODUCTTYPE,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            System.out.println("res------------->"+response);
                            String res = response+"";
                            if(res.contains("false")){
                                loading.dismiss();
                               // Toast.makeText(FilterByProduct.this, "Record not avilable", Toast.LENGTH_SHORT).show();
                                TastyToast.makeText(getApplicationContext(),  "Record not avilable", TastyToast.LENGTH_LONG, TastyToast.INFO);

                            }else{
                                try {
                                    JSONArray objArray = new JSONArray(res);
                                    for(int i=0;i<objArray.length();i++)
                                    {
                                        JSONObject obj = objArray.getJSONObject(i);
                                        PlantsFilterByCategoryObject objbean = new PlantsFilterByCategoryObject();

                                        objbean.set_name(obj.getString("name"));

                                        phoneList.add(objbean);

                                    }
                                    addContactsInList();
                                }
                                catch (Exception e)
                                {
                                    e.printStackTrace();
                                }
                            }
                            loading.dismiss();
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                          //  Toast.makeText(FilterByProduct.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            TastyToast.makeText(getApplicationContext(),  error.getMessage(), TastyToast.LENGTH_LONG, TastyToast.ERROR);

                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new HashMap<String, String>();
                    //Adding the parameters otp and username

                    params.put("catagory",catagory);
                    return params;
                }

            };

            //Adding the request to the queue
            rQueue.add(stringRequest);
    }
    private void getSelectedContacts() {
        // TODO Auto-generated method stub
        StringBuffer sb = new StringBuffer();
        for (PlantsFilterByCategoryObject bean : phoneList) {
            if (bean.isSelected()) {
                sb.append(bean.get_name());
                sb.append(",");
            }
        }
        s = sb.toString().trim();
        if (TextUtils.isEmpty(s)) {
            Toast.makeText(context, "Select atleast one Contact",
                    Toast.LENGTH_SHORT).show();
        } else {
            s = s.substring(0, s.length() - 1);
          /*  Toast.makeText(context, "Selected Contacts : " + s,
                    Toast.LENGTH_SHORT).show();*/
        }
    }

    private void addContactsInList() {
        // TODO Auto-generated method stub
        Thread thread = new Thread() {
            @Override
            public void run() {
                showPB();
                char ch;
                try {
                    /*for (ch = 'a'; ch <= 'z'; ch++) {
                        PlantsFilterByCategoryObject obj = new PlantsFilterByCategoryObject();

                        obj.set_name(ch + "");
                        obj.set_id("1");
                        phoneList.add(obj);
                    }*/
                    /*}
                    phones.close();*/
                    lv = new ListView(context);
                    lv.setLayoutParams(new LayoutParams(
                            LayoutParams.MATCH_PARENT,
                            LayoutParams.MATCH_PARENT));
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // TODO Auto-generated method stub
                            llContainer.addView(lv);
                        }
                    });
                    Collections.sort(phoneList,
                            new Comparator<PlantsFilterByCategoryObject>() {
                                @Override
                                public int compare(PlantsFilterByCategoryObject lhs,
                                                   PlantsFilterByCategoryObject rhs) {
                                    return lhs.get_name().compareTo(
                                            rhs.get_name());
                                }
                            });
                    objAdapter = new FilterByProductAdapter(FilterByProduct.this,
                            phoneList);
                    lv.setAdapter(objAdapter);
                    lv.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent,
                                                View view, int position, long id) {
                            CheckBox chk = (CheckBox) view
                                    .findViewById(R.id.contactcheck);
                            PlantsFilterByCategoryObject bean = phoneList
                                    .get(position);
                            if (bean.isSelected()) {
                                bean.setSelected(false);
                                chk.setChecked(false);
                            } else {
                                bean.setSelected(true);
                                chk.setChecked(true);
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
                hidePB();
            }
        };
        thread.start();
    }

    void showPB() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                rlPBContainer.setVisibility(View.VISIBLE);
                edtSearch.setVisibility(View.GONE);
                btnOK.setVisibility(View.GONE);
            }
        });
    }

    void hidePB() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TODO Auto-generated method stub
                rlPBContainer.setVisibility(View.GONE);
                edtSearch.setVisibility(View.VISIBLE);
                btnOK.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();

        overridePendingTransition(R.anim.left_to_right, R.anim.hold);
        return true;
    }
}