package ru.artempugachev.bmi;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.action.ViewActions.clearText;
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

    @Before
    public void clearInputs() {
        onView(withId(R.id.etHeight)).perform(clearText());
        onView(withId(R.id.etWeight)).perform(clearText());
    }


    /**
     * Test saving current input vals to preferences
     * */
    @Test
    public void saveLoadCurValsToPrefs() {
        // input values
        onView(withId(R.id.etHeight)).perform(typeText("178"));
        onView(withId(R.id.etWeight)).perform(typeText("72"));
        onView(withId(R.id.tvBmi)).check(matches(withText("BMI = 22.7")));

        // save values to preferences
        MainActivity mainActivity = mActivityRule.getActivity();
        SharedPreferences preferences = mainActivity.getPreferences(Context.MODE_PRIVATE);
        mainActivity.saveCurrentValsToPreferences(preferences);

        String height = preferences.getString(mainActivity.getString(R.string.pref_cur_height), "");
        String weight = preferences.getString(mainActivity.getString(R.string.pref_cur_weight), "");
        String bmi = preferences.getString(mainActivity.getString(R.string.pref_cur_bmi), "");

        assertEquals("178", height);
        assertEquals("72", weight);
        assertEquals("22.7", bmi);
    }

}
