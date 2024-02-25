package com.wts.chillarpay.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.wts.chillarpay.R;
import com.wts.chillarpay.model.MyLedgerModel;

import java.util.ArrayList;

public class MyLedgerAdapter extends RecyclerView.Adapter<MyLedgerAdapter.Viewholder> {

    ArrayList<MyLedgerModel>myLedgerModelArrayList;
    public MyLedgerAdapter(ArrayList<MyLedgerModel>myLedgerModelArrayList1){
        this.myLedgerModelArrayList=myLedgerModelArrayList1;
    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.ledgerreport_layout,parent,false);
        return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        String old_bal=myLedgerModelArrayList.get(position).getOld_bal();
        String new_bal=myLedgerModelArrayList.get(position).getNew_bal();
        String transaction_type=myLedgerModelArrayList.get(position).getTransaction_type();
        String remarks=myLedgerModelArrayList.get(position).getRemarks();
        String transaction_date=myLedgerModelArrayList.get(position).getTransaction_date();
        String cr_dr_type=myLedgerModelArrayList.get(position).getCr_dr_type();
        String amount=myLedgerModelArrayList.get(position).getAmount();

        holder.text_old_balance.setText(old_bal);
        holder.text_new_balance.setText(new_bal);
        holder.text_transaction_type.setText(transaction_type);
        holder.text_remarks.setText(remarks);
        holder.text_transaction_date.setText(transaction_date);
        holder.text_cr_dr_type.setText(cr_dr_type);
        holder.text_amount.setText(amount);
    }
    @Override
    public int getItemCount() {
        return myLedgerModelArrayList.size();
    }
    public class Viewholder extends RecyclerView.ViewHolder {
        TextView text_old_balance,text_new_balance,text_transaction_type,text_remarks,text_transaction_date,text_cr_dr_type,text_amount;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            text_old_balance=itemView.findViewById(R.id.tv_old_balance);
            text_new_balance=itemView.findViewById(R.id.tv_new_balance);
            text_transaction_type=itemView.findViewById(R.id.tv_transaction_type);
            text_remarks=itemView.findViewById(R.id.tv_remark);
            text_transaction_date=itemView.findViewById(R.id.tv_transaction_date);
            text_cr_dr_type=itemView.findViewById(R.id.text_crdr);
            text_amount=itemView.findViewById(R.id.tv_amount);
        }
    }
}
