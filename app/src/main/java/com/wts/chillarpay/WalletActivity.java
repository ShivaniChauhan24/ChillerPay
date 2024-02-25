package com.wts.chillarpay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.jaredrummler.android.device.DeviceName;
import com.wts.chillarpay.client.RetrofitClient;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wts.chillarpay.client.RetrofitClient.KING;

public class WalletActivity extends AppCompatActivity {
     ImageView  wallet_vectorimage,mainwallet_image,aepswallet_image,mainrefresh_image,aepsrefresh_image;
     CardView cardview1,cardview2;
     TextView mainwallet_tv,aepswallet_tv;
     String userid,token,DeviceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet);
        registerd();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userid = preferences.getString("userid", "");
        token = preferences.getString("token", "");
        DeviceInfo = preferences.getString("DeviceInfo", "");
        getBalance();
        mainrefresh_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBalance();
            }
        });
    }
    private void registerd() {
        wallet_vectorimage=findViewById(R.id.wallet_vectorimage);
        mainwallet_image=findViewById(R.id.mainwallet_image);
        aepswallet_image=findViewById(R.id.aepswallet_image);
        mainrefresh_image=findViewById(R.id.mainrefresh_image);
        aepsrefresh_image=findViewById(R.id.aepsrefresh_image);
        cardview1=findViewById(R.id.cardview1);
        cardview2=findViewById(R.id.cardview2);
        mainwallet_tv=findViewById(R.id.mainwallet_tv);
        aepswallet_tv=findViewById(R.id.aepswallet_tv);
    }
    private void getBalance() {
        ProgressDialog progressDialog = new ProgressDialog(WalletActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait while execution...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<JsonObject> call= RetrofitClient.getInstance().getApi().getBalance(KING,userid,token,DeviceInfo);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
             if (response.isSuccessful()){
                 try {
                     JSONObject responseObject=new JSONObject(String.valueOf(response.body()));
                     String responseCode=responseObject.getString("response_code");
                      if (responseCode.equalsIgnoreCase("TXN")){
                          JSONArray transacetionArray = responseObject.getJSONArray("transactions");
                          JSONObject transactionObject = transacetionArray.getJSONObject(0);
                          String balance = transactionObject.getString("balance");
                          mainwallet_tv.setText(balance);
                          progressDialog.dismiss();
                      }else if (responseCode.equalsIgnoreCase("ERR")){
                        progressDialog.dismiss();
                          mainwallet_tv.setText("00.0");
                      }else{
                          progressDialog.dismiss();
                          mainwallet_tv.setText("00.0");
                      }
                 } catch (JSONException e) {
                     e.printStackTrace();
                 }
             }else{
                 progressDialog.dismiss();
                 mainwallet_tv.setText("00.0");
             }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                 progressDialog.dismiss();
                mainwallet_tv.setText("00.0");
            }
        });
    }
}