package com.example.stepapp

import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.runner.AndroidJUnit4
import com.example.stepapp.view.MainActivity
import org.hamcrest.Matchers
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var activityRule : ActivityScenarioRule<MainActivity> = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun checkProgressShow(){
        onView(withId(R.id.progressBar)).check(matches(isDisplayed()))
    }


    @Test
    fun checkStepsShow(){
        onView(withId(R.id.progressBar)).check(matches(isDisplayed()))
        onView(
            Matchers.allOf(
                withText(R.string.stepCount),
                withText(" 0 /10000 kroków"),
                Matchers.allOf(
                    isDisplayed()
                )
            )
        )
    }
}