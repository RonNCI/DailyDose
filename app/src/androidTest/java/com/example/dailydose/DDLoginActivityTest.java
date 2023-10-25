package com.example.dailydose;

import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertTrue;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import org.junit.Rule;
import org.junit.Test;

public class DDLoginActivityTest {
    @Rule
    public ActivityScenarioRule<DDLoginActivity> activityScenarioRule = new ActivityScenarioRule<>(DDLoginActivityTest.class);

    @Test
    public void testValidLogin() {
        ActivityScenario<DDLoginActivity> scenario = activityScenarioRule.getScenario();
        Espresso.onView(withId(R.id.dd_loginEmail)).perform(replaceText("testemail@gmail.com"));
        Espresso.onView(withId(R.id.dd_loginPassword)).perform(replaceText("testpassword"));
        Espresso.onView(withId(R.id.signinButton)).perform(click());
        assertTrue(scenario.getResult().getResultCode() == DDLoginActivity.RESULT_OK);
    }
    @Test
    public void testInvalidLogin() {
        ActivityScenario<DDLoginActivity> scenario = activityScenarioRule.getScenario();
        Espresso.onView(withId(R.id.dd_loginEmail)).perform(replaceText("testusergmail.com"));
        Espresso.onView(withId(R.id.dd_loginPassword)).perform(replaceText("wrongpassword"));
        Espresso.onView(withId(R.id.signinButton)).perform(click());
        assertTrue(scenario.getResult().getResultCode() != DDLoginActivity.RESULT_OK);
    }
}