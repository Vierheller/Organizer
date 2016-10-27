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
import static android.support.test.espresso.Espresso.pressBack;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.action.ViewActions.closeSoftKeyboard;
import static android.support.test.espresso.action.ViewActions.longClick;
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
public class FullEditorEventToEditTest {

    @Rule
    public ActivityTestRule<TabActivity> mActivityTestRule = new ActivityTestRule<>(TabActivity.class);

    public void createInitialFullEvent(){
        ViewInteraction floatingActionButton = onView(
                allOf(withId(R.id.fab),
                        withParent(allOf(withId(R.id.main_content),
                                withParent(withId(android.R.id.content)))),
                        isDisplayed()));
        floatingActionButton.perform(click());

        ViewInteraction mDButton = onView(
                allOf(withId(R.id.md_buttonDefaultNegative), isDisplayed()));
        mDButton.perform(click());

        ViewInteraction appCompatEditText = onView(
                withId(R.id.editor_et_title));
        appCompatEditText.perform(scrollTo(), replaceText("TermToEditTest"), closeSoftKeyboard());

        ViewInteraction appCompatEditText2 = onView(
                withId(R.id.editor_description));
        appCompatEditText2.perform(scrollTo(), replaceText("TermToEditTestDescription"), closeSoftKeyboard());

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
                allOf(withId(R.id.ok),
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
                allOf(withId(R.id.ok),
                        withParent(allOf(withId(R.id.done_background),
                                withParent(withId(R.id.time_picker_dialog)))),
                        isDisplayed()));
        appCompatButton8.perform(click());

        ViewInteraction appCompatButton9 = onView(
                allOf(withId(R.id.editor_btn_category)));
        appCompatButton9.perform(scrollTo(), click());

        ViewInteraction recyclerView = onView(
                allOf(withId(R.id.md_contentRecyclerView),
                        withParent(withId(R.id.md_contentListViewFrame)),
                        isDisplayed()));
        recyclerView.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction appCompatButton10 = onView(
                allOf(withId(R.id.editor_btn_priority)));
        appCompatButton10.perform(scrollTo(), click());

        ViewInteraction recyclerView2 = onView(
                allOf(withId(R.id.md_contentRecyclerView),
                        withParent(withId(R.id.md_contentListViewFrame)),
                        isDisplayed()));
        recyclerView2.perform(actionOnItemAtPosition(1, click()));

        ViewInteraction appCompatButton11 = onView(
                allOf(withId(R.id.editor_btn_picknotifydelay)));
        appCompatButton11.perform(scrollTo(), click());

        ViewInteraction appCompatButton12 = onView(
                allOf(withId(R.id.dialog_numberpicker_accept), isDisplayed()));
        appCompatButton12.perform(click());

        ViewInteraction appCompatButton13 = onView(
                withId(R.id.editor_btn_tags_add));
        appCompatButton13.perform(scrollTo(), click());

        ViewInteraction appCompatEditText3 = onView(
                allOf(withId(android.R.id.input), isDisplayed()));
        appCompatEditText3.perform(replaceText("TermTestEditTag"), closeSoftKeyboard());

        ViewInteraction mDButton2 = onView(
                allOf(withId(R.id.md_buttonDefaultPositive), withText("OK"), isDisplayed()));
        mDButton2.perform(click());

        ViewInteraction appCompatButton14 = onView(
                allOf(withId(R.id.editor_btn_addnote)));
        appCompatButton14.perform(scrollTo(), click());

        ViewInteraction editText = onView(
                allOf(withClassName(is("android.widget.EditText")),
                        withParent(withId(R.id.editor_notecontainer))));
        editText.perform(scrollTo(), replaceText("TermTestEditNotiz"), closeSoftKeyboard());

        ViewInteraction appCompatImageButton = onView(
                allOf(withId(R.id.editor_toolbar_save), isDisplayed()));
        appCompatImageButton.perform(click());

    }

    @Test
    public void fullEditorEventToEditTest() {
        createInitialFullEvent();

        for (int i = 0; i < 10; i++) {
            editEvent(i);
        }
    }

