package com.example.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.Models.Cart;
import com.example.bon_apetit.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private static final String TAG="Recycler View";
    private ArrayList<Cart> icart= new ArrayList<>();
    //Float Total;
    Context context;

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView receipename,noofservings,price;
        Button delete;
        ImageView recPIC;




        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            receipename = itemView.findViewById(R.id.receipName);
            noofservings = itemView.findViewById(R.id.txtservings);
            price = itemView.findViewById(R.id.txtprice);
            delete=itemView.findViewById(R.id.btndelete);
            recPIC=itemView.findViewById(R.id.reciepImage);


        }


    }

    public CartAdapter(Context context,ArrayList<Cart> data){
        this.context=context;
        this.icart=data;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.mycart_cardview,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {

        Log.d(TAG,"On bind view holder");
        final Cart cart= icart.get(position);
        holder.receipename.setText(icart.get(position).getReceipeName());
        holder.noofservings.setText(icart.get(position).getServings());
        holder.price.setText(icart.get(position).getPrice().toString());
        //Total=Total+icart.get(position).getPrice();

        Picasso.get().load(cart.getImageUri()).placeholder(R.mipmap.ic_launcher).fit().centerCrop().into(holder.recPIC);

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                icart.remove(position);

                notifyDataSetChanged();

            }
        });

        if(icart.isEmpty()){
            Toast.makeText(context,"Nothing to display",Toast.LENGTH_LONG).show();

        }

    }

    @Override
    public int getItemCount() {
        return icart.size();
    }



}
