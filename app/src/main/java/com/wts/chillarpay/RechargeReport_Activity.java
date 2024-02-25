package com.wts.chillarpay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.wts.chillarpay.adapter.MyRechargeReportAdapter;
import com.wts.chillarpay.client.RetrofitClient;
import com.wts.chillarpay.model.MyRechargeReportModel;
import org.json.JSONArray;
import org.json.JSONObject;
import java.lang.reflect.Array;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wts.chillarpay.client.RetrofitClient.KING;

public class RechargeReport_Activity extends AppCompatActivity {
    ImageView arrow_image,from_calendar,to_calendar;
    CardView report_cardview1,report_cardview2;
    TextView from_textview,to_textview;
    Button rechargereport_button;
    RecyclerView rechargereport_layout;
    DatePickerDialog datePickerDialog;
    String userid,token,DeviceInfo,searchby,from,to;
    ArrayList<MyRechargeReportModel> myRechargeReportModelArrayList;
    TextView tvTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recharge_report_);
        registerd();

        searchby=getIntent().getStringExtra("searchBy");
        String title;
        title=getIntent().getStringExtra("title");
        tvTitle.setText(title);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userid = preferences.getString("userid", "");
        token = preferences.getString("token", "");
        DeviceInfo = preferences.getString("DeviceInfo", "");

        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        Date todayDate = new Date();
        from = currentDate.format(todayDate);
        to = currentDate.format(todayDate);
        from_textview.setText(from);
        to_textview.setText(to);


        getreport();

        report_cardview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(RechargeReport_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar fromDate = Calendar.getInstance();
                        fromDate.set(year, month, dayOfMonth);
                        String date = currentDate.format(fromDate.getTime());
                        from_textview.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        report_cardview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog =  new DatePickerDialog(RechargeReport_Activity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar fromDate = Calendar.getInstance();
                        fromDate.set(year, month, dayOfMonth);
                        String date = currentDate.format(fromDate.getTime());
                        to_textview.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });

        arrow_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        rechargereport_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               from = from_textview.getText().toString().trim();
                to = to_textview.getText().toString().trim();
                if (from.equalsIgnoreCase("")){
                    AlertDialog.Builder dialog= new AlertDialog.Builder(RechargeReport_Activity.this);
                    dialog.setMessage("Please Select From Date");
                    AlertDialog alertDialog=dialog.create();
                    alertDialog.show();
                }else if (to.equalsIgnoreCase("")){
                    AlertDialog.Builder dialog= new AlertDialog.Builder(RechargeReport_Activity.this);
                    dialog.setMessage("Please Select To Date");
                    AlertDialog alertDialog=dialog.create();
                    alertDialog.show();
                }else{
                    getreport();
                }
            }
        });
    }

    private void getreport() {
        ProgressDialog progressDialog = new ProgressDialog(RechargeReport_Activity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait while execution...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<JsonObject> call= RetrofitClient.getInstance().getApi().getreport(KING,userid,token,DeviceInfo,searchby,from,to);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
              if (response.isSuccessful()){
                  try {
                      JSONObject responseObject=new JSONObject(String.valueOf(response.body()));
                      String responseCode=responseObject.getString("response_code");
                      if (responseCode.equalsIgnoreCase("TXN")){
                          JSONArray transactionArray=responseObject.getJSONArray("transactions");
                          myRechargeReportModelArrayList = new ArrayList<>();
                          for (int i=0;i<transactionArray.length();i++){
                              MyRechargeReportModel myRechargeReportModel = new MyRechargeReportModel();
                               JSONObject transactionObject=transactionArray.getJSONObject(i);
                              String responsenumber=transactionObject.getString("number");
                              String responseamount=transactionObject.getString("amount");
                              String responsecomm=transactionObject.getString("comm");
                              String responsecost=transactionObject.getString("cost");
                              String responsebalance=transactionObject.getString("balance");
                              String responsetdatetime=transactionObject.getString("tdatetime");
                              String responsestatus=transactionObject.getString("status");
                              String responsetransactionid=transactionObject.getString("transactionid");
                              String responseopname=transactionObject.getString("opname");

                              myRechargeReportModel.setTransactionid(responsetransactionid);
                              myRechargeReportModel.setOpname(responseopname);
                              myRechargeReportModel.setNumber(responsenumber);
                              myRechargeReportModel.setAmount(responseamount);
                              myRechargeReportModel.setComm(responsecomm);
                              myRechargeReportModel.setCost(responsecost);
                              myRechargeReportModel.setBalance(responsebalance);
                              myRechargeReportModel.setTdatetime(responsetdatetime);
                              myRechargeReportModel.setStatus(responsestatus);
                              myRechargeReportModelArrayList.add(myRechargeReportModel);
                          }
                          rechargereport_layout.setLayoutManager(new LinearLayoutManager(RechargeReport_Activity.this,RecyclerView.VERTICAL,false));
                          MyRechargeReportAdapter myRechargeReportAdapter=new MyRechargeReportAdapter(myRechargeReportModelArrayList);
                          rechargereport_layout.setAdapter(myRechargeReportAdapter);
                          progressDialog.dismiss();
                      }else if (responseCode.equalsIgnoreCase("ERR")){
                          progressDialog.dismiss();
                          //Toast.makeText(RechargeReport_Activity.this, "RechargeReport Failed", Toast.LENGTH_SHORT).show();
                      }else{
                          progressDialog.dismiss();
                          //Toast.makeText(RechargeReport_Activity.this, "RechargedReport Failed", Toast.LENGTH_SHORT).show();
                      }
                  } catch (Exception e) {
                      e.printStackTrace();
                  }
              }else{
                  progressDialog.dismiss();
                  //Toast.makeText(RechargeReport_Activity.this, "RechargedReport Failed", Toast.LENGTH_SHORT).show();
              }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                //Toast.makeText(RechargeReport_Activity.this, "RechargedReport Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void registerd() {
        arrow_image=findViewById(R.id.arrow_image);
        from_calendar=findViewById(R.id.from_calendar);
        to_calendar=findViewById(R.id.to_calendar);
        report_cardview1=findViewById(R.id.report_cardview1);
        report_cardview2=findViewById(R.id.report_cardview2);
        from_textview=findViewById(R.id.from_textview);
        to_textview=findViewById(R.id.to_textview);
        rechargereport_button=findViewById(R.id.rechargereport_button);
        rechargereport_layout=findViewById(R.id.rechargereport_layout);
        tvTitle=findViewById(R.id.toolbar_text);
    }
}