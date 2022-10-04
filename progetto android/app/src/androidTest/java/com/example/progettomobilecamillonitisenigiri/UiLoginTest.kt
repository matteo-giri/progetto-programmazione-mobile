package com.example.progettomobilecamillonitisenigiri

import androidx.lifecycle.Lifecycle
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.progettomobilecamillonitisenigiri.Auth.LoginActivity
import com.example.progettomobilecamillonitisenigiri.Auth.RegisterActivity

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class UiLoginTest {
    private lateinit var stringToBetyped: String
    @get:Rule
    var activityRule: ActivityScenarioRule<LoginActivity>
            = ActivityScenarioRule(LoginActivity::class.java)



    @Test
    fun checkLoginPageTitle(){
        //controlla che il titolo sia giusto
        onView(withText("Corsi per Tutti")).check(matches(withId(R.id.titoloApp)))
    }

    @Test
    fun checkLogin(){
        //controlla che le editText siano vuote
        onView(withId(R.id.LoginEmail)).check(matches(withText("")))
        onView(withId(R.id.LoginPassword)).check(matches(withText("")))

    }

    @Test
    fun checkRegisterBtnClickOnLoginActivity(){
        //controlla che il click sul bottone LoginRegisterBtn apra la registerActivity e che al suo interno vi siano i relativi elementi
        onView(withId(R.id.LoginRegisterBtn)).perform(click())
        onView(withId(R.id.RegisterEmail)).check(matches(isDisplayed()))
        onView(withId(R.id.nomeRegistrazione)).check(matches(isDisplayed()))
        onView(withId(R.id.first_name)).check(matches(isDisplayed()))
        onView(withId(R.id.last_name)).check(matches(isDisplayed()))


    }



}