    public void editEvent(int proceedTillStep){
        int currentStep = 0;

        ViewInteraction appCompatTextView = onView(
                allOf(withText("LISTE"), isDisplayed()));
        appCompatTextView.perform(click());


        ViewInteraction linearLayout = onView(
                allOf(childAtPosition(
                        withId(R.id.fragment_list_listview),
                        0),
                        isDisplayed()));
        linearLayout.perform(longClick());

        //ViewInteraction floatingActionButton = onView(
        //        allOf(withId(R.id.task_btn_change), isDisplayed()));
        //floatingActionButton.perform(click());



        ViewInteraction appCompatTextView2 = onView(
                allOf(withId(android.R.id.title), withText("Bearbeiten"), isDisplayed()));
        appCompatTextView2.perform(click());

        ViewInteraction appCompatEditText5 = onView(
                allOf(withId(R.id.editor_et_title)));
        appCompatEditText5.perform(scrollTo(), replaceText("TermToEditTest"+currentStep), closeSoftKeyboard());


        currentStep++;
        if(currentStep <= proceedTillStep){


            ViewInteraction appCompatEditText6 = onView(
                allOf(withId(R.id.editor_description)));
        appCompatEditText6.perform(scrollTo(), replaceText("TermToEditTestDescription"+currentStep), closeSoftKeyboard());

            currentStep++;
            if(currentStep <= proceedTillStep){

        ViewInteraction appCompatButton15 = onView(
                allOf(withId(R.id.editor_btn_pickdate)));
        appCompatButton15.perform(scrollTo(), click());

        ViewInteraction appCompatButton16 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton16.perform(click());

                currentStep++;
                if(currentStep <= proceedTillStep){

        ViewInteraction appCompatButton17 = onView(
                allOf(withId(R.id.editor_btn_picktime)));
        appCompatButton17.perform(scrollTo(), click());

        ViewInteraction appCompatButton18 = onView(
                allOf(withId(R.id.ok), withText("OK"),
                        withParent(allOf(withId(R.id.done_background),
                                withParent(withId(R.id.time_picker_dialog)))),
                        isDisplayed()));
        appCompatButton18.perform(click());

                    currentStep++;
                    if(currentStep <= proceedTillStep){

        ViewInteraction appCompatButton19 = onView(
                allOf(withId(R.id.editor_btn_pickdate_fin),
                        withParent(withId(R.id.layout_enddate))));
        appCompatButton19.perform(scrollTo(), click());

        ViewInteraction appCompatButton20 = onView(
                allOf(withId(android.R.id.button1), withText("OK"),
                        withParent(allOf(withClassName(is("android.widget.LinearLayout")),
                                withParent(withClassName(is("android.widget.LinearLayout"))))),
                        isDisplayed()));
        appCompatButton20.perform(click());

                        currentStep++;
                        if(currentStep <= proceedTillStep){

        ViewInteraction appCompatButton21 = onView(
                allOf(withId(R.id.editor_btn_picktime_fin),
                        withParent(withId(R.id.layout_enddate))));
        appCompatButton21.perform(scrollTo(), click());

        ViewInteraction appCompatButton22 = onView(
                allOf(withId(R.id.ok), withText("OK"),
                        withParent(allOf(withId(R.id.done_background),
                                withParent(withId(R.id.time_picker_dialog)))),
                        isDisplayed()));
        appCompatButton22.perform(click());

                            currentStep++;
                            if(currentStep <= proceedTillStep){

        ViewInteraction appCompatButton23 = onView(
                allOf(withId(R.id.editor_btn_category)));
        appCompatButton23.perform(scrollTo(), click());

        ViewInteraction recyclerView3 = onView(
                allOf(withId(R.id.md_contentRecyclerView),
                        withParent(withId(R.id.md_contentListViewFrame)),
                        isDisplayed()));
        recyclerView3.perform(actionOnItemAtPosition(2, click()));

                                currentStep++;
                                if(currentStep <= proceedTillStep){

        ViewInteraction appCompatButton24 = onView(
                allOf(withId(R.id.editor_btn_picknotifydelay)));
        appCompatButton24.perform(scrollTo(), click());

        ViewInteraction appCompatButton25 = onView(
                allOf(withId(R.id.dialog_numberpicker_accept), isDisplayed()));
        appCompatButton25.perform(click());

                                    currentStep++;
                                    if(currentStep <= proceedTillStep){

        ViewInteraction editText2 = onView(
                allOf(withText("TermTestEditNotiz"),
                        withParent(withId(R.id.editor_notecontainer))));
        editText2.perform(scrollTo(), replaceText("TermTestEditNotiz"), closeSoftKeyboard());


                                        currentStep++;
                                        if(currentStep <= proceedTillStep){

        ViewInteraction appCompatButton26 = onView(
                withId(R.id.editor_btn_tags_delete));
        appCompatButton26.perform(scrollTo(), click());

        ViewInteraction recyclerView4 = onView(
                allOf(withId(R.id.md_contentRecyclerView),
                        withParent(withId(R.id.md_contentListViewFrame)),
                        isDisplayed()));
        recyclerView4.perform(actionOnItemAtPosition(0, click()));

        ViewInteraction mDButton3 = onView(
                allOf(withId(R.id.md_buttonDefaultPositive), withText("OK"), isDisplayed()));
        mDButton3.perform(click());

        currentStep++;
        if(currentStep <= proceedTillStep) {

            ViewInteraction appCompatButton27 = onView(
                    allOf(withId(R.id.editor_btn_addnote)));
            appCompatButton27.perform(scrollTo(), click());

            ViewInteraction editText3 = onView(
                    allOf(withClassName(is("android.widget.EditText")),
                            withParent(withId(R.id.editor_notecontainer))));
            editText3.perform(scrollTo(), replaceText("Notiz "+currentStep), closeSoftKeyboard());

        }}}}}}}}}}


        ViewInteraction appCompatImageButton2 = onView(
                allOf(withId(R.id.editor_toolbar_save), isDisplayed()));
        appCompatImageButton2.perform(click());

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
