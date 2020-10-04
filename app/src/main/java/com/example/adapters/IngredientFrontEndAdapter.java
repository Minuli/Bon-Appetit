package com.example.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.Models.Ingredients;
import com.example.bon_apetit.DisplayIngredientsFrontEnd;
import com.example.bon_apetit.R;

import java.util.List;

public class IngredientFrontEndAdapter extends RecyclerView.Adapter<IngredientFrontEndAdapter.ImageViewHolder> {
    private List<Ingredients> ingredients;

    public IngredientFrontEndAdapter(DisplayIngredientsFrontEnd displayIngredientsFrontEnd, List<Ingredients> uploads) {
        ingredients = uploads;
    }
    @Override
    public IngredientFrontEndAdapter.ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.ingredient_item_frontend, parent, false);
        return new IngredientFrontEndAdapter.ImageViewHolder(v);
    }
    @Override
    public void onBindViewHolder(IngredientFrontEndAdapter.ImageViewHolder holder, int position) {
        Ingredients uploadIngredients = ingredients.get(position);
        holder.ingredients.setText(uploadIngredients.getIngredient());
        holder.amounts.setText(""+uploadIngredients.getAmount()+" "+uploadIngredients.getUnit());
        // holder.unit.setText(uploadIngredients.getUnit());
    }
    @Override
    public int getItemCount() {
        return ingredients.size();
    }
    public class ImageViewHolder extends RecyclerView.ViewHolder {
        public TextView ingredients, amounts, unit;

        public ImageViewHolder(View itemView) {
            super(itemView);
            ingredients = itemView.findViewById(R.id.ingredient);
            amounts = itemView.findViewById(R.id.amount);
            unit = itemView.findViewById(R.id.unit);
        }
    }
}
