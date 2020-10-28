package net.sunyounglee.bakingapp;

import androidx.test.espresso.IdlingRegistry;
import androidx.test.espresso.IdlingResource;
import androidx.test.espresso.contrib.RecyclerViewActions;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.ActivityTestRule;

import net.sunyounglee.bakingapp.ui.MainActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

@RunWith(AndroidJUnit4.class)
public class IdlingResourceMainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule =
            new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void registerIdlingResource() {
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
    }

    @Test
    public void scrollRecyclerView_idlingResourceTest() {
        onView(withId(R.id.rv_recipe_card))
                .perform(RecyclerViewActions.scrollToPosition(3));
        String RECIPE_NAME_THREE = "Cheesecake";
        onView(withText(RECIPE_NAME_THREE)).check(matches(isDisplayed()));
    }

    @Test
    public void clickRecyclerView_idlingResourceTest() {
        onView(withId(R.id.rv_recipe_card)).perform(RecyclerViewActions.actionOnItemAtPosition(0, click()));
        String RECIPE_NAME_ZERO = "Nutella Pie";
        onView(withText(RECIPE_NAME_ZERO)).check(matches(isDisplayed()));

        onView(withId(R.id.rv_Ingredient))
                .perform(RecyclerViewActions.scrollToPosition(0));
        String INGREDIENT_ZERO = "Graham Cracker crumbs";
        onView(withText(INGREDIENT_ZERO)).check(matches(isDisplayed()));
    }


    @After
    public void unregisterIdlingResource() {
        if (mIdlingResource != null) {
            IdlingRegistry.getInstance().unregister(mIdlingResource);
        }
    }
}
