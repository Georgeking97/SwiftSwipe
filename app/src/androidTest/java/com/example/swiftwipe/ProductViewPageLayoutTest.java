package com.example.swiftwipe;


import android.os.SystemClock;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class ProductViewPageLayoutTest {

    @Rule
    public ActivityTestRule<Login> mActivityTestRule = new ActivityTestRule<>(Login.class);

    @Test
    public void productViewPageLayoutTest() {
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

        ViewInteraction cardView = onView(
                allOf(withId(R.id.search),
                        childAtPosition(
                                childAtPosition(
                                        withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                        0),
                                0),
                        isDisplayed()));
        cardView.perform(click());

        SystemClock.sleep(1000);

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.result_list),
                        childAtPosition(
                                withClassName(is("androidx.constraintlayout.widget.ConstraintLayout")),
                                1)));

        recyclerView.perform(actionOnItemAtPosition(0, click()));

        SystemClock.sleep(1000);

        ViewInteraction imageView = onView(
                allOf(withId(R.id.productImage),
                        withParent(allOf(withId(R.id.productNameTxt),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withText("Product:"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView.check(matches(withText("Product:")));

        ViewInteraction textView2 = onView(
                allOf(withText("Price:"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView2.check(matches(withText("Price:")));

        ViewInteraction textView3 = onView(
                allOf(withId(R.id.productName), withText("shirt"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView3.check(matches(withText("shirt")));

        ViewInteraction textView4 = onView(
                allOf(withId(R.id.productPriceTxt), withText("€39.99"),
                        withParent(withParent(IsInstanceOf.<View>instanceOf(android.widget.FrameLayout.class))),
                        isDisplayed()));
        textView4.check(matches(withText("€39.99")));

        ViewInteraction radioButton = onView(
                allOf(withId(R.id.radioButtonS), withText("Small"),
                        withParent(allOf(withId(R.id.radioGroupSizes),
                                withParent(withId(R.id.productNameTxt)))),
                        isDisplayed()));
        radioButton.check(matches(isDisplayed()));

        ViewInteraction radioButton2 = onView(
                allOf(withId(R.id.radioButtonM), withText("Medium"),
                        withParent(allOf(withId(R.id.radioGroupSizes),
                                withParent(withId(R.id.productNameTxt)))),
                        isDisplayed()));
        radioButton2.check(matches(isDisplayed()));

        ViewInteraction radioButton3 = onView(
                allOf(withId(R.id.radioButtonL), withText("Large"),
                        withParent(allOf(withId(R.id.radioGroupSizes),
                                withParent(withId(R.id.productNameTxt)))),
                        isDisplayed()));
        radioButton3.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.addBtn), withText("ADD TO CART"),
                        withParent(allOf(withId(R.id.productNameTxt),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.homeBtn),
                        withParent(allOf(withId(R.id.relativeLayout),
                                withParent(withId(R.id.productNameTxt)))),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.searchBtn),
                        withParent(allOf(withId(R.id.relativeLayout),
                                withParent(withId(R.id.productNameTxt)))),
                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

        ViewInteraction imageButton3 = onView(
                allOf(withId(R.id.barcodeBtn),
                        withParent(allOf(withId(R.id.relativeLayout),
                                withParent(withId(R.id.productNameTxt)))),
                        isDisplayed()));
        imageButton3.check(matches(isDisplayed()));

        ViewInteraction imageButton4 = onView(
                allOf(withId(R.id.cartBtn),
                        withParent(allOf(withId(R.id.relativeLayout),
                                withParent(withId(R.id.productNameTxt)))),
                        isDisplayed()));
        imageButton4.check(matches(isDisplayed()));

        ViewInteraction imageButton5 = onView(
                allOf(withId(R.id.cartBtn),
                        withParent(allOf(withId(R.id.relativeLayout),
                                withParent(withId(R.id.productNameTxt)))),
                        isDisplayed()));
        imageButton5.check(matches(isDisplayed()));
    }

    private static Matcher<View> childAtPosition(
            final Matcher<View> parentMatcher, final int position) {

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
