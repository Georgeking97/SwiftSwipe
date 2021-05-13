package com.example.swiftwipe;


import androidx.test.espresso.ViewInteraction;
import androidx.test.filters.LargeTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withParent;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class LoginPageLayoutTest {

    @Rule
    public ActivityTestRule<Login> mActivityTestRule = new ActivityTestRule<>(Login.class);

    @Test
    public void loginPageLayoutTest() {
        ViewInteraction editText = onView(
                allOf(withId(R.id.Email)));
        editText.check(matches(withHint("Email")));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.password)));
        editText2.check(matches(withHint("Password")));

        ViewInteraction button = onView(
                allOf(withId(R.id.loginBtn), withText("LOGIN")));
        button.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.createBtn), withText("NEW HERE? CREATE ACCOUNT")));
        button3.check(matches(isDisplayed()));

        ViewInteraction imageView = onView(
                allOf(withId(R.id.logo),
                        withParent(withParent(withId(android.R.id.content))),
                        isDisplayed()));
        imageView.check(matches(isDisplayed()));

    }
}
