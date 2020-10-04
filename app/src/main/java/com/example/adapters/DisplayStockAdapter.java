package com.example.adapters;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.Stock;
import com.example.bon_apetit.R;
import com.example.bon_apetit.stock_table;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class DisplayStockAdapter extends RecyclerView.Adapter<DisplayStockAdapter.ViewHolder> {
    private static final String TAG = "RecyclerView";
    private ArrayList<Stock> istock;
    private Context mContext;

    DatabaseReference dbreff = FirebaseDatabase.getInstance().getReference();

    public DisplayStockAdapter(Context mContext, ArrayList<Stock> data){
        this.mContext = mContext;
        this.istock = data;
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView sid,sname,sup;
        Button btndel;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            sid = itemView.findViewById(R.id.stockid);
            sname = itemView.findViewById(R.id.stockname);
            sup = itemView.findViewById(R.id.stockup);
            btndel = itemView.findViewById(R.id.btndeltm);

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
    public void onBindViewHolder(@NonNull final ViewHolder holder,final int position) {
        Log.d(TAG,"On bind view holder");
        holder.sid.setText(istock.get(position).getName());
        holder.sname.setText(String.valueOf(istock.get(position).getQty()+""+istock.get(position).getUnit()));
        holder.sup.setText(String.valueOf(istock.get(position).getUnitprice()));
        holder.btndel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               dbreff = FirebaseDatabase.getInstance().getReference().child("stock");
               dbreff.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        System.out.println(dataSnapshot);
                        if(dataSnapshot.hasChild(istock.get(position).getName())){
                            dbreff=FirebaseDatabase.getInstance().getReference().child("stock").child(istock.get(position).getName());
                            dbreff.removeValue();
                            istock.remove(position);
                            Intent intent = new Intent(mContext,stock_table.class);
                            mContext.startActivity(intent);
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                notifyDataSetChanged();
            }
        });

    }
    @Override
    public int getItemCount() {
        return istock.size();
    }
}
