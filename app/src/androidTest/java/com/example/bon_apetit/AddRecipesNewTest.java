package com.example.bon_apetit;


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
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class AddRecipesNewTest {
    //Done by R.W.A.D.U. Rajapakshe
    //ID :- IT19375342

    @Rule
    public ActivityTestRule<AddRecipesNew> mActivityTestRule = new ActivityTestRule<>(AddRecipesNew.class);

    @Test
    public void addRecipesNewTest() {
        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatEditText = onView(
                allOf(withId(R.id.recipe),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                13),
                        isDisplayed()));
        appCompatEditText.perform(replaceText("Pizza"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                allOf(withId(R.id.txtServings),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                14),
                        isDisplayed()));
        appCompatEditText2.perform(replaceText("4"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(R.id.txtAmount),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                16),
                        isDisplayed()));
        appCompatEditText3.perform(replaceText("3000.00"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.chooseFile), withText("Choose File"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatButton.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.chooseFile), withText("Choose File"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatButton2.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.chooseFile), withText("Choose File"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatButton3.perform(click());

        // Added a sleep statement to match the app's execution delay.
        // The recommended way to handle such scenarios is to use Espresso idling resources:
        // https://google.github.io/android-testing-support-library/docs/espresso/idling-resource/index.html
        try {
            Thread.sleep(7000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        ViewInteraction constraintLayout = onView(
                allOf(childAtPosition(
                        allOf(withId(android.R.id.content),
                                childAtPosition(
                                        withId(R.id.action_bar_root),
                                        1)),
                        0),
                        isDisplayed()));
        constraintLayout.perform(click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(R.id.methodEnter),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                10),
                        isDisplayed()));
        appCompatEditText4.perform(replaceText("Add Cheese"), closeSoftKeyboard());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.chooseFile), withText("Choose File"),
                        childAtPosition(
                                childAtPosition(
                                        withId(android.R.id.content),
                                        0),
                                4),
                        isDisplayed()));
        appCompatButton4.perform(click());
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
