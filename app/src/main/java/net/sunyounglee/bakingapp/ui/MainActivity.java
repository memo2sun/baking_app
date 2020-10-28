package net.sunyounglee.bakingapp.ui;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.sunyounglee.bakingapp.IdlingResource.SimpleIdlingResource;
import net.sunyounglee.bakingapp.R;
import net.sunyounglee.bakingapp.models.Ingredient;
import net.sunyounglee.bakingapp.models.Recipe;
import net.sunyounglee.bakingapp.models.Step;
import net.sunyounglee.bakingapp.repositories.RecipeCardRepository;
import net.sunyounglee.bakingapp.utilities.RecipeUtils;
import net.sunyounglee.bakingapp.viewModels.RecipeCardViewModel;
import net.sunyounglee.bakingapp.viewModels.RecipeCardViewModelFactory;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements RecipeCardRecyclerViewAdapter.RecipeAdapterOnClickHandler, MessageDelayer.DelayerCallback {

    private static final String TAG = MainActivity.class.getSimpleName();
    private RecyclerView mRecyclerView;
    private RecipeCardRecyclerViewAdapter mRecipeCardRecyclerViewAdapter;

    private ProgressBar mPbLoadingIndicator;
    private RecipeCardViewModel mRecipeCardViewModel;
    private List<Recipe> mRecipe = new ArrayList<>();

    @Nullable
    private SimpleIdlingResource mIdlingResource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getIdlingResource();

        RecipeCardRepository recipeCardRepository = RecipeCardRepository.getInstance(this);
        RecipeCardViewModelFactory recipeCardViewModelFactory = new RecipeCardViewModelFactory(getApplication(), recipeCardRepository);
        mRecipeCardViewModel = new ViewModelProvider(this, recipeCardViewModelFactory).get(RecipeCardViewModel.class);

        mRecyclerView = findViewById(R.id.rv_recipe_card);
        TextView mTvErrorMessage = findViewById(R.id.tv_error_message);
        mPbLoadingIndicator = findViewById(R.id.pb_loading_indicator);

        int noOfColumns = calculateNoOfColumns();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, noOfColumns);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setHasFixedSize(true);

        mRecipeCardRecyclerViewAdapter = new RecipeCardRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mRecipeCardRecyclerViewAdapter);

        showLoading();
        setupViewModel();

    }

    private int calculateNoOfColumns() {
        DisplayMetrics displayMetrics = this.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int scalingFactor = 300;
        int noOfColumns = (int) (dpWidth / scalingFactor);
        if (noOfColumns < 2)
            noOfColumns = 2;
        return noOfColumns;
    }

    private void setupViewModel() {
        if (isNetworkAvailable(this)) {
            mRecyclerView.setVisibility(View.VISIBLE);
            mPbLoadingIndicator.setVisibility(View.INVISIBLE);
        }
        MessageDelayer.processMessage(mRecipe, this, mIdlingResource);

        mRecipeCardViewModel.getRecipeList().observe(this, recipes -> {

            Log.d(TAG, recipes.get(0).toString());
            mRecipeCardRecyclerViewAdapter.setRecipeData(recipes);
            mRecipe = recipes;
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
        RecipeUtils.updateSharedPreferenceForWidget(this, recipe.getName(), mIngredientList, mStepList);
        Intent intent = new Intent(this, RecipeDetailActivity.class);
        intent.putExtra("RECIPE_NAME_INTENT_EXTRA", recipe.getName());
        intent.putParcelableArrayListExtra("INGREDIENT_INTENT_EXTRA", mIngredientList);
        intent.putParcelableArrayListExtra("STEP_INTENT_EXTRA", mStepList);
        Log.d(TAG, "recipe click: " + recipe.getName());
        startActivity(intent);
    }


    public SimpleIdlingResource getIdlingResource() {
        if (mIdlingResource == null) {
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    public void onDone(List<Recipe> recipeList) {

    }
}
