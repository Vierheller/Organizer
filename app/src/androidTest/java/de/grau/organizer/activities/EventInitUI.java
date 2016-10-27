package de.grau.organizer.activities;


import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.widget.Button;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.text.ParseException;
import java.util.Date;

import de.grau.organizer.R;

import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class EventInitUI {

    @Rule
    public ActivityTestRule<TabActivity> mActivityTestRule = new ActivityTestRule<>(TabActivity.class);

    @Test
    public void eventInitUI() {

        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        withParent(allOf(withId(R.id.main_content),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction mDButton = onView(
                allOf(withId(R.id.md_buttonDefaultNegative), withText("Termin"), isDisplayed()));
        mDButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                withId(R.id.editor_et_title));
        appCompatEditText.perform(scrollTo(), replaceText("Termin #1"), closeSoftKeyboard());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.editor_btn_picktime)));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.ok), withText("OK"),
                        withParent(allOf(withId(R.id.done_background),
                                withParent(withId(R.id.time_picker_dialog)))),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.editor_btn_picktime_fin),
                        withParent(withId(R.id.layout_enddate))));
        appCompatButton3.perform(scrollTo(), click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.ok), withText("OK"),
                        withParent(allOf(withId(R.id.done_background),
                                withParent(withId(R.id.time_picker_dialog)))),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.editor_toolbar_save), isDisplayed()));
        appCompatImageButton.perform(click());


    }

}
