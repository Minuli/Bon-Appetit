package com.example.adapters;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.CustomerPost;
import com.example.bon_apetit.R;
import com.squareup.picasso.Picasso;

import java.util.List;


public class profileFeedAdapter extends RecyclerView.Adapter<profileFeedAdapter.ImageViewHolder> {
    private Context pContext;
    private List<CustomerPost> lUploads;
    private OnItemClickListener plistner;

    public profileFeedAdapter(Context context, List<CustomerPost> uploads) {
        pContext = context;
        lUploads = uploads;
    }
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(pContext).inflate(R.layout.newcardviewprofile, parent, false);
        return new ImageViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        CustomerPost uploadCurrent = lUploads.get(position);
        holder.textViewName.setText(uploadCurrent.getRecipeName());
        holder.ingredients.setText(uploadCurrent.getIngredients());
        holder.method.setText(uploadCurrent.getMethod());
        Picasso.get()
                .load(uploadCurrent.getImgUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }
    @Override
    public int getItemCount() {
        return lUploads.size();
    }

    public class ImageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {
        public TextView textViewName,ingredients,method;
        public ImageView imageView;
        public ImageViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.recipiename);
            imageView = itemView.findViewById(R.id.image_food);
            ingredients= itemView.findViewById(R.id.ingredients);
            method =itemView.findViewById(R.id.method);

            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View view) {
            if(plistner != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){
                    plistner.onItemClick(position);
                }
            }
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("Select Action");
            MenuItem dowhat = contextMenu.add(Menu.NONE, 2, 2 ,"Do what ever");

            MenuItem delete = contextMenu.add(Menu.NONE, 1, 1 ,"Delete Post");

            dowhat.setOnMenuItemClickListener(this);
            delete.setOnMenuItemClickListener(this);
        }

        @Override
        public boolean onMenuItemClick(MenuItem menuItem) {

            if(plistner != null){
                int position = getAdapterPosition();
                if(position != RecyclerView.NO_POSITION){


                    switch (menuItem.getItemId()) {
                        case 1:
                            plistner.onWhatEverClick(position);
                            return true;
                        case 2:
                            plistner.onDeleteClick(position);
                            return true;

                }
                    }
                }

            return false;
        }
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
        void onWhatEverClick(int position);
        void onDeleteClick(int position);
    }
   public void setOnItemClickListner(OnItemClickListener listner){
        plistner = listner;
   }
}