package com.wts.chillarpay.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wts.chillarpay.R;
import com.wts.chillarpay.model.MyCreditDebitModel;

import java.util.ArrayList;

public class MyCreditDebitAdopter extends RecyclerView.Adapter<MyCreditDebitAdopter.Viewholder> {

    ArrayList<MyCreditDebitModel> myCreditDebitAdopterArrayList;
     public  MyCreditDebitAdopter(ArrayList<MyCreditDebitModel> myCreditDebitModelArrayList) {
        this.myCreditDebitAdopterArrayList = myCreditDebitModelArrayList;
    }
    @NonNull
    @Override
    public MyCreditDebitAdopter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.report_recycle_layout,parent,false);
        return new Viewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull MyCreditDebitAdopter.Viewholder holder, int position) {
        String DrUser=myCreditDebitAdopterArrayList.get(position).getDrUser();
        String CrUser=myCreditDebitAdopterArrayList.get(position).getCrUser();
        String id=myCreditDebitAdopterArrayList.get(position).getId();
        String Amount=myCreditDebitAdopterArrayList.get(position).getAmount();
        String PaymentType=myCreditDebitAdopterArrayList.get(position).getPaymentType();
        String PaymentDate=myCreditDebitAdopterArrayList.get(position).getPaymentDate();
        String Remarks=myCreditDebitAdopterArrayList.get(position).getRemarks();

        holder.text_druser.setText(DrUser);
        holder.text_cruser.setText(CrUser);
        holder.text_id.setText(id);
        holder.text_amount.setText(Amount);
        holder.text_paymenttype.setText(PaymentType);
        holder.text_paymentdate.setText(PaymentDate);
        holder.text_remark.setText(Remarks);
    }
    @Override
    public int getItemCount() {
        return myCreditDebitAdopterArrayList.size();
    }
    public class Viewholder extends  RecyclerView.ViewHolder{
         TextView text_druser,text_cruser,text_id,text_amount,text_paymenttype,text_paymentdate,text_remark;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            text_druser=itemView.findViewById(R.id.tv_druser);
            text_cruser=itemView.findViewById(R.id.tv_cruser);
            text_id=itemView.findViewById(R.id.tv_id);
            text_amount=itemView.findViewById(R.id.tv_amount);
            text_paymenttype=itemView.findViewById(R.id.tv_paymenttype);
            text_paymentdate=itemView.findViewById(R.id.tv_paymentdate);
            text_remark=itemView.findViewById(R.id.tv_remark);
        }
    }
}
