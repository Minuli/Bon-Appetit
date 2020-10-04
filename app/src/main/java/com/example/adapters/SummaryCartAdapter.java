package com.example.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.Models.Cart;
import com.example.bon_apetit.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SummaryCartAdapter extends RecyclerView.Adapter<SummaryCartAdapter.ViewHolder> {
    private static final String TAG="Recycler View";
    Context context;
    ArrayList<Cart> icart=new ArrayList<>();
    Cart cart;

    public void setAdapter(SummaryCartAdapter summaryCartAdapter) {
    }

    public class ViewHolder extends  RecyclerView.ViewHolder{

        TextView recName,price;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            recName=itemView.findViewById(R.id.receipName);
            price=itemView.findViewById(R.id.price);



        }
    }
    public SummaryCartAdapter(Context context, ArrayList<Cart> data){
        this.context=context;
        this.icart=data;

    }
    @NonNull
    @Override
    public SummaryCartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.summary_cartview,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull SummaryCartAdapter.ViewHolder holder, int position) {
        cart = new Cart();
        Log.d(TAG,"On bind view holder");
        //cart=icart.get(position);
        holder.recName.setText(icart.get(position).getReceipeName());
        System.out.println("R NAME IS : "+icart.get(position).getReceipeName());
        holder.price.setText(icart.get(position).getPrice().toString());



    }

    @Override
    public int getItemCount() {
        return icart.size();
    }
}
