package net.sunyounglee.bakingapp.ui;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import net.sunyounglee.bakingapp.R;
import net.sunyounglee.bakingapp.models.Step;

import java.util.ArrayList;

public class StepActivity extends AppCompatActivity {
    private static final String TAG = StepActivity.class.getSimpleName();
    private ArrayList<Step> mStepList;
    private Step mStep;
    private TextView tvPage;
    private ImageView ivPrev, ivNext;
    private int currentPage;
    private String mRecipeName;
    private boolean landscapeMode = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);
        getOrientationMode();
        if (savedInstanceState == null) {
            Intent intent = getIntent();
            if (intent != null && intent.hasExtra("STEP_INTENT_EXTRA")) {
                mStep = intent.getParcelableExtra("STEP_INTENT_EXTRA");
                assert mStep != null;
                Log.d(TAG, "Step intent: " + mStep.toString());
            }
            if (intent != null && intent.hasExtra("STEPLIST_INTENT_EXTRA")) {
                mStepList = intent.getParcelableArrayListExtra("STEPLIST_INTENT_EXTRA");
                assert mStepList != null;
                Log.d(TAG, "StepList intent: " + mStepList.get(0).toString());
            }
            if (intent != null && intent.hasExtra("recipeName_key")) {
                mRecipeName = intent.getStringExtra("recipeName_key");
                Log.d(TAG, "recipe name: " + mRecipeName);
            }
        } else {
            mStep = savedInstanceState.getParcelable("STEP_DATA");
            mStepList = savedInstanceState.getParcelableArrayList("STEP_LIST_DATA");
        }

        displayUpButton();

        currentPage = mStep.getId();

        tvPage = findViewById(R.id.tv_page);
        ivPrev = findViewById(R.id.iv_prev);
        ivNext = findViewById(R.id.iv_next);

        tvPage.setText(mStep.getId() + "/" + (mStepList.size() - 1));

        if (mStep.getId() == 0) {
            ivPrev.setVisibility(View.INVISIBLE);
        }
        if (mStep.getId() == mStepList.size() - 1) {
            ivNext.setVisibility(View.INVISIBLE);
        }

        ivPrev.setOnClickListener(v -> {
            currentPage--;
            ivNext.setVisibility(View.VISIBLE);
            if (currentPage == 0) {
                ivPrev.setVisibility(View.INVISIBLE);
            } else {
                ivPrev.setVisibility(View.VISIBLE);
            }
            mStep = mStepList.get(currentPage);
            tvPage.setText(mStep.getId() + "/" + (mStepList.size() - 1));
            fragmentReplace(mStep);
        });

        ivNext.setOnClickListener(v -> {
            currentPage++;
            ivPrev.setVisibility(View.VISIBLE);
            if (currentPage == mStepList.size() - 1) {
                ivNext.setVisibility(View.INVISIBLE);
            } else {
                ivNext.setVisibility(View.VISIBLE);
            }
            mStep = mStepList.get(currentPage);
            tvPage.setText(mStep.getId() + "/" + (mStepList.size() - 1));
            fragmentReplace(mStep);
        });

        if (savedInstanceState == null) {
            fragmentTransaction(mStep);
        }

        if (landscapeMode) {
            hideNavigator();
        }
    }

    private void displayUpButton() {
        getSupportActionBar().setTitle(mRecipeName);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void fragmentReplace(Step mStep) {
        Fragment stepDetailFragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(StepDetailFragment.ARG_STEP, mStep);
        stepDetailFragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.step_detail_container, stepDetailFragment)
                .commit();
    }

    private void fragmentTransaction(Step mStep) {
        Fragment stepDetailFragment = new StepDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(StepDetailFragment.ARG_STEP, mStep);
        stepDetailFragment.setArguments(args);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.step_detail_container, stepDetailFragment)
                .commit();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable("STEP_DATA", mStep);
        outState.putParcelableArrayList("STEP_LIST_DATA", mStepList);
    }

    private void getOrientationMode() {
        int orientation_num = getResources().getConfiguration().orientation;
        landscapeMode = orientation_num != 1;
    }


    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            hideNavigator();
        } else {
            showNavigator();
        }
    }

    private void hideNavigator() {
        if (ivPrev != null) {
            ivPrev.setVisibility(View.GONE);
        }
        if (ivNext != null) {
            ivNext.setVisibility(View.GONE);
        }
        if (tvPage != null) {
            tvPage.setVisibility(View.GONE);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
    }

    private void showNavigator() {
        if (ivPrev != null) {
            ivPrev.setVisibility(View.VISIBLE);
        }
        if (ivNext != null) {
            ivNext.setVisibility(View.VISIBLE);
        }
        if (tvPage != null) {
            tvPage.setVisibility(View.VISIBLE);
        }

        if (getSupportActionBar() != null) {
            getSupportActionBar().show();
        }
    }

}