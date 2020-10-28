package net.sunyounglee.bakingapp.viewModels;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import net.sunyounglee.bakingapp.models.Step;
import net.sunyounglee.bakingapp.repositories.StepViewRepository;

public class StepViewModel extends AndroidViewModel {
    private static final String TAG = StepViewModel.class.getSimpleName();
    private MutableLiveData<Step> mStep;

    public StepViewModel(Step step, @NonNull Application application, StepViewRepository stepViewRepository) {
        super(application);
        mStep = stepViewRepository.getStep();
    }

    public void setStep(Step step) {
        mStep.setValue(step);
        Log.d(TAG, "mStep: " + mStep.getValue().getDescription());
    }


    public LiveData<Step> getLiveDataStep() {
        Log.d(TAG, "mStep from getLiveDataStep: " + mStep.getValue().getDescription());
        return mStep;
    }

}
