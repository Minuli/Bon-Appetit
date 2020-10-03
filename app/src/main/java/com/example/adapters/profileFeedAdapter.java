package com.example.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView textViewName;
        public ImageView imageView;
        public ImageViewHolder(View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.recipiename);
            imageView = itemView.findViewById(R.id.image_food);
        }
    }
}