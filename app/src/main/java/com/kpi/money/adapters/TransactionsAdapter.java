package com.kpi.money.adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.kpi.money.R;
import com.kpi.money.model.Transactions;

import java.util.List;


public class TransactionsAdapter extends RecyclerView.Adapter<TransactionsAdapter.ViewHolder>{

    private Context context;
    private List<Transactions> listItem;
    String PrevDate;

    public TransactionsAdapter(Context context, List<Transactions> listItem) {
        this.context = context;
        this.listItem = listItem;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_transactions_list,parent,false);

        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {


        final Transactions transaction = listItem.get(position);

        String date = transaction.getTnDate();
        String TnType = transaction.getTnType();
        String status = transaction.getStatus();

        if(position == 0){

            PrevDate = "empty";

        }else{

            PrevDate = listItem.get(position - 1).getTnDate();
        }

        holder.tnName.setText(transaction.getTnName());
        holder.tncat.setText("#id : " + transaction.getTnId());

        if (date.intern() != PrevDate.intern()){

            holder.date.setText(date);
            holder.date.setVisibility(View.VISIBLE);
        }

        if (TnType.equals("cr")){

            holder.amount.setText("+ " + transaction.getAmount());
            holder.amount.setTextColor(context.getResources().getColor(R.color.green));

        }else if(TnType.equals("db")){

            holder.amount.setText("- " + transaction.getAmount());
            holder.amount.setTextColor(context.getResources().getColor(R.color.red));
        }

        if(status.equals("0")){

            ((GradientDrawable)holder.statusName.getBackground()).setColor(context.getResources().getColor(R.color.yellow));
            holder.statusName.setText(context.getResources().getString(R.string.pending));

        }else if(status.equals("2")){

            ((GradientDrawable)holder.statusName.getBackground()).setColor(context.getResources().getColor(R.color.yellow));
            holder.statusName.setText(context.getResources().getString(R.string.processing));

        }else if(status.equals("1")){

            ((GradientDrawable)holder.statusName.getBackground()).setColor(context.getResources().getColor(R.color.green_light));
            holder.statusName.setText(context.getResources().getString(R.string.success));

        }else if(status.equals("3")){

            ((GradientDrawable)holder.statusName.getBackground()).setColor(context.getResources().getColor(R.color.red));
            holder.statusName.setText(context.getResources().getString(R.string.rejected));

        }else {

            holder.statusName.setText(context.getResources().getString(R.string.app_currency));
        }

    }

    @Override
    public int getItemCount() {
        return listItem.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView date,tnName,tncat,amount,statusName;
        ImageView image;
        LinearLayout SingleItem;
        public ViewHolder(View itemView) {
            super(itemView);

            date = itemView.findViewById(R.id.date);
            tnName = itemView.findViewById(R.id.tnName);
            tncat = itemView.findViewById(R.id.tnType);
            amount = itemView.findViewById(R.id.amount);
            statusName = itemView.findViewById(R.id.statusName);
            image = itemView.findViewById(R.id.image);
            SingleItem = itemView.findViewById(R.id.SingleItem);
        }
    }
}
