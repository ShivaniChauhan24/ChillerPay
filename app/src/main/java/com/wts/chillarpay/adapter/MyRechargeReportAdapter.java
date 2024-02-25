package com.wts.chillarpay.adapter;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wts.chillarpay.R;
import com.wts.chillarpay.model.MyRechargeReportModel;
import java.util.ArrayList;

public class MyRechargeReportAdapter extends RecyclerView.Adapter<MyRechargeReportAdapter.Viewholder> {

    ArrayList<MyRechargeReportModel> myRechargeReportModelArrayList;
    public MyRechargeReportAdapter(ArrayList<MyRechargeReportModel> myRechargeReportModelArrayList){
        this.myRechargeReportModelArrayList =  myRechargeReportModelArrayList;
    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.rechargereport_layout,parent,false);
         return new Viewholder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        String number=myRechargeReportModelArrayList.get(position).getNumber();
        String amount=myRechargeReportModelArrayList.get(position).getAmount();
        String comm=myRechargeReportModelArrayList.get(position).getComm();
        String cost=myRechargeReportModelArrayList.get(position).getCost();
        String balance=myRechargeReportModelArrayList.get(position).getBalance();
        String tdatetime=myRechargeReportModelArrayList.get(position).getTdatetime();
        String status=myRechargeReportModelArrayList.get(position).getStatus();
        String transactionid=myRechargeReportModelArrayList.get(position).getTransactionid();
        String opname=myRechargeReportModelArrayList.get(position).getOpname();

        holder.text_transactionID.setText(transactionid);
        holder.text_Opname.setText(opname);
        holder.text_number.setText(number);
        holder.amount.setText(amount);
        holder.text_commission.setText(comm);
        holder.text_cost.setText(cost);
        holder.text_balance.setText(balance);
        holder.text_datetime.setText(tdatetime);
        if (!status.equalsIgnoreCase("Success")){
            holder.text_status.setText(status);
            holder.text_status.setTextColor(Color.RED);
        }else {
            holder.text_status.setText(status);
            holder.text_status.setTextColor(Color.GREEN);
        }
    }
    @Override
    public int getItemCount() {
        return myRechargeReportModelArrayList.size();
    }
    public class Viewholder extends RecyclerView.ViewHolder {
        TextView text_transactionID,text_Opname,text_number,amount,text_commission,text_cost,text_balance,text_datetime,text_status;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            text_transactionID=itemView.findViewById(R.id.tv_transactionid);
            text_Opname=itemView.findViewById(R.id.tv_opname);
            text_number=itemView.findViewById(R.id.tv_number);
            amount=itemView.findViewById(R.id.tv_amount);
            text_commission=itemView.findViewById(R.id.tv_commission);
            text_cost=itemView.findViewById(R.id.tv_cost);
            text_balance=itemView.findViewById(R.id.tv_balance);
            text_datetime=itemView.findViewById(R.id.tv_dttime);
            text_status=itemView.findViewById(R.id.tv_failure);
        }
    }
}
