package net.sunyounglee.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.viewpager2.widget.ViewPager2;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import net.sunyounglee.bakingapp.R;
import net.sunyounglee.bakingapp.models.Ingredient;
import net.sunyounglee.bakingapp.models.Step;
import net.sunyounglee.bakingapp.repositories.StepViewRepository;
import net.sunyounglee.bakingapp.viewModels.StepViewModel;
import net.sunyounglee.bakingapp.viewModels.StepViewModelFactory;

import java.util.ArrayList;

public class RecipeDetailActivity extends AppCompatActivity {
    private static final String TAG = RecipeDetailActivity.class.getSimpleName();
    private String mRecipeName;
    private ArrayList<Ingredient> mIngredientList;
    private ArrayList<Step> mStepList;

    private StepViewModel mStepViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);

        Intent intent = getIntent();
        if (intent != null && intent.hasExtra("RECIPE_NAME_INTENT_EXTRA")) {
            mRecipeName = intent.getStringExtra("RECIPE_NAME_INTENT_EXTRA");
        }

        if (intent != null && intent.hasExtra("INGREDIENT_INTENT_EXTRA")) {
            mIngredientList = intent.getParcelableArrayListExtra("INGREDIENT_INTENT_EXTRA");

        }
        if (intent != null && intent.hasExtra("STEP_INTENT_EXTRA")) {
            mStepList = intent.getParcelableArrayListExtra("STEP_INTENT_EXTRA");
        }


        StepViewRepository stepViewRepository = StepViewRepository.getInstance(mStepList.get(0));
        StepViewModelFactory stepViewModelFactory = new StepViewModelFactory(mStepList.get(0), getApplication(), stepViewRepository);
        mStepViewModel = new ViewModelProvider(this, stepViewModelFactory).get(StepViewModel.class);

        displayUpButton();
        viewPagerSetup();
        if (isTablet()) {
            displayVideoFragment();
        }
    }

    private void displayVideoFragment() {
        mStepViewModel.getLiveDataStep().observe(this, new Observer<Step>() {
            @Override
            public void onChanged(Step step) {
                StepDetailFragment stepDetailFragment = new StepDetailFragment();
                Bundle args = new Bundle();
                args.putParcelable(StepDetailFragment.ARG_STEP, step);
                stepDetailFragment.setArguments(args);
                Log.d(TAG, "STEP LIVE DATA: " + step.getDescription());
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_container, stepDetailFragment)
                        .commit();
            }
        });

    }

    private void displayUpButton() {
        getSupportActionBar().setTitle(mRecipeName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void viewPagerSetup() {
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        String[] tabTitle = new String[]{"INGREDIENTS", "STEPS"};
        RecipeDetailViewPagerAdapter recipeDetailViewPagerAdapter =
                new RecipeDetailViewPagerAdapter(this, tabLayout.getTabCount(), mIngredientList, mStepList, mRecipeName);
        ViewPager2 viewPager = findViewById(R.id.vp_recipe_detail);
        viewPager.setAdapter(recipeDetailViewPagerAdapter);

        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(tabTitle[position])
        ).attach();
    }

    private boolean isTablet() {
        boolean bool_tablet = getResources().getBoolean(R.bool.isTablet);
        return bool_tablet;
    }
}