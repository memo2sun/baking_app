package net.sunyounglee.bakingapp.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import net.sunyounglee.bakingapp.repositories.RecipeCardRepository;

public class RecipeCardViewModelFactory implements ViewModelProvider.Factory {

    @NonNull
    private final Application application;
    private RecipeCardRepository mRecipeCardRepository;

    public RecipeCardViewModelFactory(@NonNull Application application, RecipeCardRepository recipeCardRepository) {
        this.application = application;
        this.mRecipeCardRepository = recipeCardRepository;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        if (modelClass == RecipeCardViewModel.class) {
            return (T) new RecipeCardViewModel(application, mRecipeCardRepository);
        }
        return null;
    }
}
