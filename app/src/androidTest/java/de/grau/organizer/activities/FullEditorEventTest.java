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
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.replaceText;
import static android.support.test.espresso.action.ViewActions.scrollTo;
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
public class FullEditorEventTest {

    @Rule
    public ActivityTestRule<TabActivity> mActivityTestRule = new ActivityTestRule<>(TabActivity.class);

    @Test
    public void fullEditorEventTest() {
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
        appCompatEditText.perform(scrollTo(), click());

        ViewInteraction appCompatEditText2 = onView(
                withId(R.id.editor_et_title));
        appCompatEditText2.perform(scrollTo(), replaceText("Test"), closeSoftKeyboard());

        ViewInteraction appCompatEditText3 = onView(
                withId(R.id.editor_description));
        appCompatEditText3.perform(scrollTo(), replaceText("Test"), closeSoftKeyboard());

        ViewInteraction switch_ = onView(
                allOf(withId(R.id.sw_allDay)));
        switch_.perform(scrollTo(), click());

        ViewInteraction switch_2 = onView(
                allOf(withId(R.id.sw_allDay)));
        switch_2.perform(scrollTo(), click());

        ViewInteraction appCompatButton = onView(
                allOf(withId(R.id.editor_btn_pickdate)));
        appCompatButton.perform(scrollTo(), click());

        ViewInteraction appCompatButton2 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton2.perform(click());

        ViewInteraction appCompatButton3 = onView(
                allOf(withId(R.id.editor_btn_picktime)));
        appCompatButton3.perform(scrollTo(), click());

        ViewInteraction appCompatButton4 = onView(
                allOf(withId(R.id.ok), withText("OK"),
                        withParent(allOf(withId(R.id.done_background),
                                withParent(withId(R.id.time_picker_dialog)))),
                        isDisplayed()));
        appCompatButton4.perform(click());

        ViewInteraction appCompatButton5 = onView(
                allOf(withId(R.id.editor_btn_pickdate_fin),
                        withParent(withId(R.id.layout_enddate))));
        appCompatButton5.perform(scrollTo(), click());

        ViewInteraction appCompatButton6 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton6.perform(click());

        ViewInteraction appCompatButton7 = onView(
                allOf(withId(R.id.editor_btn_picktime_fin),
                        withParent(withId(R.id.layout_enddate))));
        appCompatButton7.perform(scrollTo(), click());

        ViewInteraction appCompatButton8 = onView(
                allOf(withId(R.id.ok), withText("OK"),
                        withParent(allOf(withId(R.id.done_background),
                                withParent(withId(R.id.time_picker_dialog)))),
                        isDisplayed()));
        appCompatButton8.perform(click());

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(R.id.editor_btn_category), withText("Allgemein")));
        appCompatButton9.perform(scrollTo(), click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.md_contentRecyclerView),
                        withParent(withId(R.id.md_contentListViewFrame)),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction appCompatButton10 = onView(
                allOf(withId(R.id.editor_btn_priority), withText("Priorität 4")));
        appCompatButton10.perform(scrollTo(), click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.md_contentRecyclerView),
                        withParent(withId(R.id.md_contentListViewFrame)),
                        isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction appCompatButton11 = onView(
                allOf(withId(R.id.editor_btn_picknotifydelay), withText("none")));
        appCompatButton11.perform(scrollTo(), click());

        ViewInteraction appCompatButton12 = onView(
                allOf(withId(R.id.dialog_numberpicker_accept), withText("Accept"), isDisplayed()));
        appCompatButton12.perform(click());

        ViewInteraction appCompatButton14 = onView(
                allOf(withId(R.id.editor_btn_addnote), withText("Notiz hinzufügen")));
        appCompatButton14.perform(scrollTo(), click());

        ViewInteraction editText = onView(
                allOf(withClassName(is("android.widget.EditText")),
                        withParent(withId(R.id.editor_notecontainer))));
        editText.perform(scrollTo(), replaceText("Test"), closeSoftKeyboard());

        ViewInteraction appCompatButton15 = onView(
                allOf(withId(R.id.editor_btn_tags_add)));
        appCompatButton15.perform(scrollTo(), click());

        ViewInteraction appCompatEditText4 = onView(
                allOf(withId(android.R.id.input), isDisplayed()));
        appCompatEditText4.perform(replaceText("Test"), closeSoftKeyboard());

        ViewInteraction mDButton2 = onView(
                allOf(withId(R.id.md_buttonDefaultPositive), withText("OK"), isDisplayed()));
        mDButton2.perform(click());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.editor_toolbar_save), isDisplayed()));
        appCompatImageButton.perform(click());

    }

}
