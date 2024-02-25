package com.wts.chillarpay;

import androidx.appcompat.app.AppCompatActivity;

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

public class FundRequest extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
        ImageView arrow_image;
        TextView toolbar_text;
        Spinner spinner;
        EditText et_EnterAmount,et_EnterRefereshNumber,et_EnterRemark;
        Button UserSubmit_button;
        String userid,comment,amount,bankrefno,paymentmode,token,DeviceInfo;
        String[] names={"Select","BY IMPS","BY WALLET","BY CASH IN BANK","UPI PAYMENT","BY ATM TRANSFER"};

   //   ArrayList<String> Names;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fund_request);
        arrow_image=findViewById(R.id.arrow_image);
        toolbar_text=findViewById(R.id.toolbar_text);
        spinner=findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(this);
        ArrayAdapter arrayAdapter=new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,names);
        spinner.setAdapter(arrayAdapter);
        et_EnterAmount=findViewById(R.id.et_EnterAmount);
        et_EnterRefereshNumber=findViewById(R.id.et_EnterRefereshNumber);
        et_EnterRemark=findViewById(R.id.et_EnterRemark);
        UserSubmit_button=findViewById(R.id.UserSubmit_button);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userid = preferences.getString("userid", "");
        token = preferences.getString("token", "");
        DeviceInfo = preferences.getString("DeviceInfo", "");
        arrow_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        UserSubmit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount=et_EnterAmount.getText().toString().trim();
                bankrefno=et_EnterRefereshNumber.getText().toString().trim();
                comment=et_EnterRemark.getText().toString().trim();
                if (amount.equalsIgnoreCase("")){
                    et_EnterAmount.setError("Required");
                }else if (bankrefno.equalsIgnoreCase("")){
                    et_EnterRefereshNumber.setError("Required");
                }else if (comment.equalsIgnoreCase("")){
                    et_EnterRemark.setError("Required");
                }else if (paymentmode.equalsIgnoreCase("select")){
                    AlertDialog.Builder dialog= new AlertDialog.Builder(FundRequest.this);
                    dialog.setMessage("Please Select Paymentmode :");
                    final AlertDialog alertDialog=dialog.create();
                    alertDialog.show();
                } else{
                    paymentrequest();
                }
            }
        });
    }
    private void paymentrequest() {
        ProgressDialog progressDialog = new ProgressDialog(FundRequest.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait while execution...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<JsonObject> call= RetrofitClient.getInstance().getApi().paymentrequest(KING,userid,comment,amount,bankrefno,paymentmode,token,DeviceInfo);
         call.enqueue(new Callback<JsonObject>() {
             @Override
             public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                 if (response.isSuccessful()){
                     try {
                         JSONObject responseObject=new JSONObject(String.valueOf(response.body()));
                         String responseCode=responseObject.getString("response_code");
                         if (responseCode.equalsIgnoreCase("TXN")){
                             String transaction=responseObject.getString("transactions");
//                             Names=new ArrayList<>();
//                             Names.add("select")
                             androidx.appcompat.app.AlertDialog.Builder builder=new androidx.appcompat.app.AlertDialog.Builder(FundRequest.this);
                             builder.setTitle("Status");
                             builder.setMessage(transaction);
                             builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                 @Override
                                 public void onClick(DialogInterface dialog, int which) {
                                     Intent intent=new Intent(FundRequest.this,MainActivity.class);
                                     startActivity(intent);
                                 }
                             }).show();
                             progressDialog.dismiss();
                         }else if (responseCode.equalsIgnoreCase("ERR")){
                             String message=responseObject.getString("response_msg");
                             progressDialog.dismiss();
                             new AlertDialog.Builder(FundRequest.this)
                                     .setMessage(message)
                                     .setTitle("Status")
                                     .show();
                         }else{
                             progressDialog.dismiss();
                             new AlertDialog.Builder(FundRequest.this)
                                     .setTitle("Status")
                                     .setMessage("something went wrong...")
                                     .show();
                         }
                     } catch (JSONException e) {
                         e.printStackTrace();
                         progressDialog.dismiss();
                         new AlertDialog.Builder(FundRequest.this)
                                 .setTitle("Status")
                                 .setMessage("something went wrong...")
                                 .show();                     }
                 }
             }
             @Override
             public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                 new AlertDialog.Builder(FundRequest.this)
                         .setTitle("Status")
                         .setMessage("something went wrong...")
                         .show();
                 }
         });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        paymentmode=names[position];
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}