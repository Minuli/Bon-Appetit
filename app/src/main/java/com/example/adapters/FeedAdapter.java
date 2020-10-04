package com.example.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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

import java.util.ArrayList;

public class FeedAdapter extends RecyclerView.Adapter<FeedAdapter.ImageViewHolder> {

    FirebaseAuth firebaseAuth;
    FirebaseUser user;
    FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference1;

    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView recipie,likes,personName,ingredients,method;
        public ImageView imageView;
        public ImageView propic;
        public Button heart;
        public ImageView like;
        public ImageViewHolder(View itemView) {
            super(itemView);
            recipie= itemView.findViewById(R.id.recipeName);
            ingredients= itemView.findViewById(R.id.ingredients);
            method= itemView.findViewById(R.id.method);
            imageView = itemView.findViewById(R.id.image_food);
            propic = itemView.findViewById(R.id.customerImage);
            likes = itemView.findViewById(R.id.likeCount);
            personName = itemView.findViewById(R.id.cusName);
            like = itemView.findViewById(R.id.likebtn);

        }
    }

    private void isLikes(String postid, final ImageView imageView) {
        final FirebaseUser firebaseuser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("likes").child(postid);
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.child(firebaseuser.getUid()).exists()) {
                    imageView.setImageResource(R.drawable.ic_heart2);
                    imageView.setTag("liked");
                } else {
                    imageView.setImageResource(R.drawable.ic_heart1);
                    imageView.setTag("like");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
      private void numlikes(final TextView likes,String postid){
          DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("likes").child(postid);
          reference.addValueEventListener(new ValueEventListener() {
              @Override
              public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                  likes.setText(dataSnapshot.getChildrenCount()+"likes");
              }

              @Override
              public void onCancelled(@NonNull DatabaseError databaseError) {

              }
          });
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
    public void onBindViewHolder(final ImageViewHolder holder, int position) {
        final CustomerPost uploadCurrent = lUploads.get(position);
        holder.recipie.setText(uploadCurrent.getRecipeName());
        holder.ingredients.setText(uploadCurrent.getIngredients());
        holder.method.setText(uploadCurrent.getMethod());
        Picasso.get()
                .load(uploadCurrent.getImgUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);

        if (uploadCurrent.getCustomerImage() != null) {
            Picasso.get()
                    .load(uploadCurrent.getCustomerImage())
                    .placeholder(R.mipmap.ic_launcher)
                    .fit()
                    .centerCrop()
                    .into(holder.propic);
        }

        if (uploadCurrent.getCustomerName() != null){
            holder.personName.setText(uploadCurrent.getCustomerName());
        }

        //isLikes(uploadCurrent.getPostid(),holder.like);
        //numlikes(holder.likes,uploadCurrent.getPostid());

        holder.like.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.like.getTag().equals(("like"))){
                    FirebaseDatabase.getInstance().getReference().child("Like").child(uploadCurrent.getPostid()).child(firebaseAuth.getCurrentUser().getUid()).setValue(true);
                }else{
                    FirebaseDatabase.getInstance().getReference().child("likes").child(uploadCurrent.getPostid()).child(firebaseAuth.getCurrentUser().getUid()).removeValue();
                }

            }
        });




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

