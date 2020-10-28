package net.sunyounglee.bakingapp.repositories;

import androidx.lifecycle.MutableLiveData;

import net.sunyounglee.bakingapp.models.Step;

public class StepViewRepository {

    private static StepViewRepository sInstance;
    private static final Object LOCK = new Object();
    private MutableLiveData<Step> mStep = new MutableLiveData<>();

    private StepViewRepository(Step step) {
        mStep.setValue(step);
    }

    public static StepViewRepository getInstance(Step step) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new StepViewRepository(step);
            }
        }
        return sInstance;
    }

    public MutableLiveData<Step> getStep() {
        return mStep;
    }

}
