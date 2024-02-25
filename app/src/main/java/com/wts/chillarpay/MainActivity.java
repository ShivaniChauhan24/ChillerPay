package com.wts.chillarpay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.constants.ScaleTypes;
import com.denzcoskun.imageslider.models.SlideModel;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;
import com.wts.chillarpay.client.RetrofitClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wts.chillarpay.client.RetrofitClient.KING;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    LinearLayout prepaidLY, postpaidLY, dth_rechargeLY, landlineLY, electricityLy, broadbandLY, gasLY, aadhar_atmLY, datacardLY, commissionLY,wallet_ly;
    String userid, ownername, username, mobileno, dpimg, token, DeviceInfo,
            userStr, passStr;
    TextView tv_settings;
    EditText userNameET, passwordET;
    Button botton_CH, cancelBtn;
    String OldPassword, NewPassword;
    LinearLayout report_layout, historyLayout,profile_LY;
    DrawerLayout drawer;
    ImageSlider imageSlider;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        drawer=findViewById(R.id.drawer);
        NavigationView navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(MainActivity.this);
        View navView = navigationView.getHeaderView(0);
        TextView text_header = navView.findViewById(R.id.text_header);
        TextView numberTT = navView.findViewById(R.id.numberTT);
        register();
        setImageSlider();
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        ownername = preferences.getString("ownername", "");
        username = preferences.getString("username", "");
        mobileno = preferences.getString("mobileno", "");
        dpimg = preferences.getString("dpimg", "");
        userid = preferences.getString("userid", "");
        token = preferences.getString("token", "");
        DeviceInfo = preferences.getString("DeviceInfo", "");
        userStr = preferences.getString("u", "");
        passStr = preferences.getString("p", "");
        text_header.setText(ownername);
        numberTT.setText(mobileno);
        checkCredentialData();
        clickHandle();

    }
    private void checkCredentialData(){
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setMessage("Please wait...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<JsonObject> call = RetrofitClient.getInstance().getApi().checkCredential(KING,userStr,passStr,DeviceInfo);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                   try {
                       JSONObject jsonObject = new JSONObject(String.valueOf(response.body()));
                       String response_code = jsonObject.getString("response_code");
                       if (response_code.equalsIgnoreCase("TXN")){
                           SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);//database created
                           SharedPreferences.Editor editor = sharedPreferences.edit();//edit the databse
                           editor.putString("userid", userid);
                           editor.apply();
                           progressDialog.dismiss();
                       }else if (response_code.equalsIgnoreCase("ERR")){
                           progressDialog.dismiss();
                           AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                           alert.setTitle("Alert!!!");
                           alert.setMessage("Recently your ID login another device or may be password changed");
                           alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                                   startActivity(intent);
                                   SharedPreferences sharedPreferences =PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                   SharedPreferences.Editor editor=sharedPreferences.edit();
                                   editor.clear();
                                   editor.apply();
                                   finish();
                               }
                           });
                           alert.setCancelable(false);
                           alert.show();
                       }else {
                           progressDialog.dismiss();
                           AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                           alert.setTitle("Alert!!!");
                           alert.setMessage("Recently your ID login another device or may be password changed");
                           alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                               @Override
                               public void onClick(DialogInterface dialog, int which) {
                                   Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                                   startActivity(intent);
                                   SharedPreferences sharedPreferences =PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                                   SharedPreferences.Editor editor=sharedPreferences.edit();
                                   editor.clear();
                                   editor.apply();
                                   finish();
                               }
                           });
                           alert.setCancelable(false);
                           alert.show();
                       }
                   }catch (Exception e){
                       progressDialog.dismiss();
                   }
                }else {
                    progressDialog.dismiss();
                    AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                    alert.setTitle("Alert!!!");new android.app.AlertDialog.Builder(MainActivity.this)
                            .setMessage("Something went wrong..")
                            .setTitle("Status")
                            .show();
                    alert.setMessage("Recently your ID login another device or may be password changed");
                    alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                            SharedPreferences sharedPreferences =PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                            SharedPreferences.Editor editor=sharedPreferences.edit();
                            editor.clear();
                            editor.apply();
                            finish();
                        }
                    });
                    alert.setCancelable(false);
                    alert.show();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                AlertDialog.Builder alert = new AlertDialog.Builder(MainActivity.this);
                alert.setTitle("Alert!!!");
                alert.setMessage("Recently your ID login another device or may be password changed");
                alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                      Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                      startActivity(intent);
                        SharedPreferences sharedPreferences =PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
                        SharedPreferences.Editor editor=sharedPreferences.edit();
                        editor.clear();
                        editor.apply();
                        finish();
                    }
                });
                alert.setCancelable(false);
                alert.show();
            }
        });
    }
    private void setImageSlider() {
            ArrayList<SlideModel> mySliderList = new ArrayList<>();
            mySliderList.add(new SlideModel(R.drawable.slider1, ScaleTypes.FIT));
            mySliderList.add(new SlideModel(R.drawable.slider2, ScaleTypes.FIT));
            mySliderList.add(new SlideModel(R.drawable.slider3, ScaleTypes.FIT));
            imageSlider.setImageList(mySliderList, ScaleTypes.FIT);
        }
    private void clickHandle() {
        prepaidLY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Recharge_Activity.class);
                intent.putExtra("service","Mobile");
                intent.putExtra("key","1");
                startActivity(intent);
            }//put extra--one page ka data dusre page pe bhejna.
        });
        postpaidLY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Recharge_Activity.class);
                intent.putExtra("service","Postpaid");
                intent.putExtra("key","2");
                startActivity(intent);
            }
        });
        dth_rechargeLY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Recharge_Activity.class);
                intent.putExtra("service","DTH");
                intent.putExtra("key","3");
                startActivity(intent);
            }
        });
        tv_settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });
        commissionLY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, MyCommision.class);
                startActivity(intent);
            }
        });
        wallet_ly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,WalletActivity.class);
                startActivity(intent);
            }
        });
        historyLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ReportsActivity.class);
                startActivity(intent);
            }
        });
        profile_LY.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
                startActivity(intent);
            }
        });
        report_layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,LedgerReportActivity.class);
                startActivity(intent);
            }
        });
    }
    private void changepassword() {
        ProgressDialog progressDialog = new ProgressDialog(MainActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait while execution...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<JsonObject> call = RetrofitClient.getInstance().getApi().changepassword(KING, userid, token, DeviceInfo, OldPassword, NewPassword);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject responseObject = new JSONObject(String.valueOf(response.body()));
                        String responseCode = responseObject.getString("response_code");
                        if (responseCode.equalsIgnoreCase("TXN")) {
                         String response_code = responseObject.getString("transactions");
                            Toast.makeText(MainActivity.this, "proceed successful", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        } else if (responseCode.equalsIgnoreCase("ERR")) {
                            String message=responseObject.getString("response_msg");
                            progressDialog.dismiss();
                            new AlertDialog.Builder(MainActivity.this)
                                    .setMessage(message)
                                    .setTitle("Status")
                                    .show();
                        } else {
                            progressDialog.dismiss();
                            new AlertDialog.Builder(MainActivity.this)
                                    .setMessage("Something went wrong...")
                                    .setTitle("Status")
                                    .show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                       // Toast.makeText(MainActivity.this, "Proceed Failed", Toast.LENGTH_SHORT).show();
                        new AlertDialog.Builder(MainActivity.this)
                                .setMessage("Something went wrong...")
                                .setTitle("Status")
                                .show();
                    }
                } else {
                    progressDialog.dismiss();
                    //Toast.makeText(MainActivity.this, "Proceed Failed", Toast.LENGTH_SHORT).show();
                    new AlertDialog.Builder(MainActivity.this)
                            .setMessage("Something went wrong...")
                            .setTitle("Status")
                            .show();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                //Toast.makeText(MainActivity.this, "Proceed Failed", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("Something went wrong...")
                        .setTitle("Status")
                        .show();
            }
        });
    }
    private void register() {
        prepaidLY = findViewById(R.id.prepaidLY);
        postpaidLY = findViewById(R.id.postpaidLY);
        dth_rechargeLY = findViewById(R.id.dth_rechargeLY);
        landlineLY = findViewById(R.id.landlineLY);
        electricityLy = findViewById(R.id.electricityLy);
        broadbandLY = findViewById(R.id.broadbandLY);
        gasLY = findViewById(R.id.gasLY);
        aadhar_atmLY = findViewById(R.id.aadhar_atmLY);
        datacardLY = findViewById(R.id.datacardLY);
        tv_settings = findViewById(R.id.tv_settings);
        commissionLY = findViewById(R.id.commission_layout);
        wallet_ly = findViewById(R.id.wallet_ly);
        profile_LY = findViewById(R.id.profile_LY);
        imageSlider=findViewById(R.id.image_slider);
        report_layout=findViewById(R.id.report_layout);
        historyLayout =findViewById(R.id.creditbalance_layout);
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id=item.getItemId();
        if (id==R.id.nav_home){
            Toast.makeText(this, "Home", Toast.LENGTH_SHORT).show();
            drawer.closeDrawer(GravityCompat.START);
        }else if (id==R.id.nav_creditbalance){
            Intent intent=new Intent(MainActivity.this,CreditDebitActivity.class);
            intent.putExtra("key","1");
            startActivity(intent);
            //Toast.makeText(this, "CreditBalance", Toast.LENGTH_SHORT).show();
            drawer.closeDrawer(GravityCompat.START);
        }else if (id==R.id.nav_debitbalance){
            Intent intent=new Intent(MainActivity.this,CreditDebitActivity.class);
            intent.putExtra("key","2");
            startActivity(intent);
            //Toast.makeText(this, "DebitBalance", Toast.LENGTH_SHORT).show();
            drawer.closeDrawer(GravityCompat.START);
        }else if (id==R.id.nav_fundrequest) {
            Intent intent=new Intent(MainActivity.this,FundRequest.class);
            startActivity(intent);
            //Toast.makeText(this, "FundRequest", Toast.LENGTH_SHORT).show();
            drawer.closeDrawer(GravityCompat.START);
        }else if (id==R.id.nav_creditreport) {
            Intent intent=new Intent(MainActivity.this,CreditDebitReportActivity.class);
            intent.putExtra("key","1");
            startActivity(intent);
            //Toast.makeText(this, "CreditReport", Toast.LENGTH_SHORT).show();
            drawer.closeDrawer(GravityCompat.START);
        }else if (id==R.id.nav_debitreport) {
            Intent intent=new Intent(MainActivity.this,CreditDebitReportActivity.class);
            intent.putExtra("key","2");
            startActivity(intent);
            //Toast.makeText(this, "DebitReport", Toast.LENGTH_SHORT).show();
            drawer.closeDrawer(GravityCompat.START);
        }else if (id==R.id.nav_ledgerreport) {
            Intent intent=new Intent(MainActivity.this,LedgerReportActivity.class);
            startActivity(intent);
           // Toast.makeText(this, "LedgerReport", Toast.LENGTH_SHORT).show();
            drawer.closeDrawer(GravityCompat.START);
        }else if (id==R.id.nav_changepass) {
            View changePassView= LayoutInflater.from(MainActivity.this).inflate(R.layout.change_password_layout,null,false);
            AlertDialog changePassDialog=new AlertDialog.Builder(MainActivity.this).create();
            changePassDialog.setView(changePassView);
            changePassDialog.show();
            botton_CH=changePassView.findViewById(R.id.botton_CH);
            cancelBtn=changePassView.findViewById(R.id.cancelBtn);
            userNameET=changePassView.findViewById(R.id.userNameET);
            passwordET=changePassView.findViewById(R.id.passwordET);
            cancelBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   changePassDialog.dismiss();
                }
            });
            botton_CH.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OldPassword = userNameET.getText().toString().trim();
                    NewPassword = passwordET.getText().toString().trim();
                    if (OldPassword.equalsIgnoreCase("")){
                        userNameET.setError("Required");
                    }else if (NewPassword.equalsIgnoreCase("")){
                        passwordET.setError("Required");
                    }else{
                        changepassword();
                    }
                }
            });
            drawer.closeDrawer(GravityCompat.START);
        }else if (id==R.id.nav_logout) {
            SharedPreferences sharedPreferences =PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
            SharedPreferences.Editor editor=sharedPreferences.edit();
            editor.clear();
            editor.apply();
            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
            startActivity(intent);
            finish();
            drawer.closeDrawer(GravityCompat.START);
        }
        return false;
    }
}