package com.example.kys;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.core.app.ActivityScenario;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityScenarioRule<MainActivity> activityRule=new ActivityScenarioRule<>(MainActivity.class);
    @Test
    public void testUI()
    {
        onView(withId(R.id.deleteButton)).perform(click());

        onView(withId(R.id.invoice_button)).perform(click());

        onView(withId(R.id.dismiss_button_2)).perform(click());

        onView(withId(R.id.price_button)).perform(click());
        onView(withId(R.id.dismiss_button)).perform(click());

        String TEXT_COMPANY="TESTING .INC";
        String TEXT_CLASS="TEST";
        String TEXT_AMOUNT="1";
        onView(withId(R.id.company_text)).perform(typeText(TEXT_COMPANY));
        onView(withId(R.id.class_text)).perform(typeText(TEXT_CLASS));
        onView(withId(R.id.amount_text)).perform(typeText(TEXT_AMOUNT));
        onView(withId(R.id.dismiss_button)).perform(click());
    }
}
