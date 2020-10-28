package net.sunyounglee.bakingapp.ui;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.sunyounglee.bakingapp.R;
import net.sunyounglee.bakingapp.models.Step;
import net.sunyounglee.bakingapp.repositories.StepViewRepository;
import net.sunyounglee.bakingapp.viewModels.StepViewModel;
import net.sunyounglee.bakingapp.viewModels.StepViewModelFactory;

import java.util.ArrayList;

public class StepOverviewFragment extends Fragment implements StepOverviewRecyclerViewAdapter.StepOverviewOnClickHandler {
    private static final String TAG = StepOverviewFragment.class.getSimpleName();
    public static final String ARG_STEP = "step_list";
    private static final String RECIPENAME_KEY = "recipeName_key";
    private ArrayList<Step> mStepList;
    private String mRecipeName;
    private StepViewModel stepViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_step_overview, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            Bundle args = getArguments();
            mStepList = args.getParcelableArrayList(ARG_STEP);
            mRecipeName = args.getString(RECIPENAME_KEY);
            Log.d(TAG, "recipeName: " + mRecipeName);
        } else {
            Log.d(TAG, "step bundle is null");
        }
        Step mStep = mStepList.get(0);
        StepViewRepository stepViewRepository = StepViewRepository.getInstance(mStep);
        StepViewModelFactory stepViewModelFactory = new StepViewModelFactory(mStep, getActivity().getApplication(), stepViewRepository);
        stepViewModel = new ViewModelProvider(this, stepViewModelFactory).get(StepViewModel.class);


        RecyclerView mRecyclerView = view.findViewById(R.id.rv_step_overview);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setHasFixedSize(true);

        StepOverviewRecyclerViewAdapter mStepOverviewRecyclerViewAdapter = new StepOverviewRecyclerViewAdapter(this);
        mRecyclerView.setAdapter(mStepOverviewRecyclerViewAdapter);

        mStepOverviewRecyclerViewAdapter.setStepData(mStepList);
    }

    @Override
    public void onClick(Step step) {
        Log.d(TAG, "step click: " + step.getShortDescription());
        if (isTablet()) {
            stepViewModel.setStep(step);
            Log.d(TAG, "this is a tablet");

        } else {
            Intent intent = new Intent(getContext(), StepActivity.class);
            intent.putParcelableArrayListExtra("STEPLIST_INTENT_EXTRA", mStepList);
            intent.putExtra("STEP_INTENT_EXTRA", step);
            intent.putExtra(RECIPENAME_KEY, mRecipeName);
            startActivity(intent);
        }
    }

    private boolean isTablet() {
        return getResources().getBoolean(R.bool.isTablet);
    }
}