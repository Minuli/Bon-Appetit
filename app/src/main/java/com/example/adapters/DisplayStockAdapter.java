package com.example.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.Stock;
import com.example.bon_apetit.R;

import java.util.ArrayList;

public class DisplayStockAdapter extends RecyclerView.Adapter<DisplayStockAdapter.ViewHolder> {
    private static final String TAG = "RecyclerView";
    private ArrayList<Stock> istock = new ArrayList<>();
    private Context mContext;

    public DisplayStockAdapter(Context mContext, ArrayList<Stock> data){
        this.mContext = mContext;
        this.istock = data;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView sid,sname,sup;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sid = itemView.findViewById(R.id.stockid);
            sname = itemView.findViewById(R.id.stockname);
            sup = itemView.findViewById(R.id.stockup);
        }
    }
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.loadstockdata,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder,final int position) {
        Log.d(TAG,"On bind view holder");
        holder.sid.setText(istock.get(position).getName());
        holder.sname.setText(istock.get(position).getQty());
        holder.sup.setText((int) istock.get(position).getUnitprice());

    }
    @Override
    public int getItemCount() {
        return istock.size();
    }
}
