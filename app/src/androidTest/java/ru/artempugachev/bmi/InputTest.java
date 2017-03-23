package ru.artempugachev.bmi;

import android.support.test.filters.LargeTest;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.typeText;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;

/**
 * Espresso test for user input
 */


@RunWith(AndroidJUnit4.class)
@LargeTest
public class InputTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

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

}