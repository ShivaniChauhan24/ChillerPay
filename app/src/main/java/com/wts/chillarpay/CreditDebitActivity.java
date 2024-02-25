package com.wts.chillarpay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wts.chillarpay.client.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wts.chillarpay.client.RetrofitClient.KING;

public class CreditDebitActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    Spinner spinner;
    EditText et_amount,et_remark;
    Button UserProceed_button;
    ImageView creditbalance,arrowimage;
    String userid,token,DeviceInfo,comment,amount,creditto,select;
    ArrayList<String> userNameList,userIdList;
    private String key;
    private TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.creditdebit_activity);
        key = getIntent().getStringExtra("key");
        title = findViewById(R.id.toolbar_text);
        if (key.equals("1")) {
            title.setText("Credit Balance");
        } else
        {
            title.setText("Debit Balance");
        }
        spinner=findViewById(R.id.spinner);
        creditbalance=findViewById(R.id.creditbalance);
        arrowimage=findViewById(R.id.arrowimage);
        et_amount=findViewById(R.id.et_amount);
        et_remark=findViewById(R.id.et_remark);
        UserProceed_button=findViewById(R.id.UserProceed_button);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userid = preferences.getString("userid", "");
        token =preferences.getString("token","");
        DeviceInfo =preferences.getString("DeviceInfo","");
        spinner.setOnItemSelectedListener(this);
        getUser();
        arrowimage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        UserProceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                comment = et_remark.getText().toString().trim();
                amount = et_amount.getText().toString().trim();
                if (amount.equalsIgnoreCase("")) {
                    et_amount.setError("Required");
                } else if (comment.equalsIgnoreCase("")) {
                    et_remark.setError("Required");
                }else if (creditto.equalsIgnoreCase("select")){
                    AlertDialog.Builder dialog= new AlertDialog.Builder(CreditDebitActivity.this);
                    dialog.setMessage("Please Select User :");
                    final AlertDialog alertDialog=dialog.create();
                    alertDialog.show();
                }else {
                    if(key.equals("1")){
                        creditbalance();
                    }
                    if(key.equals("2")){
                        debitbalance();
                    }
                    //debitbalance();
                } }
        });
       /* UserProceed_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount=et_amount.getText().toString().trim();
                comment=et_remark.getText().toString().trim();
                if (comment.equalsIgnoreCase("")){
                    et_amount.setError("Required");
                }else if (amount.equalsIgnoreCase("")){
                    et_remark.setError("Required");
                }else{
                    creditbalance();
                }
            }
        });*/
    }
    private void debitbalance() {
        ProgressDialog progressDialog = new ProgressDialog(CreditDebitActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait while execution...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<JsonObject> call=RetrofitClient.getInstance().getApi().debitbalance(KING,userid,token,DeviceInfo,comment,amount,creditto);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject responseObject=new JSONObject(String.valueOf(response.body()));
                        String responseCode = responseObject.getString("response_code");
                        String msg=responseObject.getString("response_msg");
                        if (responseCode.equalsIgnoreCase("TXN")){
                            String response_code=responseObject.getString("transactions");
                            androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(CreditDebitActivity.this);
                            builder.setTitle("Status");
                            builder.setMessage(response_code);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(CreditDebitActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }
                            }).show();
                            //Toast.makeText(CreditDebitActivity.this, "debit seccessful", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }else if (responseCode.equalsIgnoreCase("ERR")){
                            progressDialog.dismiss();
                            new AlertDialog.Builder(CreditDebitActivity.this)
                                    .setTitle("Status")
                                    .setMessage("Something went wrong...")
                                    .show();
                        }else{
                            progressDialog.dismiss();
                            new AlertDialog.Builder(CreditDebitActivity.this)
                                    .setTitle("Status")
                                    .setMessage("Something went wrong...")
                                    .show();
                        }
                        Log.e("sd",msg);
                    } catch (JSONException e){
                        e.printStackTrace();
                        progressDialog.dismiss();
                        new AlertDialog.Builder(CreditDebitActivity.this)
                                .setTitle("Status")
                                .setMessage("Something went wrong...")
                                .show();
                    }
                }else{
                    progressDialog.dismiss();
                    new AlertDialog.Builder(CreditDebitActivity.this)
                            .setTitle("Status")
                            .setMessage("Something went wrong...")
                            .show();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                new AlertDialog.Builder(CreditDebitActivity.this)
                        .setTitle("Status")
                        .setMessage("Something went wrong...")
                        .show();
                Log.e("ae", "onFailure: "+t.getMessage() );
            }
        });
    }
    private void creditbalance() {
        ProgressDialog progressDialog = new ProgressDialog(CreditDebitActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait while execution...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<JsonObject> call=RetrofitClient.getInstance().getApi().creditbalance(KING,userid,token,DeviceInfo,comment,amount,creditto);
        Log.e("er",KING+"\n"+userid+"\n"+token+"\n"+DeviceInfo+"\n"+comment+"\n"+amount+"\n"+creditto);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject responseobject=new JSONObject(String.valueOf(response.body()));
                        String responseCode= responseobject.getString("response_code");
                        if (responseCode.equalsIgnoreCase("TXN")){
                            String response_code=responseobject.getString("transactions");
                            androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(CreditDebitActivity.this);
                            builder.setTitle("Status");
                            builder.setMessage(response_code);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent=new Intent(CreditDebitActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }
                            }).show();
                           // Toast.makeText(CreditDebitActivity.this, "Proceed Successful", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }else if (responseCode.equalsIgnoreCase("ERR")){
                            progressDialog.dismiss();
                            String response_code=responseobject.getString("transactions");
                            AlertDialog.Builder alert = new AlertDialog.Builder(CreditDebitActivity.this);
                            alert.setTitle("Alert!!!");
                            alert.setMessage(response_code);
                            alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    Intent intent = new Intent(CreditDebitActivity.this,MainActivity.class);
                                    startActivity(intent);
                                }
                            });
                            alert.setCancelable(false);
                            alert.show();
                        }else{
                            progressDialog.dismiss();
                            new AlertDialog.Builder(CreditDebitActivity.this)
                                    .setTitle("Status")
                                    .setMessage("Something went wrong....")
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        new AlertDialog.Builder(CreditDebitActivity.this)
                                .setTitle("Status")
                                .setMessage("Something went wrong....")
                                .show();
                    }
                }else{
                    progressDialog.dismiss();
                    new AlertDialog.Builder(CreditDebitActivity.this)
                            .setTitle("Status")
                            .setMessage("Something went wrong....")
                            .show();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                new AlertDialog.Builder(CreditDebitActivity.this)
                        .setTitle("Status")
                        .setMessage("Something went wrong....")
                        .show();
            }
        });
    }
    private void getUser() {
        ProgressDialog progressDialog = new ProgressDialog(CreditDebitActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait while execution...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<JsonObject> call= RetrofitClient.getInstance().getApi().getusers(KING,userid,token,DeviceInfo);
        Log.e("sd",KING+"\n"+userid+"\n"+token+"\n"+DeviceInfo);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject responseObject=new JSONObject(String.valueOf(response.body()));
                        String responseCode=responseObject.getString("response_code");
                        if (responseCode.equalsIgnoreCase("TXN")){
                            JSONArray transactionArray=responseObject.getJSONArray("transactions");
                            userNameList=new ArrayList<>();
                            userIdList=new ArrayList<>();
                            userNameList.add("select");
                            userIdList.add("select");
                            for (int i=0;i<transactionArray.length();i++){
                                JSONObject transactionObject=transactionArray.getJSONObject(i);
                                String userName=transactionObject.getString("OwnerName");
                                String id=transactionObject.getString("id");
                                userNameList.add(userName);
                                userIdList.add(id);
                                ArrayAdapter arrayAdapter=new ArrayAdapter(CreditDebitActivity.this,android.R.layout.simple_spinner_dropdown_item,userNameList);
                                //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                                spinner.setAdapter(arrayAdapter);
                            }
                            progressDialog.dismiss();
                        }else if (responseCode.equalsIgnoreCase("ERR")){
                            progressDialog.dismiss();
                            new AlertDialog.Builder(CreditDebitActivity.this)
                                    .setTitle("Status")
                                    .setMessage("Something went wrong...")
                                    .show();
                        }else{
                            progressDialog.dismiss();
                            new AlertDialog.Builder(CreditDebitActivity.this)
                                    .setTitle("Status")
                                    .setMessage("Something went wrong...")
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        new AlertDialog.Builder(CreditDebitActivity.this)
                                .setTitle("Login Failed")
                                .show();
                    }
                }else{
                    progressDialog.dismiss();
                    new AlertDialog.Builder(CreditDebitActivity.this)
                            .setTitle("Status")
                            .setMessage("Something went wrong...")
                            .show();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                new AlertDialog.Builder(CreditDebitActivity.this)
                        .setTitle("Status")
                        .setMessage("Something went wrong...")
                        .show();
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        creditto=userIdList.get(position);
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}