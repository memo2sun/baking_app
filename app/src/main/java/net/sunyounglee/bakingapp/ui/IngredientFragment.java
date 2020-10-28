package net.sunyounglee.bakingapp.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import net.sunyounglee.bakingapp.R;
import net.sunyounglee.bakingapp.models.Ingredient;

import java.util.ArrayList;

public class IngredientFragment extends Fragment {
    private static final String TAG = IngredientFragment.class.getSimpleName();
    public static final String ARG_INGREDIENT = "ingredient_list";
    private ArrayList<Ingredient> mIngredientList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_ingredient, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if (getArguments() != null) {
            Bundle args = getArguments();
            mIngredientList = args.getParcelableArrayList(ARG_INGREDIENT);
        } else {
            Log.d(TAG, "ingredient bundle is null");
        }

        RecyclerView mRecyclerView = view.findViewById(R.id.rv_Ingredient);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setNestedScrollingEnabled(true);
        mRecyclerView.setHasFixedSize(true);

        IngredientRecyclerAdapter mIngredientRecyclerAdapter = new IngredientRecyclerAdapter();
        mRecyclerView.setAdapter(mIngredientRecyclerAdapter);

        mIngredientRecyclerAdapter.setIngredientData(mIngredientList);
    }
}