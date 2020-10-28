package net.sunyounglee.bakingapp.ui;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.sunyounglee.bakingapp.R;
import net.sunyounglee.bakingapp.models.Ingredient;

import java.util.List;

public class IngredientRecyclerAdapter extends RecyclerView.Adapter<IngredientRecyclerAdapter.IngredientViewHoder> {
    private List<Ingredient> mIngredientList;

    @NonNull
    @Override
    public IngredientViewHoder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.ingredient_item, parent, false);
        return new IngredientViewHoder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientViewHoder holder, int position) {
        Ingredient ingredient = mIngredientList.get(position);
        holder.tvIngredient.setText(ingredient.getIngredient());
        holder.tvQuantity.setText(String.valueOf(ingredient.getQuantity()));
        holder.tvMeasure.setText(ingredient.getMeasure());
    }

    @Override
    public int getItemCount() {
        return mIngredientList == null ? 0 : mIngredientList.size();
    }

    public class IngredientViewHoder extends RecyclerView.ViewHolder {
        TextView tvIngredient;
        TextView tvQuantity;
        TextView tvMeasure;

        public IngredientViewHoder(@NonNull View itemView) {
            super(itemView);
            tvIngredient = itemView.findViewById(R.id.tv_ingredient);
            tvQuantity = itemView.findViewById(R.id.tv_quantity);
            tvMeasure = itemView.findViewById(R.id.tv_measure);

        }
    }

    public void setIngredientData(List<Ingredient> ingredient) {
        mIngredientList = ingredient;
        notifyDataSetChanged();

    }
}
