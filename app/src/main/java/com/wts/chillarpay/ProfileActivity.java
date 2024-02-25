package com.wts.chillarpay;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    TextView ownerNameTT,userNameTT,UsertypeTT,mobileNumberTT,pancardTT,adharcardTT,outlediD;
    String ownername,username,usertype,mobileno,pancard,aadharcard,OutletId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        //useridTT=findViewById(R.id.useridTT);
        ownerNameTT = findViewById(R.id.ownerNameTT);
        userNameTT = findViewById(R.id.userNameTT);
        UsertypeTT = findViewById(R.id.UsertypeTT);
        mobileNumberTT = findViewById(R.id.mobileNumberTT);
        pancardTT = findViewById(R.id.pancardTT);
        adharcardTT = findViewById(R.id.adharcardTT);
        outlediD = findViewById(R.id.outlediD);


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
       // userid = preferences.getString("userid", "");
        ownername = preferences.getString("ownername", "");
        username = preferences.getString("username", "");
        usertype = preferences.getString("usertype", "");
        mobileno = preferences.getString("mobileno", "");
        pancard = preferences.getString("pancard", "");
        aadharcard = preferences.getString("aadharcard", "");
        OutletId = preferences.getString("OutletId", "");

        //useridTT.setText(userid);
        ownerNameTT.setText(ownername);
        userNameTT.setText(username);
        UsertypeTT.setText(usertype);
        mobileNumberTT.setText(mobileno);
        pancardTT.setText(pancard);
        adharcardTT.setText(aadharcard);
        outlediD.setText(OutletId);
    }
}