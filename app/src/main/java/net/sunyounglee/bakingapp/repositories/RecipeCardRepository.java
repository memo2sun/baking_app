package net.sunyounglee.bakingapp.repositories;

import android.content.Context;
import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import net.sunyounglee.bakingapp.models.Recipe;
import net.sunyounglee.bakingapp.utilities.RecipeAPIService;
import net.sunyounglee.bakingapp.utilities.RetrofitClient;

import java.util.List;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

public class RecipeCardRepository {
    private static final String TAG = RecipeCardRepository.class.getSimpleName();
    private static RecipeCardRepository sInstance;
    private static final Object LOCK = new Object();
    private MutableLiveData<List<Recipe>> mRecipeList = new MutableLiveData<>();

    private RecipeCardRepository(Context context) {
        fetchRecipe(context);
    }

    public static RecipeCardRepository getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new RecipeCardRepository(context);
            }
        }
        return sInstance;
    }

    public void fetchRecipe(Context context) {
        RecipeAPIService service = RetrofitClient.getRetrofitInstance(context).create(RecipeAPIService.class);
        service.fetchAllRecipe()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<List<Recipe>>() {

                    @Override
                    public void onSuccess(List<Recipe> recipes) {
                        // mRecipeList.postValue(recipes);
                        mRecipeList.setValue(recipes);
                        Log.d(TAG, recipes.get(0).getName());
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.d(TAG, e.getMessage());
                    }
                });
    }

    public LiveData<List<Recipe>> getRecipeList() {
        return mRecipeList;
    }


}
