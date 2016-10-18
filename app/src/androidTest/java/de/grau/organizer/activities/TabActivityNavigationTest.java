package de.grau.organizer.activities;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.grau.organizer.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TabActivityNavigationTest {

    @Rule
    public ActivityTestRule<TabActivity> mActivityTestRule = new ActivityTestRule<>(TabActivity.class);

    @Test
    public void tabActivityNavigationTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        withParent(allOf(withId(R.id.main_content),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction mDButton = onView(
                allOf(withId(R.id.md_buttonDefaultNegative), withText("Termin"), isDisplayed()));
        mDButton.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.editor_toolbar_cancel), isDisplayed()));
        appCompatImageButton.perform(click());

        ViewInteraction appCompatTextView = onView(
                allOf(withText("WEEK"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction floatingActionButton2 = onView(
                allOf(withId(R.id.fab),
                        withParent(allOf(withId(R.id.main_content),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        floatingActionButton2.perform(click());

        ViewInteraction mDButton2 = onView(
                allOf(withId(R.id.md_buttonDefaultNegative), withText("Termin"), isDisplayed()));
        mDButton2.perform(click());

        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.editor_toolbar_cancel), isDisplayed()));
        appCompatImageButton2.perform(click());

        ViewInteraction appCompatTextView2 = onView(
                allOf(withText("LIST"), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction floatingActionButton3 = onView(
                allOf(withId(R.id.fab),
                        withParent(allOf(withId(R.id.main_content),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        floatingActionButton3.perform(click());

        ViewInteraction mDButton3 = onView(
                allOf(withId(R.id.md_buttonDefaultNegative), withText("Termin"), isDisplayed()));
        mDButton3.perform(click());

        ViewInteraction appCompatImageButton3 = onView(
                allOf(withId(R.id.editor_toolbar_cancel), isDisplayed()));
        appCompatImageButton3.perform(click());

        ViewInteraction appCompatImageView = onView(
                allOf(withClassName(is("android.support.v7.widget.AppCompatImageView")), withContentDescription("Search"),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withId(R.id.app_bar_search)))),
                        isDisplayed()));
        appCompatImageView.perform(click());

        ViewInteraction appCompatImageView2 = onView(
                allOf(withClassName(is("android.support.v7.widget.AppCompatImageView")), withContentDescription("Clear query"),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatImageView2.perform(click());

        ViewInteraction appCompatTextView3 = onView(
                allOf(withText("MONTH"), isDisplayed()));
        appCompatTextView3.perform(click());

    }

}
