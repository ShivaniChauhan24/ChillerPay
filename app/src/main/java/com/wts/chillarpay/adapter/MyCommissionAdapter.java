package com.wts.chillarpay.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.wts.chillarpay.R;
import com.wts.chillarpay.model.MyCommissionModel;
import java.util.ArrayList;

public class MyCommissionAdapter extends RecyclerView.Adapter<MyCommissionAdapter.Viewholder> {

    ArrayList<MyCommissionModel> myCommissionModelArrayList;
    public MyCommissionAdapter(ArrayList<MyCommissionModel> myCommissionModelArrayList) {
        this.myCommissionModelArrayList = myCommissionModelArrayList;
    }
    @NonNull
    @Override
    public Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.recycle_layout,parent,false);
        return new Viewholder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Viewholder holder, int position) {
        String operatorName=myCommissionModelArrayList.get(position).getOperator();
        String chargePer=myCommissionModelArrayList.get(position).getChargePer();
        String commPer=myCommissionModelArrayList.get(position).getCommPer();

        holder.tvOperatorName.setText(operatorName);
        holder.tvChargePer.setText(chargePer);
        holder.tvCommPer.setText(commPer);
    }
    @Override
    public int getItemCount() {
        return myCommissionModelArrayList.size();
    }

    public class Viewholder extends RecyclerView.ViewHolder {
        TextView tvOperatorName,tvCommPer,tvChargePer;
        public Viewholder(@NonNull View itemView) {
            super(itemView);
            tvOperatorName=itemView.findViewById(R.id.text_Airtel);
            tvCommPer=itemView.findViewById(R.id.text1);
            tvChargePer=itemView.findViewById(R.id.text2);
        }
    }
}
