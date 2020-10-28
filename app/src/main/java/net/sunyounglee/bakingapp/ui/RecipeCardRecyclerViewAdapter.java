package net.sunyounglee.bakingapp.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.sunyounglee.bakingapp.R;
import net.sunyounglee.bakingapp.models.Recipe;

import java.util.List;

public class RecipeCardRecyclerViewAdapter extends
        RecyclerView.Adapter<RecipeCardRecyclerViewAdapter.RecipeCardViewHolder> {
    private List<Recipe> mRecipeList;
    private static final String TAG = RecipeCardRecyclerViewAdapter.class.getSimpleName();

    private final RecipeAdapterOnClickHandler mClickHandler;
    private Context context;

    /**
     * The interface that receives onClick messages.
     */
    public interface RecipeAdapterOnClickHandler {
        void onClick(Recipe recipe);
    }

    public RecipeCardRecyclerViewAdapter(RecipeAdapterOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public RecipeCardRecyclerViewAdapter.RecipeCardViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.recipe_card_item, parent, false);
        return new RecipeCardViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeCardRecyclerViewAdapter.RecipeCardViewHolder holder, int position) {
        Recipe recipe = mRecipeList.get(position);
        Log.d(TAG, recipe.getName());
        TextView tvRecipeName = holder.btnRecipeName.findViewById(R.id.tv_recipe_name);
        tvRecipeName.setText(recipe.getName());
        setRecipeImage(holder, recipe);
        holder.btnRecipeName.setBackgroundColor(context.getResources().getColor(R.color.colorYellow));
    }

    private void setRecipeImage(RecipeCardRecyclerViewAdapter.RecipeCardViewHolder holder, Recipe recipe) {
        ImageView ivRecipeImage = holder.btnRecipeName.findViewById(R.id.iv_recipe_image);
        int recipeAsset = 0;
        switch (recipe.getName()) {
            case "Nutella Pie":
                recipeAsset = R.drawable.nutella_pie;
                break;
            case "Brownies":
                recipeAsset = R.drawable.brownie;
                break;
            case "Yellow Cake":
                recipeAsset = R.drawable.yellow_cake;
                break;
            case "Cheesecake":
                recipeAsset = R.drawable.cheese_cake;
                break;
        }

        ivRecipeImage.setImageResource(recipeAsset);
    }

    @Override
    public int getItemCount() {
        //  Log.d(TAG, String.valueOf(mRecipeList.size()));
        return mRecipeList == null ? 0 : mRecipeList.size();
    }

    public class RecipeCardViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout btnRecipeName;

        public RecipeCardViewHolder(@NonNull View itemView) {
            super(itemView);
            btnRecipeName = itemView.findViewById(R.id.btn_recipe_card);
            btnRecipeName.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick called from RecyclerView");
            int adapterPosition = getAdapterPosition();
            Recipe recipe = mRecipeList.get(adapterPosition);
            mClickHandler.onClick(recipe);
        }
    }

    public void setRecipeData(List<Recipe> recipe) {
        mRecipeList = recipe;
        notifyDataSetChanged();

    }
}
