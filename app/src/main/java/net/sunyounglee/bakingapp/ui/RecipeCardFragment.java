package net.sunyounglee.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.sunyounglee.bakingapp.R;
import net.sunyounglee.bakingapp.models.Ingredient;
import net.sunyounglee.bakingapp.models.Recipe;
import net.sunyounglee.bakingapp.models.Step;
import net.sunyounglee.bakingapp.repositories.RecipeCardRepository;
import net.sunyounglee.bakingapp.viewModels.RecipeCardViewModel;
import net.sunyounglee.bakingapp.viewModels.RecipeCardViewModelFactory;

import java.util.ArrayList;

import static android.content.Context.CONNECTIVITY_SERVICE;

public class RecipeCardFragment extends Fragment implements RecipeCardRecyclerViewAdapter.RecipeAdapterOnClickHandler {
    private static final String TAG = RecipeCardFragment.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecipeCardRecyclerViewAdapter mRecipeCardRecyclerViewAdapter;

    private ProgressBar mPbLoadingIndicator;
    private RecipeCardViewModel mRecipeCardViewModel;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_card, container, false);

        RecipeCardRepository recipeCardRepository = RecipeCardRepository.getInstance(this.getContext());
        RecipeCardViewModelFactory recipeCardViewModelFactory = new RecipeCardViewModelFactory(getActivity().getApplication(), recipeCardRepository);
        mRecipeCardViewModel = new ViewModelProvider(this, recipeCardViewModelFactory).get(RecipeCardViewModel.class);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mRecyclerView = view.findViewById(R.id.rv_recipe_card);
        TextView mTvErrorMessage = view.findViewById(R.id.tv_error_message);
        mPbLoadingIndicator = view.findViewById(R.id.pb_loading_indicator);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setHasFixedSize(true);

        mRecipeCardRecyclerViewAdapter = new RecipeCardRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mRecipeCardRecyclerViewAdapter);

        showLoading();
        setupViewModel();
    }

    private void setupViewModel() {
        if (isNetworkAvailable(getContext())) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mPbLoadingIndicator.setVisibility(View.INVISIBLE);
        }
        mRecipeCardViewModel.getRecipeList().observe(this, recipes -> {
            Log.d(TAG, recipes.get(0).toString());
            Log.d(TAG, recipes.get(0).getIngredients().toString());
            Log.d(TAG, recipes.get(0).getSteps().toString());
            mRecipeCardRecyclerViewAdapter.setRecipeData(recipes);
        });
    }

    private void showLoading() {
        mRecyclerView.setVisibility(View.INVISIBLE);
        mPbLoadingIndicator.setVisibility(View.VISIBLE);
    }

    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(CONNECTIVITY_SERVICE);
        NetworkCapabilities networkCapabilities = null;
        boolean connected;
        if (cm != null) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                Network network = cm.getActiveNetwork();
                if (network != null) {
                    networkCapabilities = cm.getNetworkCapabilities(network);
                } else {
                    return false;
                }
            } else {
                Network[] networks = cm.getAllNetworks();
                for (int i = 0; i < networks.length && networkCapabilities == null; i++) {
                    networkCapabilities = cm.getNetworkCapabilities(networks[i]);
                }
            }
            assert networkCapabilities != null;
            connected = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET);
            // Log.d(TAG, "network boolean: " + connected);
            return connected;
        }
        return false;
    }

    @Override
    public void onClick(Recipe recipe) {
        Log.d(TAG, "recipe click: " + recipe.getName());
        ArrayList<Ingredient> mIngredientList = recipe.getIngredients();
        ArrayList<Step> mStepList = recipe.getSteps();
        Intent intent = new Intent(getContext(), RecipeDetailActivity.class);
        intent.putExtra("RECIPE_INTENT_EXTRA", recipe);
        intent.putParcelableArrayListExtra("INGREDIENT_INTENT_EXTRA", mIngredientList);
        intent.putParcelableArrayListExtra("STEP_INTENT_EXTRA", mStepList);
        Log.d(TAG, "recipe click: " + recipe.getName());
        startActivity(intent);
    }
}