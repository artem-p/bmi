package ru.artempugachev.bmi;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Test for save and restore current vals.
 */


@RunWith(AndroidJUnit4.class)
@LargeTest
public class CurrentValsTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);


    /**
     * Test saving current input vals to preferences
     * */
    @Test
    public void save_cur_vals_to_prefs() {
        // input values
        onView(withId(R.id.etHeight)).perform(typeText("178"));
        onView(withId(R.id.etWeight)).perform(typeText("73"));
        onView(withId(R.id.tvBmi)).check(matches(withText("BMI = 23.0")));

        // save values to preferences
        MainActivity mainActivity = mActivityRule.getActivity();
        SharedPreferences preferences = mainActivity.getPreferences(Context.MODE_PRIVATE);
        mainActivity.saveToPreferences(preferences);

        // check values was saved
        String height = preferences.getString(mainActivity.getString(R.string.pref_cur_height), "");
        String weight = preferences.getString(mainActivity.getString(R.string.pref_cur_weight), "");
        String bmi = preferences.getString(mainActivity.getString(R.string.pref_cur_bmi), "");

        assertEquals(height, "178");
        assertEquals(weight, "73");
        assertEquals(bmi, "BMI = 23.0");
    }
}
