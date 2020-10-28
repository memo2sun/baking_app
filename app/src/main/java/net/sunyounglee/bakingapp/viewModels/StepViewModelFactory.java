package net.sunyounglee.bakingapp.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import net.sunyounglee.bakingapp.models.Step;
import net.sunyounglee.bakingapp.repositories.StepViewRepository;

public class StepViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    private final Application application;
    StepViewRepository stepViewRepository;
    private final Step step;

    public StepViewModelFactory(Step step, @NonNull Application application, StepViewRepository stepViewRepository) {
        this.application = application;
        this.stepViewRepository = stepViewRepository;
        this.step = step;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == StepViewModel.class) {
            return (T) new StepViewModel(step, application, stepViewRepository);
        }
        return null;
    }
}
