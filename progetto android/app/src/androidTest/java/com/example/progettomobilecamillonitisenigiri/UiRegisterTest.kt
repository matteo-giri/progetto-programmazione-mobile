package com.example.progettomobilecamillonitisenigiri

import androidx.lifecycle.Lifecycle
import androidx.test.InstrumentationRegistry.getTargetContext
import androidx.test.espresso.Espresso
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.RootMatchers.withDecorView
import androidx.test.espresso.matcher.ViewMatchers
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.platform.app.InstrumentationRegistry
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.progettomobilecamillonitisenigiri.Auth.LoginActivity
import com.example.progettomobilecamillonitisenigiri.Auth.RegisterActivity
import org.hamcrest.CoreMatchers.allOf

import org.junit.Test
import org.junit.runner.RunWith

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.mockito.AdditionalMatchers.not

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@RunWith(AndroidJUnit4::class)
class UiRegisterTest {
    private lateinit var stringToBetyped: String

    @get:Rule
    var activityRule: ActivityScenarioRule<RegisterActivity>
            = ActivityScenarioRule(RegisterActivity::class.java)

    @Test
    fun checkScrittura() {
        //Controlla che il testo viene inserito nell'edit text
        stringToBetyped = "Prova Scrittura"
        onView(withId(R.id.RegisterEmail)).perform(ViewActions.typeText(stringToBetyped), ViewActions.closeSoftKeyboard())


        onView(withId(R.id.RegisterEmail)).check(matches(withText(stringToBetyped)))

    }

    @Test
    fun checkTornaAlLoginBtnClick(){
        //controlla che il click sul bottone RegisterLoginBtn apra la loginActivity e che al suo interno vi siano i relativi elementi
        onView(withId(R.id.RegisterLoginBtn)).perform(click())

        onView(withId(R.id.LoginPassword)).check(matches(isDisplayed()))
        onView(withId(R.id.LoginEmail)).check(matches(isDisplayed()))

    }





}