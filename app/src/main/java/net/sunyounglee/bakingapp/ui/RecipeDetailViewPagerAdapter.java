package net.sunyounglee.bakingapp.ui;

import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import net.sunyounglee.bakingapp.models.Ingredient;
import net.sunyounglee.bakingapp.models.Step;

import java.util.ArrayList;

public class RecipeDetailViewPagerAdapter extends FragmentStateAdapter {
    private static final String TAG = RecipeDetailViewPagerAdapter.class.getSimpleName();
    private final int mTabCount;
    private ArrayList<Ingredient> mIngredientList;
    private ArrayList<Step> mStepList;
    private String mRecipeName;

    public RecipeDetailViewPagerAdapter(@NonNull FragmentActivity fragmentActivity, int tabCount, ArrayList<Ingredient> ingredients, ArrayList<Step> steps, String recipeName) {
        super(fragmentActivity);
        mTabCount = tabCount;
        mIngredientList = ingredients;
        mStepList = steps;
        mRecipeName = recipeName;
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        String RECIPENAME_KEY = "recipeName_key";
        switch (position) {
            case 0:
                Fragment ingredientFragment = new IngredientFragment();
                Bundle args1 = new Bundle();
                Log.d(TAG, "send parcelable arg: " + mIngredientList.get(0).getIngredient());
                args1.putParcelableArrayList(IngredientFragment.ARG_INGREDIENT, mIngredientList);
                ingredientFragment.setArguments(args1);
                return ingredientFragment;
            case 1:
                Fragment stepOverviewFragment = new StepOverviewFragment();
                Bundle args2 = new Bundle();
                args2.putParcelableArrayList(StepOverviewFragment.ARG_STEP, mStepList);
                Log.d(TAG, "recipe  name" + mRecipeName);
                args2.putString(RECIPENAME_KEY, mRecipeName);
                stepOverviewFragment.setArguments(args2);
                return stepOverviewFragment;
        }
        return null;
    }

    @Override
    public int getItemCount() {
        return mTabCount;
    }
}
