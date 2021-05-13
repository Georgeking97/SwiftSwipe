package com.example.swiftwipe;


import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.ViewInteraction;
import androidx.test.espresso.contrib.RecyclerViewActions;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import static androidx.test.InstrumentationRegistry.getInstrumentation;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.pressBack;
import static androidx.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu;
import static androidx.test.espresso.action.ViewActions.*;
import static androidx.test.espresso.assertion.ViewAssertions.*;
import static androidx.test.espresso.matcher.ViewMatchers.*;

import com.example.swiftwipe.R;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class SearchPageLayoutTest {

    @Rule
    public ActivityTestRule<Login> mActivityTestRule = new ActivityTestRule<>(Login.class);

    @Test
    public void searchPageLayoutTest() {
        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.Email),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                1),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("georgehenryking97@gmail.com"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.password),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                2),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("Henrygeorge13"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.loginBtn), withText("Login"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                3),
                        isDisplayed()));
        appCompatButton.perform(click());

        SystemClock.sleep(1000);

        onView(allOf(withId(R.id.search),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                0),
                        isDisplayed())).perform(click());
        ViewInteraction editText = onView(allOf(withId(R.id.searchBar)));
        editText.check(matches(withHint("Search...")));

        SystemClock.sleep(1000);

        onView(allOf(withId(R.id.result_list))).check(matches(isDisplayed()));

        ViewInteraction textView6 = onView(allOf(withId(R.id.ProductPrice), withText("€39.99")));
        textView6.check(matches(withText("€39.99")));

        ViewInteraction imageButton = onView(allOf(withId(R.id.homeBtn)));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction imageButton2 = onView(allOf(withId(R.id.searchBtn)));
        imageButton2.check(matches(isDisplayed()));

        ViewInteraction imageButton3 = onView(allOf(withId(R.id.barcodeBtn)));
        imageButton3.check(matches(isDisplayed()));

        ViewInteraction imageButton4 = onView(allOf(withId(R.id.cartBtn)));
        imageButton4.check(matches(isDisplayed()));

        ViewInteraction imageButton5 = onView(allOf(withId(R.id.cartBtn)));
        imageButton5.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(final Matcher<View> parentMatcher, final int position) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(Description description) {
                description.appendText("Child at position " + position + " in parent ");
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
}
