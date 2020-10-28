package net.sunyounglee.bakingapp.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.RemoteViews;

import net.sunyounglee.bakingapp.R;
import net.sunyounglee.bakingapp.models.Ingredient;
import net.sunyounglee.bakingapp.models.Step;
import net.sunyounglee.bakingapp.ui.RecipeDetailActivity;
import net.sunyounglee.bakingapp.utilities.RecipeUtils;

import java.util.ArrayList;

/**
 * Implementation of App Widget functionality.
 */
public class RecipeWidgetProvider extends AppWidgetProvider {
    private static final String TAG = RecipeWidgetProvider.class.getSimpleName();
    private static final int WIDGET_PENDING_INTENT_REQUEST_CODE = 1001;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {

        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.widget_ingredient_list_view);

        Intent intent = new Intent(context, RecipeWidgetService.class);
        intent.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId);
        intent.setData(Uri.parse(intent.toUri(Intent.URI_INTENT_SCHEME)));

        SharedPreferences sharedPref = context.getSharedPreferences(context.getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        String strRecipeName = sharedPref.getString(context.getString(R.string.saved_recipe_name_key), "");
        views.setTextViewText(R.id.tv_recipe_name, strRecipeName);

        String strIngredient = sharedPref.getString(context.getString(R.string.saved_ingredient_key), "");
        ArrayList<Ingredient> ingredientList = RecipeUtils.convertStringToListOfIngredient(strIngredient);

        String strStep = sharedPref.getString(context.getString(R.string.saved_step_key), "");
        ArrayList<Step> stepList = RecipeUtils.convertStringToListOfStep(strStep);

        views.setRemoteAdapter(R.id.widget_list, intent);
        views.setEmptyView(R.id.widget_list, R.id.empty_view);

        if (!strRecipeName.isEmpty()) {
            Intent appIntent = new Intent(context, RecipeDetailActivity.class);
            appIntent.putExtra("RECIPE_NAME_INTENT_EXTRA", strRecipeName);
            appIntent.putParcelableArrayListExtra("INGREDIENT_INTENT_EXTRA", ingredientList);
            appIntent.putParcelableArrayListExtra("STEP_INTENT_EXTRA", stepList);
            PendingIntent appPendingIntent = PendingIntent.getActivity(context, WIDGET_PENDING_INTENT_REQUEST_CODE, appIntent, PendingIntent.FLAG_UPDATE_CURRENT);

            views.setOnClickPendingIntent(R.id.layout_widget_list, appPendingIntent);
        }
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }

}

