package net.sunyounglee.bakingapp.utilities;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import net.sunyounglee.bakingapp.R;
import net.sunyounglee.bakingapp.models.Ingredient;
import net.sunyounglee.bakingapp.models.Step;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class RecipeUtils {
    private static final String TAG = RecipeUtils.class.getSimpleName();

    public static void updateSharedPreferenceForWidget(Context context, String recipeName, ArrayList<Ingredient> ingredientList, ArrayList<Step> stepList) {
        Log.d(TAG, "updateSharedPreferenceForWidget");
        SharedPreferences sharedPref = context.getSharedPreferences(
                context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(context.getString(R.string.saved_recipe_name_key), recipeName);
        String strIngredient = convertListOfIngredientToString(ingredientList);
        editor.putString(context.getString(R.string.saved_ingredient_key), strIngredient);

        String strStep = convertListOfStepToString(stepList);
        editor.putString(context.getString(R.string.saved_step_key), strStep);
        editor.commit();
    }

    public static String convertListOfIngredientToString(ArrayList<Ingredient> ingredientList) {
        Type listOfIngredient = new TypeToken<ArrayList<Ingredient>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.toJson(ingredientList, listOfIngredient);
    }

    public static ArrayList<Ingredient> convertStringToListOfIngredient(String strIngredient) {
        Type listOfIngredient = new TypeToken<ArrayList<Ingredient>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(strIngredient, listOfIngredient);
    }

    public static String convertListOfStepToString(ArrayList<Step> stepList) {
        Type listOfStep = new TypeToken<ArrayList<Step>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.toJson(stepList, listOfStep);
    }

    public static ArrayList<Step> convertStringToListOfStep(String strStep) {
        Type listOfStep = new TypeToken<ArrayList<Step>>() {
        }.getType();
        Gson gson = new Gson();
        return gson.fromJson(strStep, listOfStep);
    }
}
