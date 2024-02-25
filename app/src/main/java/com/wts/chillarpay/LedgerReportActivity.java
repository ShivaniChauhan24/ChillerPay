package com.wts.chillarpay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import java.text.SimpleDateFormat;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.google.gson.JsonObject;
import com.wts.chillarpay.adapter.MyLedgerAdapter;
import com.wts.chillarpay.client.RetrofitClient;
import com.wts.chillarpay.model.MyLedgerModel;

import org.json.JSONArray;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.wts.chillarpay.client.RetrofitClient.KING;

public class LedgerReportActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
     ImageView arrow_image,from_calendar,to_calendar;
     CardView ledgerreport_cardview1,ledgerreport_cardview2;
     TextView from_textview,to_textview;
     Button ledgerreport_button;
     Spinner spinner;
     String[]name={"All","Credit","Debit"};
     RecyclerView ledgerreport_layout;
     DatePickerDialog datePickerDialog;
     SimpleDateFormat simpleDateFormat;
     String userid,token,DeviceInfo,searchby,from,to;
     ArrayList<MyLedgerModel> myLedgerModelArrayList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ledger_report);
        registered();
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd",Locale.US);
        ArrayAdapter arrayAdapter= new ArrayAdapter(this,R.layout.support_simple_spinner_dropdown_item,name);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userid = preferences.getString("userid", "");
        token = preferences.getString("token", "");
        DeviceInfo = preferences.getString("DeviceInfo", "");
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd");
        Date todayDate = new Date();
        from = currentDate.format(todayDate);
        to = currentDate.format(todayDate);
        from_textview.setText(from);
        to_textview.setText(to);
        getledger();
        arrow_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        ledgerreport_cardview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(LedgerReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar fromDate = Calendar.getInstance();
                        fromDate.set(year, month, dayOfMonth);
                        String date = simpleDateFormat.format(fromDate.getTime());
                        from_textview.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        ledgerreport_cardview2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(LedgerReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        Calendar fromDate = Calendar.getInstance();
                        fromDate.set(year, month, dayOfMonth);
                        String date = simpleDateFormat.format(fromDate.getTime());
                        to_textview.setText(date);
                    }
                },year,month,day);
                datePickerDialog.show();
            }
        });
        ledgerreport_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                from=from_textview.getText().toString().trim();
                to=to_textview.getText().toString().trim();
                if (from.equalsIgnoreCase("")){
                    AlertDialog.Builder dialog=new AlertDialog.Builder(LedgerReportActivity.this);
                    dialog.setMessage("Please Select From Date");
                    AlertDialog alertDialog=dialog.create();
                    alertDialog.show();
                }else if (to.equalsIgnoreCase("")){
                    AlertDialog.Builder dialog= new AlertDialog.Builder(LedgerReportActivity.this);
                    dialog.setMessage("Please Select To Date");
                    AlertDialog alertDialog=dialog.create();
                    alertDialog.show();
                }else{
                    getledger();
                }
            }
        });
    }
    private void getledger() {
        ProgressDialog progressDialog = new ProgressDialog(LedgerReportActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait while execution...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        Call<JsonObject> call= RetrofitClient.getInstance().getApi().getledger(KING,userid,token,DeviceInfo,"ALL",from,to);
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()){
                    try {
                        JSONObject responseObject=new JSONObject(String.valueOf(response.body()));
                        String responseCode=responseObject.getString("response_code");
                        if (responseCode.equalsIgnoreCase("TXN")){
                            JSONArray transactionArray=responseObject.getJSONArray("transactions");
                            myLedgerModelArrayList = new ArrayList<>();
                            for (int i=0;i<transactionArray.length();i++){
                                MyLedgerModel myLedgerModel = new MyLedgerModel();
                                JSONObject transactionObject=transactionArray.getJSONObject(i);
                                String old_bal=transactionObject.getString("old_bal");
                                String new_bal=transactionObject.getString("new_bal");
                                String transaction_type=transactionObject.getString("transaction_type");
                                String remarks=transactionObject.getString("remarks");
                                String transaction_date=transactionObject.getString("transaction_date");
                                String cr_dr_type=transactionObject.getString("cr_dr_type");
                                String amount=transactionObject.getString("amount");

                                myLedgerModel.setOld_bal(old_bal);
                                myLedgerModel.setNew_bal(new_bal);
                                myLedgerModel.setTransaction_type(transaction_type);
                                myLedgerModel.setRemarks(remarks);
                                myLedgerModel.setTransaction_date(transaction_date);
                                myLedgerModel.setCr_dr_type(cr_dr_type);
                                myLedgerModel.setAmount(amount);
                                myLedgerModelArrayList.add(myLedgerModel);
                            }
                            ledgerreport_layout.setLayoutManager(new LinearLayoutManager(LedgerReportActivity.this,RecyclerView.VERTICAL,false));
                            MyLedgerAdapter myLedgerAdapter= new MyLedgerAdapter(myLedgerModelArrayList);
                            ledgerreport_layout.setAdapter(myLedgerAdapter);
                            progressDialog.dismiss();
                        }else if (responseCode.equalsIgnoreCase("ERR")){
                            progressDialog.dismiss();
                            //Toast.makeText(LedgerReportActivity.this, "ledger failed", Toast.LENGTH_SHORT).show();
                        }else{
                            progressDialog.dismiss();
                            //Toast.makeText(LedgerReportActivity.this, "ledger failed", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                        //Toast.makeText(LedgerReportActivity.this, "ledger failed", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    progressDialog.dismiss();
                    //Toast.makeText(LedgerReportActivity.this, "leddger failed", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                //Toast.makeText(LedgerReportActivity.this, "ledger failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void registered() {
        ledgerreport_button=findViewById(R.id.ledgerreport_button);
        spinner=findViewById(R.id.spinner);
        arrow_image=findViewById(R.id.arrow_image);
        from_calendar=findViewById(R.id.from_calendar);
        to_calendar=findViewById(R.id.to_calendar);
        ledgerreport_cardview1=findViewById(R.id.ledgerreport_cardview1);
        ledgerreport_cardview2=findViewById(R.id.ledgerreport_cardview2);
        from_textview=findViewById(R.id.from_textview);
        to_textview=findViewById(R.id.to_textview);
        ledgerreport_layout=findViewById(R.id.ledgerreport_layout);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        searchby = name[position];
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}