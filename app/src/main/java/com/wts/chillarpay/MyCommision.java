package com.wts.chillarpay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wts.chillarpay.adapter.MyCommissionAdapter;
import com.wts.chillarpay.adapter.MyCreditDebitAdopter;
import com.wts.chillarpay.client.RetrofitClient;
import com.wts.chillarpay.model.MyCommissionModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wts.chillarpay.client.RetrofitClient.KING;

public class MyCommision extends AppCompatActivity {
    RecyclerView recycler_Layout;
    String userid,token,DeviceInfo;
    ImageView arrow_image;
    ArrayList<MyCommissionModel> myCommissionModelArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_commision);
        recycler_Layout=findViewById(R.id.recycler_Layout);
        arrow_image=findViewById(R.id.arrow_image);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userid = preferences.getString("userid", "");
        token =preferences.getString("token","");
        DeviceInfo =preferences.getString("DeviceInfo","");
        getcommision();
        arrow_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void getcommision() {
        ProgressDialog progressDialog = new ProgressDialog(MyCommision.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait while execution...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<JsonObject> call= RetrofitClient.getInstance().getApi().getcommision(KING,userid,token,DeviceInfo);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
             if (response.isSuccessful()){
                 try {
                     JSONObject responseObject=new JSONObject(String.valueOf(response.body()));
                     String responseCode=responseObject.getString("response_code");
                     if (responseCode.equalsIgnoreCase("TXN")){
                         JSONArray transactionArray=responseObject.getJSONArray("transactions");
                         myCommissionModelArrayList=new ArrayList<>();
                         for (int i=0;i<transactionArray.length();i++)
                         {
                             MyCommissionModel myCommissionModel=new MyCommissionModel();
                             JSONObject transactionObject=transactionArray.getJSONObject(i);
                             String Operator=transactionObject.getString("Operator");
                             String CommPer=transactionObject.getString("CommPer");
                             String ChargePer=transactionObject.getString("ChargePer");
                             myCommissionModel.setOperator(Operator);
                             myCommissionModel.setCommPer(CommPer);
                             myCommissionModel.setChargePer(ChargePer);
                             myCommissionModelArrayList.add(myCommissionModel);
                         }
                         recycler_Layout.setLayoutManager(new LinearLayoutManager(MyCommision.this,RecyclerView.VERTICAL,false));
                         MyCommissionAdapter myCommissionAdapter=new MyCommissionAdapter(myCommissionModelArrayList);
                         recycler_Layout.setAdapter(myCommissionAdapter);
                         progressDialog.dismiss();
                     }else if(responseCode.equalsIgnoreCase("ERR")){
                         String message=responseObject.getString("response_msg");
                         progressDialog.dismiss();
                         new AlertDialog.Builder(MyCommision.this)
                                 .setMessage(message)
                                 .setTitle("Status")
                                 .show();
                     }else{
                         progressDialog.dismiss();
                        // Toast.makeText(MyCommision.this, "Commision Failed", Toast.LENGTH_SHORT).show();
                         new AlertDialog.Builder(MyCommision.this)
                                 .setTitle("Status")
                                 .setMessage("Something went wrong....")
                                 .show();
                     }
                 } catch (JSONException e) {
                     e.printStackTrace();
                     progressDialog.dismiss();
                     //Toast.makeText(MyCommision.this, "Commission Failed", Toast.LENGTH_SHORT).show();
                     new AlertDialog.Builder(MyCommision.this)
                             .setTitle("Status")
                             .setMessage("Something went wrong....")
                             .show();
                 }
             }else{
                 progressDialog.dismiss();
                 //Toast.makeText(MyCommision.this, "Commission Failed", Toast.LENGTH_SHORT).show();
                 new AlertDialog.Builder(MyCommision.this)
                         .setTitle("Status")
                         .setMessage("Something went wrong....")
                         .show();
             }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
              progressDialog.dismiss();
                //Toast.makeText(MyCommision.this, t.getMessage()+"", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(MyCommision.this)
                        .setTitle("Status")
                        .setMessage("Something went wrong....")
                        .show();
            }
        });
    }
}