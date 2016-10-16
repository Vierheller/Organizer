package de.grau.organizer.activities;


import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;
import android.test.suitebuilder.annotation.LargeTest;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.grau.organizer.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withContentDescription;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class TabActivityTest {

    @Rule
    public ActivityTestRule<TabActivity> mActivityTestRule = new ActivityTestRule<>(TabActivity.class);

    @Test
    public void tabActivityTest() {
        ViewInteraction appCompatTextView = onView(
                allOf(withText("WEEK"), isDisplayed()));
        appCompatTextView.perform(click());

        ViewInteraction view = onView(
                allOf(withId(R.id.weekView),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        1),
                                0),
                        isDisplayed()));
        view.check(matches(isDisplayed()));

        ViewInteraction appCompatTextView2 = onView(
                allOf(withText("LIST"), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction listView = onView(
                allOf(withId(R.id.fragment_list_listview),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        1),
                                0),
                        isDisplayed()));
        listView.check(matches(isDisplayed()));

        ViewInteraction appCompatTextView3 = onView(
                allOf(withText("MONTH"), isDisplayed()));
        appCompatTextView3.perform(click());

        ViewInteraction calendarPagerView = onView(
                allOf(withContentDescription("Calendar"),
                        childAtPosition(
                                allOf(withId(R.id.mcv_pager),
                                        childAtPosition(
                                                withId(R.id.calendarView),
                                                1)),
                                1),
                        isDisplayed()));
        calendarPagerView.check(matches(isDisplayed()));

        ViewInteraction listView2 = onView(
                allOf(withId(R.id.month_list_view),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.container),
                                        0),
                                1),
                        isDisplayed()));
        listView2.check(matches(isDisplayed()));

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
