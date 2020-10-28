package net.sunyounglee.bakingapp.utilities;

import net.sunyounglee.bakingapp.models.Recipe;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;

public interface RecipeAPIService {

    String GET_RECIPES = "topher/2017/May/59121517_baking/baking.json";

    //Fetch all recipes
    @GET(GET_RECIPES)
    Single<List<Recipe>> fetchAllRecipe();

}