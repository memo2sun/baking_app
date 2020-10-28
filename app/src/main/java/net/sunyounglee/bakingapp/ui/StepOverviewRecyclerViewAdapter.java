package net.sunyounglee.bakingapp.ui;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import net.sunyounglee.bakingapp.R;
import net.sunyounglee.bakingapp.models.Step;

import java.util.List;

public class StepOverviewRecyclerViewAdapter extends RecyclerView.Adapter<StepOverviewRecyclerViewAdapter.StepOverviewViewHolder> {
    private static final String TAG = StepOverviewRecyclerViewAdapter.class.getSimpleName();
    private List<Step> mStepList;

    private final StepOverviewRecyclerViewAdapter.StepOverviewOnClickHandler mClickHandler;

    /**
     * The interface that receives onClick messages.
     */
    public interface StepOverviewOnClickHandler {
        void onClick(Step step);
    }

    public StepOverviewRecyclerViewAdapter(StepOverviewRecyclerViewAdapter.StepOverviewOnClickHandler clickHandler) {
        mClickHandler = clickHandler;
    }

    @NonNull
    @Override
    public StepOverviewRecyclerViewAdapter.StepOverviewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.step_overview_item, parent, false);
        return new StepOverviewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepOverviewRecyclerViewAdapter.StepOverviewViewHolder holder, int position) {
        Step step = mStepList.get(position);
        holder.tvShortDescription.setText(step.getShortDescription());
    }

    @Override
    public int getItemCount() {
        return mStepList == null ? 0 : mStepList.size();
    }

    public class StepOverviewViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvShortDescription;
        ImageView ivStepDetailButton;

        public StepOverviewViewHolder(@NonNull View itemView) {
            super(itemView);
            tvShortDescription = itemView.findViewById(R.id.tv_short_description);
            ivStepDetailButton = itemView.findViewById(R.id.iv_step_detail);
            ivStepDetailButton.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Log.d(TAG, "StepOverviewViewHolder onClick called from RecyclerView");
            int adapterPosition = getAdapterPosition();
            Step step = mStepList.get(adapterPosition);
            mClickHandler.onClick(step);
        }
    }

    public void setStepData(List<Step> stepList) {
        mStepList = stepList;
        notifyDataSetChanged();

    }

}
