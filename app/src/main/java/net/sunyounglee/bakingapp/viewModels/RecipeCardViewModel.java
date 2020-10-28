package net.sunyounglee.bakingapp.viewModels;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import net.sunyounglee.bakingapp.models.Recipe;
import net.sunyounglee.bakingapp.repositories.RecipeCardRepository;

import java.util.List;

public class RecipeCardViewModel extends AndroidViewModel {

    private String TAG = RecipeCardViewModel.class.getSimpleName();
    private RecipeCardRepository recipeCardRepository;

    public RecipeCardViewModel(@NonNull Application application, RecipeCardRepository recipeCardRepository) {
        super(application);
        recipeCardRepository = RecipeCardRepository.getInstance(application);
        recipeCardRepository.fetchRecipe(application);
        this.recipeCardRepository = recipeCardRepository;
    }

    public LiveData<List<Recipe>> getRecipeList() {
        return recipeCardRepository.getRecipeList();
    }

}
