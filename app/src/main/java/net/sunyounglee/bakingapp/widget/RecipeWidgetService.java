package net.sunyounglee.bakingapp.widget;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService;

import net.sunyounglee.bakingapp.R;
import net.sunyounglee.bakingapp.models.Ingredient;
import net.sunyounglee.bakingapp.utilities.RecipeUtils;

import java.util.ArrayList;
import java.util.List;

public class RecipeWidgetService extends RemoteViewsService {
    @Override
    public RemoteViewsFactory onGetViewFactory(Intent intent) {
        return new RecipeRemoteViewsFactory(this.getApplication());
    }
}

class RecipeRemoteViewsFactory implements RemoteViewsService.RemoteViewsFactory {
    private static final String TAG = RecipeRemoteViewsFactory.class.getSimpleName();
    Application mApplication;
    private List<Ingredient> ingredientList = new ArrayList<>();

    public RecipeRemoteViewsFactory(Application application) {
        mApplication = application;

    }

    @Override
    public void onCreate() {
    }

    @Override
    public void onDataSetChanged() {
        SharedPreferences sharedPref = mApplication.getSharedPreferences(mApplication.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String strIngredient = sharedPref.getString(mApplication.getString(R.string.saved_ingredient_key), "");
        ingredientList = RecipeUtils.convertStringToListOfIngredient(strIngredient);
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public int getCount() {
        return ingredientList == null ? 0 : ingredientList.size();
    }

    @Override
    public RemoteViews getViewAt(int position) {

        RemoteViews rv = new RemoteViews(mApplication.getPackageName(), R.layout.widget_ingredient_item);
        rv.setTextViewText(R.id.tv_ingredient, ingredientList.get(position).getIngredient());
        rv.setTextViewText(R.id.tv_quantity, String.valueOf(ingredientList.get(position).getQuantity()));
        rv.setTextViewText(R.id.tv_measure, ingredientList.get(position).getMeasure());
        return rv;
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }

    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }
}
