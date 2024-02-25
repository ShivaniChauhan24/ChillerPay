package com.wts.chillarpay;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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

public class Recharge_Activity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
   ImageView recharge_image,arrow_image;
   EditText et_EnterMobileNumber,et_EnterAmount;
   Spinner spinner;
   Button recharge_button;
   String userid,token,DeviceInfo,amount,operatorid,number,stype,Loginby;
   String serviceName;
   String serviceId;
   ArrayList<String> operatorNameList, operatorIdList;
    private String key;
    private TextView title;
    LinearLayout prepaid_Poweredby,dth_Poweredby;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_);
        registerviews();
        key = getIntent().getStringExtra("key");
        if (key.equals("1")){
            title.setText("Prepaid Recharge");
            prepaid_Poweredby.setVisibility(View.VISIBLE);
            dth_Poweredby.setVisibility(View.GONE);

        }else if (key.equals("2")){
            title.setText("Postpaid Recharge");
            prepaid_Poweredby.setVisibility(View.VISIBLE);
            dth_Poweredby.setVisibility(View.GONE);

        }else{
            title.setText("DTH  Recharge");
            prepaid_Poweredby.setVisibility(View.GONE);
            dth_Poweredby.setVisibility(View.VISIBLE);
        }
        serviceName=getIntent().getStringExtra("service");

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userid = preferences.getString("userid", "");
        token = preferences.getString("token", "");
        DeviceInfo = preferences.getString("DeviceInfo", "");
        spinner.setOnItemSelectedListener(this);

        getService();

        arrow_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        recharge_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                number =  et_EnterMobileNumber.getText().toString().trim();
                amount = et_EnterAmount.getText().toString().trim();
                if (number.equalsIgnoreCase("")){
                    et_EnterMobileNumber.setError("Required");
                }else if (amount.equalsIgnoreCase("")){
                    et_EnterAmount.setError("Required");
                }else if (operatorid.equalsIgnoreCase("Select Operator"))
                {
                    new AlertDialog.Builder(Recharge_Activity.this)
                            .setMessage("Please Select Operator.").show();
                }else{
                    recharge();
                }
            }
        });
    }
      private void registerviews() {
        title = findViewById(R.id.toolbar_text);
        recharge_image=findViewById(R.id.recharge_image);
        et_EnterMobileNumber=findViewById(R.id.et_EnterMobileNumber);
        et_EnterAmount=findViewById(R.id.et_EnterAmount);
        spinner=findViewById(R.id.spinner);
        arrow_image=findViewById(R.id.arrow_image);
        recharge_button=findViewById(R.id.recharge_button);
          prepaid_Poweredby=findViewById(R.id.prepaid_Poweredby);
          dth_Poweredby=findViewById(R.id.dth_Poweredby);
    }
    private void recharge() {
        ProgressDialog progressDialog = new ProgressDialog(Recharge_Activity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait while execution...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<JsonObject> call=RetrofitClient.getInstance().getApi().recharge(KING,userid,token,DeviceInfo,amount,operatorid,number,serviceName,"APP");
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject responseObject= new JSONObject(String.valueOf(response.body()));
                        String responseCode=responseObject.getString("response_code");
                        if (responseCode.equalsIgnoreCase("TXN")){
                            JSONArray transactionArray=responseObject.getJSONArray("transactions");
                            JSONObject transactionObject=transactionArray.getJSONObject(0);
                            String responsecustid = transactionObject.getString("custid");
                            String responseTOKENKEY = transactionObject.getString("TOKENKEY");
                            String responseStype = transactionObject.getString("Stype");
                            String responsenumber = transactionObject.getString("number");
                            String responseamount = transactionObject.getString("amount");
                            String responsecomm = transactionObject.getString("comm");
                            String responsesurcharge = transactionObject.getString("surcharge");
                            String responsecost = transactionObject.getString("cost");
                            String responsebalance = transactionObject.getString("balance");
                            String responsetdatetime = transactionObject.getString("tdatetime");
                            String responseaccountno = transactionObject.getString("accountno");
                            String responsestatus = transactionObject.getString("status");/// ye wala
                            String responsetransactionid = transactionObject.getString("transactionid");
                            String responsebrid = transactionObject.getString("brid");
                            String responseopname = transactionObject.getString("opname");
                            String responseapiname = transactionObject.getString("apiname");

                            View view= LayoutInflater.from(Recharge_Activity.this).inflate(R.layout.recharge_status_layout,null,false);
                            AlertDialog rechargeDialog=new AlertDialog.Builder(Recharge_Activity.this).create();
                            rechargeDialog.setView(view);
                            rechargeDialog.setCancelable(false);
                            ImageView imgStatus=view.findViewById(R.id.img_status);
                            TextView tvStatus=view.findViewById(R.id.tv_status);
                            Button btnOK=view.findViewById(R.id.recharge_button);
                            imgStatus.setImageResource(R.drawable.right);
                            tvStatus.setText(responsestatus);
                            btnOK.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                            rechargeDialog.show();
                            progressDialog.dismiss();
                        }
                        else if (responseCode.equalsIgnoreCase("ERR")){
                            String responseMessage=responseObject.getString("response_msg");
                            View view= LayoutInflater.from(Recharge_Activity.this).inflate(R.layout.recharge_status_layout,null,false);
                            AlertDialog rechargeDialog=new AlertDialog.Builder(Recharge_Activity.this).create();
                            rechargeDialog.setView(view);
                            rechargeDialog.setCancelable(false);
                            ImageView imgStatus=view.findViewById(R.id.img_status);
                            TextView tvStatus=view.findViewById(R.id.tv_status);
                            Button btnOK=view.findViewById(R.id.recharge_button);
                            imgStatus.setImageResource(R.drawable.cross);
                            tvStatus.setText(responseMessage);
                            //tvStatus.setText("failed");
                            tvStatus.setTextColor(getResources().getColor(R.color.red));
                            btnOK.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    finish();
                                }
                            });
                            rechargeDialog.show();
                            progressDialog.dismiss();
                        }
                        else{
                            progressDialog.dismiss();
                            //Toast.makeText(Recharge_Activity.this, "Rechared Failed", Toast.LENGTH_SHORT).show();
                            AlertDialog.Builder dialog=new AlertDialog.Builder(Recharge_Activity.this);
                            dialog.setMessage("Recharge failed");
                            AlertDialog alertDialog=dialog.create();
                            alertDialog.show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        //Toast.makeText(Recharge_Activity.this, "Rechared Failed", Toast.LENGTH_SHORT).show();
                        AlertDialog.Builder dialog=new AlertDialog.Builder(Recharge_Activity.this);
                        dialog.setMessage("Recharge failed");
                        AlertDialog alertDialog=dialog.create();
                        alertDialog.show();
                    }
                }else{
                    progressDialog.dismiss();
                    //Toast.makeText(Recharge_Activity.this, "Rechared Failed", Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder dialog=new AlertDialog.Builder(Recharge_Activity.this);
                    dialog.setMessage("Recharge failed");
                    AlertDialog alertDialog=dialog.create();
                    alertDialog.show();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                //Toast.makeText(Recharge_Activity.this, "RechargeFailed", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder dialog=new AlertDialog.Builder(Recharge_Activity.this);
                dialog.setMessage("Recharge failed");
                AlertDialog alertDialog=dialog.create();
                alertDialog.show();
            }
        });
    }
    private void getOperator() {
        ProgressDialog progressDialog = new ProgressDialog(Recharge_Activity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait while execution...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<JsonObject> call=RetrofitClient.getInstance().getApi().getoperators(KING,userid,token,DeviceInfo, serviceId);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
             if (response.isSuccessful()){
                 try {
                     JSONObject responseObject=new JSONObject(String.valueOf(response.body()));
                     String responseCode=responseObject.getString("response_code");
                     if (responseCode.equalsIgnoreCase("TXN")){
                         operatorNameList =new ArrayList<>();
                         operatorIdList =new ArrayList<>();
                         operatorNameList.add("Select Operator");
                         operatorIdList.add("Select Operator");
                         JSONArray transactionArray=responseObject.getJSONArray("transactions");
                         for (int i=0;i<transactionArray.length();i++){
                             JSONObject transactionObject=transactionArray.getJSONObject(i);
                             String id = transactionObject.getString("id");
                             String OperatorName = transactionObject.getString("OperatorName");
                             operatorNameList.add(OperatorName);
                             operatorIdList.add(id);
                             ArrayAdapter arrayAdapter=new ArrayAdapter(Recharge_Activity.this,R.layout.support_simple_spinner_dropdown_item, operatorNameList);
                             //arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                             spinner.setAdapter(arrayAdapter);
                         }
                         progressDialog.dismiss();
                         }else if (responseCode.equalsIgnoreCase("ERR")){
                         String message=responseObject.getString("response_msg");
                         progressDialog.dismiss();
                         new android.app.AlertDialog.Builder(Recharge_Activity.this)
                                 .setMessage(message)
                                 .setTitle("Status")
                                 .show();
                     }else{
                         progressDialog.dismiss();
                        // Toast.makeText(Recharge_Activity.this, "recharge failed", Toast.LENGTH_SHORT).show();
                         new android.app.AlertDialog.Builder(Recharge_Activity.this)
                                 .setMessage("Something went wrong..")
                                 .setTitle("Status")
                                 .show();
                     }
                 } catch (JSONException e) {
                     e.printStackTrace();
                     progressDialog.dismiss();
                     //Toast.makeText(Recharge_Activity.this, "recharge failed", Toast.LENGTH_SHORT).show();
                     new android.app.AlertDialog.Builder(Recharge_Activity.this)
                             .setMessage("Something went wrong..")
                             .setTitle("Status")
                             .show();
                 }
             }else{
                 progressDialog.dismiss();
                 //Toast.makeText(Recharge_Activity.this, "recharge failed", Toast.LENGTH_SHORT).show();
                 new android.app.AlertDialog.Builder(Recharge_Activity.this)
                         .setMessage("Something went wrong..")
                         .setTitle("Status")
                         .show();
             }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
              progressDialog.dismiss();
                //Toast.makeText(Recharge_Activity.this, "recharge failed", Toast.LENGTH_SHORT).show();
                new android.app.AlertDialog.Builder(Recharge_Activity.this)
                        .setMessage("Something went wrong..")
                        .setTitle("Status")
                        .show();
            }
        });
    }
    private void getService() {
        ProgressDialog progressDialog = new ProgressDialog(Recharge_Activity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait while execution...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<JsonObject> call= RetrofitClient.getInstance().getApi().Getservice(KING,userid,token,DeviceInfo);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject responseObject=new JSONObject(String.valueOf(response.body()));
                        String responseCode=responseObject.getString("response_code");
                        if (responseCode.equalsIgnoreCase("TXN")){
                            JSONArray transactionArray=responseObject.getJSONArray("transactions");
                            for (int i=0;i<transactionArray.length();i++){
                                JSONObject transactionObject=transactionArray.getJSONObject(i);
                                serviceId = transactionObject.getString("serviceid");
                                String responeServiceName = transactionObject.getString("servicename");
                                if (serviceName.equalsIgnoreCase(responeServiceName))
                                {
                                    getOperator();
                                    break;
                                }
                            }
                            progressDialog.dismiss();
                        }else if (responseCode.equalsIgnoreCase("ERR")){
                            String message=responseObject.getString("response_msg");
                            progressDialog.dismiss();
                            new android.app.AlertDialog.Builder(Recharge_Activity.this)
                                    .setMessage(message)
                                    .setTitle("Status")
                                    .show();
                        }else{
                            progressDialog.dismiss();
                            //Toast.makeText(Recharge_Activity.this, "rechrage failed", Toast.LENGTH_SHORT).show();
                            new android.app.AlertDialog.Builder(Recharge_Activity.this)
                                    .setMessage("Something went wrong..")
                                    .setTitle("Status")
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        //Toast.makeText(Recharge_Activity.this, "recharge failed", Toast.LENGTH_SHORT).show();
                        new android.app.AlertDialog.Builder(Recharge_Activity.this)
                                .setMessage("Something went wrong..")
                                .setTitle("Status")
                                .show();
                    }
                }else{
                    progressDialog.dismiss();
                    //Toast.makeText(Recharge_Activity.this, "recharge failed", Toast.LENGTH_SHORT).show();
                    new android.app.AlertDialog.Builder(Recharge_Activity.this)
                            .setMessage("Something went wrong..")
                            .setTitle("Status")
                            .show();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                //Toast.makeText(Recharge_Activity.this, t.getMessage()+"", Toast.LENGTH_SHORT).show();
                new android.app.AlertDialog.Builder(Recharge_Activity.this)
                        .setMessage("Something went wrong..")
                        .setTitle("Status")
                        .show();
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        operatorid = operatorIdList.get(position);
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}

