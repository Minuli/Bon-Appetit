package com.example.adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.Recipes;
import com.example.bon_apetit.DisplayRecipeScrollFrontEndNew;
import com.example.bon_apetit.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeImageAdapter extends RecyclerView.Adapter<RecipeImageAdapter.ImageViewHolder> {
    private List<Recipes> recipe;
    private Context mContext;

    public RecipeImageAdapter(Context context, List<Recipes> uploads) {
        recipe = uploads;
        mContext = context;
    }
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_image_item, parent, false);
        return new ImageViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        Recipes uploadRecipes = recipe.get(position);
        holder.recipeName.setText(uploadRecipes.getRecipeName());
        Picasso.get()
                .load(uploadRecipes.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);

        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, DisplayRecipeScrollFrontEndNew.class);
                intent.putExtra("EnteredRecipe", recipe.get(position).getRecipeName());
                mContext.startActivity(intent);
            }
        });


    }
    @Override
    public int getItemCount() {
        return recipe.size();
    }
    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView recipeName;
        public ImageView imageView;
        LinearLayout parent;
        public ImageViewHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            imageView = itemView.findViewById(R.id.imageUpload);
            parent = itemView.findViewById(R.id.parent_layout);
        }
    }

}