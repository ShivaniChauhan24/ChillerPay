package com.wts.chillarpay;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
public class LoginActivity extends AppCompatActivity {

    EditText etUserName, etPassword;
    Button btnLogin;
    TextView forget_password;
    String username, password,token,DeviceInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        registerViews();
        forget_password = findViewById(R.id.forget_password);
        token = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                username = etUserName.getText().toString().trim();
                password = etPassword.getText().toString().trim();
                if (username.equalsIgnoreCase("")) {
                    etUserName.setError("Required");
                } else if (password.equalsIgnoreCase("")) {
                    etPassword.setError("Required");
                } else {
                    DeviceName.init(LoginActivity.this);
                    DeviceName.with(LoginActivity.this).request((info, error) -> {
                        String manufacturer = info.manufacturer;
                        String name = info.marketName;
                        String model = info.model;
                        String codename = info.codename;
                        String deviceName = info.getName();
                        DeviceInfo = manufacturer + name + model + codename + deviceName;
                        Login();
                    });
                }
            }
        });
        }
    private void Login() {
        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait while execution...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<JsonObject> callss = RetrofitClient.getInstance().getApi().Login(KING,
                username,password,token,DeviceInfo,"APP");
        callss.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject responseObject = new JSONObject(String.valueOf(response.body()));
                        String responseCode = responseObject.getString("response_code");
                        if (responseCode.equalsIgnoreCase("TXN")) {
                            JSONArray transactionArray = responseObject.getJSONArray("transactions");
                            JSONObject transactionObject = transactionArray.getJSONObject(0);
                            String userid = transactionObject.getString("userid");
                            String ownername = transactionObject.getString("ownername");
                            String usernamess = transactionObject.getString("username");
                            String rusertype = transactionObject.getString("usertype");
                            String mobileno = transactionObject.getString("mobileno");
                            String rpancard = transactionObject.getString("pancard");
                            String dpimg = transactionObject.getString("dpimg");
                            String raadharcard = transactionObject.getString("aadharcard");
                            String rOutletId = transactionObject.getString("OutletId");
                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);//database created
                            SharedPreferences.Editor editor = sharedPreferences.edit();//edit the databse
                            editor.putString("userid", userid);
                            editor.putString("ownername", ownername);
                            editor.putString("username", usernamess);
                            editor.putString("usertype", rusertype);
                            editor.putString("mobileno", mobileno);
                            editor.putString("pancard", rpancard);
                            editor.putString("dpimg", dpimg);
                            editor.putString("aadharcard", raadharcard);
                            editor.putString("OutletId", rOutletId);
                            editor.putString("DeviceInfo", DeviceInfo);
                            editor.putString("token", token);
                            editor.putString("u", username);
                            editor.putString("p", password);
                            editor.apply();
                            startActivity(intent);
                            finish();
                            progressDialog.dismiss();
                        } else if (responseCode.equalsIgnoreCase("ERR")) {
                            String message=responseObject.getString("response_msg");
                            progressDialog.dismiss();
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setMessage(message)
                                    .setTitle("Login Failed")
                                    .show();
                        } else {
                            progressDialog.dismiss();
                            new AlertDialog.Builder(LoginActivity.this)
                                    .setTitle("Login Failed")
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        new AlertDialog.Builder(LoginActivity.this)
                                .setTitle("Login Failed")
                                .show();
                    }
                } else {progressDialog.dismiss();
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Login Failed")
                            .show();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                new AlertDialog.Builder(LoginActivity.this)
                        .setTitle("Login Failed")
                        .setMessage(t.getMessage())
                        .show();
            }
        });
    }
    private void registerViews() {
        etUserName = findViewById(R.id.et_username);
        etPassword = findViewById(R.id.et_password);
        btnLogin = findViewById(R.id.btn_login);
    }
}