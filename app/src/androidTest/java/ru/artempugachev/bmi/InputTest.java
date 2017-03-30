package ru.artempugachev.bmi;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.clearText;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withTagValue;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.is;

/**
 * Espresso test for user input
 */


@RunWith(AndroidJUnit4.class)
@LargeTest
public class InputTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);


    @Before
    public void clearInputs() {
        onView(withId(R.id.etHeight)).perform(clearText());
        onView(withId(R.id.etWeight)).perform(clearText());
    }


    /**
     * Test height input
     * */
    @Test
    public void setHeightNormal() {
        onView(withId(R.id.etHeight)).perform(typeText("178"));
        onView(withId(R.id.etHeight)).check(matches(withText("178")));
    }


    /**
     * Test height input with decimal.
     * Height measures in cm, so we can't input height with decimal
     * */
    @Test
    public void setHeightDecimal() {
        onView(withId(R.id.etHeight)).perform(typeText("178.5"));
        onView(withId(R.id.etHeight)).check(matches(withText("1785")));
    }

    @Test
    public void check_bmi() {
        onView(withId(R.id.etHeight)).perform(typeText("178"));
        onView(withId(R.id.etWeight)).perform(typeText("73"));
        onView(withId(R.id.tvBmi)).check(matches(withText("BMI = 23.0")));
        onView(withTagValue(is((Object) "23.0")));
    }
}
