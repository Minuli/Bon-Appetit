package com.example.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.Models.Customer;
import com.example.Models.CustomerPost;
import com.example.bon_apetit.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;
import java.util.List;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ImageViewHolder> {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference1;


    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView recipie,likes,showmore,personName;
        public ImageView imageView;
        public ImageView propic;
        public Button heart;
        public ImageViewHolder(View itemView) {
            super(itemView);
            recipie= itemView.findViewById(R.id.recipeName);
            imageView = itemView.findViewById(R.id.image_food);
            propic = itemView.findViewById(R.id.image);
            likes = itemView.findViewById(R.id.likeCount);
            personName = itemView.findViewById(R.id.cusName);
            showmore = itemView.findViewById(R.id.showmore);
            heart = itemView.findViewById(R.id.likebtn);
        }
    }
    private Context pContext;
    private ArrayList<CustomerPost> lUploads;
    private ArrayList<Customer> cusdetails;



    public FeedAdapter(Context context, ArrayList<CustomerPost> uploads) {
        pContext = context;
        lUploads = uploads;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(pContext).inflate(R.layout.cardviewfeed, parent, false);
        return new ImageViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        CustomerPost uploadCurrent = lUploads.get(position);
        holder.recipie.setText(uploadCurrent.getRecipeName());
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

    private void  publisherinfo(final ImageView imageView ,final TextView personName ,final String uid ){
        DatabaseReference reference =FirebaseDatabase.getInstance().getReference("Customer").child(firebaseAuth.getCurrentUser().getUid());
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Customer cus =dataSnapshot.getValue(Customer.class);
                personName.setText(cus.getName());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}

