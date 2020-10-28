package net.sunyounglee.bakingapp;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

import net.sunyounglee.bakingapp.models.Ingredient;
import net.sunyounglee.bakingapp.models.Step;
import net.sunyounglee.bakingapp.ui.RecipeDetailActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.isInternal;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;

@RunWith(AndroidJUnit4.class)
public class RecipeDetailActivityTest {

    private ArrayList<Ingredient> mIngredientList = new ArrayList<>();
    private ArrayList<Step> mStepList = new ArrayList<>();

    @Rule
    public IntentsTestRule<RecipeDetailActivity> mIntentTestRule = new IntentsTestRule<RecipeDetailActivity>
            (RecipeDetailActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            mIngredientList = getIngredientList();
            mStepList = getStepList();

            Context targetContext = InstrumentationRegistry.getInstrumentation()
                    .getTargetContext();
            Intent result = new Intent(targetContext, RecipeDetailActivity.class);
            result.putExtra("INGREDIENT_INTENT_EXTRA", mIngredientList);
            result.putExtra("STEP_INTENT_EXTRA", mStepList);
            return result;
        }
    };

    @Before
    public void stubAllExternalIntents() {
        // By default Espresso Intents does not stub any Intents. Stubbing needs to be setup before
        // every test run. In this case all external Intents will be blocked.
        intending(not(isInternal())).respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, null));
    }

    @Test
    public void clickImage_SendStepDetail() {
        onView(allOf(childAtPosition(
                childAtPosition(
                        withId(R.id.tabLayout), 0), 1), isDisplayed()))
                .perform(click());

        onView(withId(R.id.rv_step_overview))
                .perform(RecyclerViewActions.scrollToPosition(3));

        onView(withId(R.id.rv_step_overview)).perform(
                RecyclerViewActions.actionOnItemAtPosition(0, MyViewAction.clickChildViewWithId(R.id.iv_step_detail)));

        String STEP_ZERO = "Recipe Introduction";
        onView(withText(STEP_ZERO)).check(matches(isDisplayed()));
        onView(allOf(withId(R.id.playerView),
                childAtPosition(
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                0), 0),
                isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("with " + position + " child view of type parentMatcher");
                parentMatcher.describeTo(description);
            }

            @Override
            public boolean matchesSafely(View view) {
                ViewParent parent = view.getParent();
                return parent instanceof ViewGroup && parentMatcher.matches(parent)
                        && view.equals(((ViewGroup) parent).getChildAt(position));
            }
        };
    }

    private ArrayList<Ingredient> getIngredientList() {
        mIngredientList.add(new Ingredient(2, "CUP", "Graham Cracker crumbs"));
        mIngredientList.add(new Ingredient(6, "TBLSP", "unsalted butter, melted"));
        mIngredientList.add(new Ingredient(0.5f, "CUP", "granulated sugar"));
        mIngredientList.add(new Ingredient(1.5f, "TSP", "salt"));
        mIngredientList.add(new Ingredient(5, "TBLSP", "vanilla"));
        mIngredientList.add(new Ingredient(1, "K", "Nutella or other chocolate-hazelnut spread"));
        mIngredientList.add(new Ingredient(500, "G", "Mascapone Cheese(room temperature"));
        mIngredientList.add(new Ingredient(1, "CUP", "heavy cream(cold)"));
        mIngredientList.add(new Ingredient(4, "OZ", "cream cheese(softened)"));

        return mIngredientList;
    }

    private ArrayList<Step> getStepList() {
        mStepList.add(new Step(0, "Recipe Introduction", "Recipe Introduction",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd974_-intro-creampie/-intro-creampie.mp4",
                ""));
        mStepList.add(new Step(1, "Starting prep", "1. Preheat the oven to 350\u00b0F. Butter a 9\" deep dish pie pan.",
                "", ""));
        mStepList.add(new Step(2, "Prep the cookie crust.",
                "2. Whisk the graham cracker crumbs, 50 grams (1/4 cup) of sugar, and 1/2 teaspoon of salt together in a medium bowl. Pour the melted butter and 1 teaspoon of vanilla into the dry ingredients and stir together until evenly mixed.",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9a6_2-mix-sugar-crackers-creampie/2-mix-sugar-crackers-creampie.mp4",
                ""));
        mStepList.add(new Step(3, "Press the crust into baking form.", "3. Press the cookie crumb mixture into the prepared pie pan and bake for 12 minutes. Let crust cool to room temperature.",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd9cb_4-press-crumbs-in-pie-plate-creampie/4-press-crumbs-in-pie-plate-creampie.mp4",
                ""));
        mStepList.add(new Step(4, "Start filling prep", "4. Beat together the nutella, mascarpone, 1 teaspoon of salt, and 1 tablespoon of vanilla on medium speed in a stand mixer or high speed with a hand mixer until fluffy.",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffd97a_1-mix-marscapone-nutella-creampie/1-mix-marscapone-nutella-creampie.mp4",
                ""));
        mStepList.add(new Step(5, "Finish filling prep", "5. Beat the cream cheese and 50 grams (1/4 cup) of sugar on medium speed in a stand mixer or high speed with a hand mixer for 3 minutes. Decrease the speed to medium-low and gradually add in the cold cream. Add in 2 teaspoons of vanilla and beat until stiff peaks form.",
                "", "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda20_7-add-cream-mix-creampie/7-add-cream-mix-creampie.mp4"));
        mStepList.add(new Step(6, "Finishing Steps", "6. Pour the filling into the prepared crust and smooth the top. Spread the whipped cream over the filling. Refrigerate the pie for at least 2 hours. Then it's ready to serve!",
                "https://d17h27t6h515a5.cloudfront.net/topher/2017/April/58ffda45_9-add-mixed-nutella-to-crust-creampie/9-add-mixed-nutella-to-crust-creampie.mp4",
                ""));
        return mStepList;

    }

    // https://stackoverflow.com/questions/28476507/using-espresso-to-click-view-inside-recyclerview-item
    public static class MyViewAction {
        public static ViewAction clickChildViewWithId(final int id) {
            return new ViewAction() {
                @Override
                public Matcher<View> getConstraints() {
                    return null;
                }

                @Override
                public String getDescription() {
                    return "Click on a child view with specified id.";
                }

                @Override
                public void perform(UiController uiController, View view) {
                    View v = view.findViewById(id);
                    v.performClick();
                }
            };
        }

    }
}
