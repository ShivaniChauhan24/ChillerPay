package com.wts.chillarpay.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wts.chillarpay.R;
import com.wts.chillarpay.model.ReportModel;

import java.util.ArrayList;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ViewHolder> {

    Context context;
    ArrayList<ReportModel> arrayList;

    public ReportAdapter(Context context, ArrayList<ReportModel> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.iteam_report2_layout,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.rechargeAmountTT.setText(arrayList.get(position).getAmount());
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView rechargeAmountTT;
        ImageView logo;
        TextView text_number;
        TextView text_apiname;
        TextView text_ID;
        TextView text_operatorID;
        TextView text_referenceNum;
        TextView text_Date_Time;
        Button btn_submit;
        Button btn_availableBal;
        Button btn_status;
        Button btn_dispute;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            rechargeAmountTT = itemView.findViewById(R.id.rechargeAmountTT);
            logo = itemView.findViewById(R.id.logo);
            text_number = itemView.findViewById(R.id.text_number);
            text_apiname = itemView.findViewById(R.id.text_apiname);
            text_ID = itemView.findViewById(R.id.text_ID);
            text_operatorID = itemView.findViewById(R.id.text_operatorID);
            text_referenceNum = itemView.findViewById(R.id.text_referenceNum);
            text_Date_Time = itemView.findViewById(R.id.text_Date_Time);
            btn_submit = itemView.findViewById(R.id.btn_submit);
            btn_availableBal = itemView.findViewById(R.id.btn_availableBal);
            btn_status = itemView.findViewById(R.id.btn_submit);
            btn_dispute = itemView.findViewById(R.id.btn_dispute);
        }
    }
}
