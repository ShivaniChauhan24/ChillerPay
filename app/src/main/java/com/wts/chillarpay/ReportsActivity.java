package com.wts.chillarpay;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ReportsActivity extends AppCompatActivity {
    ImageView arrow_image;
    CardView Prepaid_cardview,Postpaid_cardview,dth_cardview,All_cardview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reports);
        registered();
    }
    private void registered() {
        arrow_image=findViewById(R.id.arrow_image);
        Prepaid_cardview=findViewById(R.id.Prepaid_cardview);
        Postpaid_cardview=findViewById(R.id.Postpaid_cardview);
        dth_cardview=findViewById(R.id.dth_cardview);
        All_cardview=findViewById(R.id.All_cardview);
        arrow_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        Prepaid_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReportsActivity.this,RechargeReport_Activity.class);
                intent.putExtra("title","Prepaid Report");
                intent.putExtra("searchBy","Mobile");
                startActivity(intent);
            }
        });
        Postpaid_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReportsActivity.this,RechargeReport_Activity.class);
                intent.putExtra("title","Postpaid Report");
                intent.putExtra("searchBy","Postpaid");
                startActivity(intent);
            }
        });
        dth_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReportsActivity.this,RechargeReport_Activity.class);
                intent.putExtra("title","DTH Report");
                intent.putExtra("searchBy","DTH");
                startActivity(intent);
            }
        });
        All_cardview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(ReportsActivity.this,RechargeReport_Activity.class);
                intent.putExtra("title","Report");
                intent.putExtra("searchBy","ALL");
                startActivity(intent);
            }
        });
    }
}