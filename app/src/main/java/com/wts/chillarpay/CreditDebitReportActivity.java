package com.wts.chillarpay;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.JsonObject;
import com.wts.chillarpay.adapter.MyCreditDebitAdopter;
import com.wts.chillarpay.client.RetrofitClient;
import com.wts.chillarpay.model.MyCreditDebitModel;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;

import static android.app.PendingIntent.getActivity;
import static com.wts.chillarpay.client.RetrofitClient.KING;

public class CreditDebitReportActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ImageView arrow_image, from_calendar, to_calendar;
    CardView report_cardview1, report_cardview2;
    Spinner spinner;
    TextView from_textview, to_textview;
    DatePickerDialog datePickerDialog;
    Button report_button;
    RecyclerView report_recycle_layout;
    String userid, token, DeviceInfo, searchby = "DATE", from, to;
    String[] name = {"ALL", "DATE"};
    SimpleDateFormat simpleDateFormat;
    ArrayList<MyCreditDebitModel> myCreditDebitModelArrayList;
    private String key;
    private TextView title;
    Call<JsonObject> call;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_debit_report);
        registerd();
        key = getIntent().getStringExtra("key");
        title = findViewById(R.id.toolbar_text);
        if (key.equals("1")) {
            title.setText("Credit Report");
        } else {
            title.setText("Debit Report");
        }
        simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, name);
        spinner.setAdapter(arrayAdapter);
        spinner.setOnItemSelectedListener(this);
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        userid = preferences.getString("userid", "");
        token = preferences.getString("token", "");
        DeviceInfo = preferences.getString("DeviceInfo", "");
        SimpleDateFormat currentDate = new SimpleDateFormat("yyyy-MM-dd"); //by default current date
        Date todayDate = new Date();
        from = currentDate.format(todayDate);
        to = currentDate.format(todayDate);
        from_textview.setText(from);
        to_textview.setText(to);
        creditreport();
        report_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                from = from_textview.getText().toString().trim();
                to = to_textview.getText().toString().trim();
                if (from.equalsIgnoreCase("")) {
                    AlertDialog.Builder dialoge = new AlertDialog.Builder(CreditDebitReportActivity.this);
                    dialoge.setMessage("Please Select From Date");
                    AlertDialog alertDialog = dialoge.create();
                    alertDialog.show();
                } else if (to.equalsIgnoreCase("")) {
                    AlertDialog.Builder dialoge = new AlertDialog.Builder(CreditDebitReportActivity.this);
                    dialoge.setMessage("Please Select To Date");
                    AlertDialog alertDialog = dialoge.create();
                    alertDialog.show();
                } else {
                    creditreport();
                }
            }
        });
        report_cardview1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(CreditDebitReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        /*month=month+1;
                        String date=year+"/"+month+"/"+dayOfMonth;*/
                        Calendar fromDate = Calendar.getInstance();
                        fromDate.set(year, month, dayOfMonth);
                        String date = simpleDateFormat.format(fromDate.getTime());
                        from_textview.setText(date);
                    }
                }, year, month, day);
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
                datePickerDialog = new DatePickerDialog(CreditDebitReportActivity.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        /*month=month+1;
                        String date=year+"/"+month+"/"+dayOfMonth;*/
                        Calendar fromDate = Calendar.getInstance();
                        fromDate.set(year, month, dayOfMonth);
                        String date = simpleDateFormat.format(fromDate.getTime());
                        to_textview.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        //debitreport();
        arrow_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
    private void registerd() {
        arrow_image = findViewById(R.id.arrow_image);
        from_calendar = findViewById(R.id.from_calendar);
        from_textview = findViewById(R.id.from_textview);
        to_textview = findViewById(R.id.to_textview);
        to_calendar = findViewById(R.id.to_calendar);
        spinner = findViewById(R.id.spinner);
        report_cardview1 = findViewById(R.id.report_cardview1);
        report_cardview2 = findViewById(R.id.report_cardview2);
        report_button = findViewById(R.id.report_button);
        report_recycle_layout = findViewById(R.id.report_recycle_layout);
    }

    private void creditreport() {
        ProgressDialog progressDialog = new ProgressDialog(CreditDebitReportActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Please wait while execution...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        if (key.equals("1")) {
            call = RetrofitClient.getInstance().getApi().creditreport(KING, userid, token, DeviceInfo, searchby, from, to);
        } else {
            call = RetrofitClient.getInstance().getApi().debitreport(KING, userid, token, DeviceInfo, searchby, from, to);
        }
        call.enqueue(new Callback<JsonObject>() {
            @Override
            public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
                if (response.isSuccessful()) {
                    try {
                        JSONObject responseObject = new JSONObject(String.valueOf(response.body()));
                        String responseCode = responseObject.getString("response_code");
                        if (responseCode.equalsIgnoreCase("TXN")) {
                            JSONArray transactionArray = responseObject.getJSONArray("transactions");
                            myCreditDebitModelArrayList = new ArrayList<>();
                            for (int i = 0; i < transactionArray.length(); i++) {
                                MyCreditDebitModel myCreditDebitModel = new MyCreditDebitModel();
                                JSONObject transactionObject = transactionArray.getJSONObject(i);
                                String DrUser = transactionObject.getString("DrUser");
                                String CrUser = transactionObject.getString("CrUser");
                                String id = transactionObject.getString("id");
                                String Amount = transactionObject.getString("Amount");
                                String PaymentType = transactionObject.getString("PaymentType");
                                String PaymentDate = transactionObject.getString("PaymentDate");
                                String Remarks = transactionObject.getString("Remarks");
                                myCreditDebitModel.setDrUser(DrUser);
                                myCreditDebitModel.setCrUser(CrUser);
                                myCreditDebitModel.setId(id);
                                myCreditDebitModel.setAmount(Amount);
                                myCreditDebitModel.setPaymentType(PaymentType);
                                myCreditDebitModel.setPaymentDate(PaymentDate);
                                myCreditDebitModel.setRemarks(Remarks);
                                myCreditDebitModelArrayList.add(myCreditDebitModel);
                            }
                            report_recycle_layout.setLayoutManager(new LinearLayoutManager(CreditDebitReportActivity.this, RecyclerView.VERTICAL, false));
                            MyCreditDebitAdopter myCreditDebitAdopter = new MyCreditDebitAdopter(myCreditDebitModelArrayList);
                            report_recycle_layout.setAdapter(myCreditDebitAdopter);
                            progressDialog.dismiss();
                        } else if (responseCode.equalsIgnoreCase("ERR")) {
                            progressDialog.dismiss();
                        } else {
                            progressDialog.dismiss();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                        progressDialog.dismiss();
                    }
                } else {
                    progressDialog.dismiss();
}
            }
            @Override
            public void onFailure(Call<JsonObject> call, Throwable t) {
                progressDialog.dismiss();
                Toast.makeText(CreditDebitReportActivity.this, t.getMessage()+"", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        searchby = name[position];
    }
    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}