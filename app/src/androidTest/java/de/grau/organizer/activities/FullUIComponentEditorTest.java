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
import org.hamcrest.core.IsInstanceOf;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import de.grau.organizer.R;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.contrib.RecyclerViewActions.actionOnItemAtPosition;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withClassName;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withParent;
import static android.support.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

@LargeTest
@RunWith(AndroidJUnit4.class)
public class FullUIComponentEditorTest {

    @Rule
    public ActivityTestRule<TabActivity> mActivityTestRule = new ActivityTestRule<>(TabActivity.class);

    @Test
    public void fullUIComponentEditorTest() {
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        withParent(allOf(withId(R.id.main_content),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction mDButton = onView(
                allOf(withId(R.id.md_buttonDefaultNegative), withText("Termin"), isDisplayed()));
        mDButton.perform(click());

        pressBack();

        ViewInteraction imageButton = onView(
                allOf(withId(R.id.editor_toolbar_cancel),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        0),
                                0),
                        isDisplayed()));
        imageButton.check(matches(isDisplayed()));

        ViewInteraction imageButton2 = onView(
                allOf(withId(R.id.editor_toolbar_save),
                        childAtPosition(
                                childAtPosition(
                                        withId(R.id.toolbar),
                                        0),
                                2),
                        isDisplayed()));
        imageButton2.check(matches(isDisplayed()));

        ViewInteraction editText = onView(
                allOf(withId(R.id.editor_et_title),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                0),
                        isDisplayed()));
        editText.check(matches(isDisplayed()));

        ViewInteraction editText2 = onView(
                allOf(withId(R.id.editor_description),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                2),
                        isDisplayed()));
        editText2.check(matches(isDisplayed()));

        ViewInteraction switch_ = onView(
                allOf(withId(R.id.sw_allDay),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.ScrollView.class),
                                        0),
                                4),
                        isDisplayed()));
        switch_.check(matches(isDisplayed()));

        ViewInteraction button = onView(
                allOf(withId(R.id.editor_btn_pickdate),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        5),
                                0),
                        isDisplayed()));
        button.check(matches(isDisplayed()));

        ViewInteraction button2 = onView(
                allOf(withId(R.id.editor_btn_picktime),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        5),
                                1),
                        isDisplayed()));
        button2.check(matches(isDisplayed()));

        ViewInteraction button3 = onView(
                allOf(withId(R.id.editor_btn_pickdate_fin),
                        childAtPosition(
                                allOf(withId(R.id.layout_enddate),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                6)),
                                0),
                        isDisplayed()));
        button3.check(matches(isDisplayed()));

        ViewInteraction button4 = onView(
                allOf(withId(R.id.editor_btn_picktime_fin),
                        childAtPosition(
                                allOf(withId(R.id.layout_enddate),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                6)),
                                1),
                        isDisplayed()));
        button4.check(matches(isDisplayed()));

        ViewInteraction button5 = onView(
                allOf(withId(R.id.editor_btn_category),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        8),
                                0),
                        isDisplayed()));
        button5.check(matches(isDisplayed()));

        ViewInteraction button6 = onView(
                allOf(withId(R.id.editor_btn_priority),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        8),
                                1),
                        isDisplayed()));
        button6.check(matches(isDisplayed()));

        ViewInteraction button7 = onView(
                allOf(withId(R.id.editor_btn_picknotifydelay),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        10),
                                0),
                        isDisplayed()));
        button7.check(matches(isDisplayed()));

        ViewInteraction button8 = onView(
                allOf(withId(R.id.editor_btn_chooseaction),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        10),
                                1),
                        isDisplayed()));
        button8.check(matches(isDisplayed()));

        ViewInteraction button9 = onView(
                allOf(withId(R.id.editor_btn_addnote),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        12),
                                0),
                        isDisplayed()));
        button9.check(matches(isDisplayed()));

        ViewInteraction textView = onView(
                allOf(withId(R.id.editor_tags),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        14),
                                0),
                        isDisplayed()));
        textView.check(matches(isDisplayed()));

        ViewInteraction button10 = onView(
                allOf(withId(R.id.editor_btn_tags_delete),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        14),
                                1),
                        isDisplayed()));
        button10.check(matches(isDisplayed()));

        ViewInteraction button11 = onView(
                allOf(withId(R.id.editor_btn_tags_add),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        14),
                                2),
                        isDisplayed()));
        button11.check(matches(isDisplayed()));

        ViewInteraction button12 = onView(
                allOf(withId(R.id.editor_btn_tags_add),
                        childAtPosition(
                                childAtPosition(
                                        IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                        14),
                                2),
                        isDisplayed()));
        button12.check(matches(isDisplayed()));

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.editor_btn_category)));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.md_contentRecyclerView),
                        withParent(withId(R.id.md_contentListViewFrame)),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(R.id.editor_btn_priority)));
        appCompatButton2.perform(scrollTo(), click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.md_contentRecyclerView),
                        withParent(withId(R.id.md_contentListViewFrame)),
                        isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.editor_btn_picknotifydelay)));
        appCompatButton3.perform(scrollTo(), click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.dialog_numberpicker_accept), isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.editor_btn_pickdate)));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.editor_btn_picktime)));
        appCompatButton7.perform(scrollTo(), click());

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.ok), withText("OK"),
                        withParent(allOf(withId(R.id.done_background),
                                withParent(withId(R.id.time_picker_dialog)))),
                        isDisplayed()));
        appCompatButton8.perform(click());

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(R.id.editor_btn_addnote)));
        appCompatButton9.perform(scrollTo(), click());

        /*ViewInteraction editText3 = onView(
                allOf(withText("Note"),
                        childAtPosition(
                                allOf(withId(R.id.editor_notecontainer),
                                        childAtPosition(
                                                IsInstanceOf.<View>instanceOf(android.widget.LinearLayout.class),
                                                11)),
                                0),
                        isDisplayed()));
        editText3.check(matches(withText("Note")));*/

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
