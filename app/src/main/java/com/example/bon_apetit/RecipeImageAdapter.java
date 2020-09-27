package com.example.bon_apetit;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.Recipes;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecipeImageAdapter extends RecyclerView.Adapter<RecipeImageAdapter.ImageViewHolder> {
    private List<Recipes> recipe;

    public RecipeImageAdapter(HomepageActivityNew homepageActivityNew, List<Recipes> uploads) {
        recipe = uploads;
    }
    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recipe_image_item, parent, false);
        return new ImageViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ImageViewHolder holder, int position) {
        Recipes uploadRecipes = recipe.get(position);
        holder.recipeName.setText(uploadRecipes.getRecipeName());
        Picasso.get()
                .load(uploadRecipes.getImageUrl())
                .placeholder(R.mipmap.ic_launcher)
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }
    @Override
    public int getItemCount() {
        return recipe.size();
    }
    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView recipeName;
        public ImageView imageView;
        public ImageViewHolder(View itemView) {
            super(itemView);
            recipeName = itemView.findViewById(R.id.recipeName);
            imageView = itemView.findViewById(R.id.imageUpload);
        }
    }
}