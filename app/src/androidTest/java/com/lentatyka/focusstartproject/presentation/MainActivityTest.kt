package com.lentatyka.focusstartproject.presentation

import androidx.test.core.app.ActivityScenario
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.action.ViewActions.*
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import com.lentatyka.focusstartproject.R
import com.lentatyka.focusstartproject.TestApplication
import com.lentatyka.focusstartproject.common.Constants.PREFERENCES
import com.lentatyka.focusstartproject.data.network.Mapper
import com.lentatyka.focusstartproject.temp.FakeRepo
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
@LargeTest
class MainActivityTest {

    @get:Rule
    var rule = ActivityScenarioRule(MainActivity::class.java)
    private lateinit var _currentDate: String
    private lateinit var _previousDate: String
    private lateinit var _timestamp: String

    @Before
    fun setUp() {
        //drop shared preferences
        rule.scenario.onActivity {
            it.applicationContext.deleteSharedPreferences(PREFERENCES)
        }

        runBlocking {
            Mapper().mapExchangeRatesDtoToExchangeRates(FakeRepo().invoke()).apply {
                _currentDate = date
                _previousDate = previousDate
                _timestamp = timestamp
            }

        }
    }

    @ExperimentalCoroutinesApi
    @Test
    fun test() = runTest {
        val scenario = ActivityScenario.launch(MainActivity::class.java)
        val appContext = ApplicationProvider.getApplicationContext<TestApplication>()
        //Block of dates

        onView(withId(R.id.tv_date)).check(matches(withText(R.string.actual_date)))
        onView(withId(R.id.tv_date_value)).check(matches(withText(_currentDate)))

        onView(withId(R.id.tv_previous_date)).check(matches(withText(R.string.previous_date)))
        onView(withId(R.id.tv_previous_date_value)).check(matches(withText(_previousDate)))

        onView(withId(R.id.tv_timestamp)).check(matches(withText(R.string.timestamp_date)))
        onView(withId(R.id.tv_timestamp_value)).check(matches(withText(_timestamp)))
        //checkbox
        onView(withId(R.id.chb_auto_update))
            .check(matches(isNotChecked()))
            .perform(click())
            .check(matches(isChecked()))

        //swipe recyclerView
        onView(withId(R.id.swipeLayout)).perform(ViewActions.swipeDown())
        onView(withId(R.id.recycler))
            .perform(RecyclerViewActions.scrollToPosition<MainAdapter.ViewHolder>(0))

        //Recycler items block
        for (list in FakeRepo.listRateDto) {
            onView(withText(list.name)).check(matches(isDisplayed()))
            onView(withText(list.charCode)).check(matches(isDisplayed()))
            onView(withText(appContext.getString(R.string.item_nominal, list.nominal)))
                .check(matches(isDisplayed()))
            onView(withText(appContext.getString(R.string.item_nominal, list.nominal)))
                .check(matches(isDisplayed()))
            onView(withText(appContext.getString(R.string.item_actual, list.value)))
                .check(matches(isDisplayed()))
            onView(withText(appContext.getString(R.string.item_previous, list.previous)))
                .check(matches(isDisplayed()))
        }

        //Converter block
        onView(withId(R.id.et_value)).perform(click()).perform(replaceText("100"))
        onView(withId(R.id.tv_result)).check(matches(withText("100.0000")))
        var current = FakeRepo.listRateDto[0]
        onView(withText(current.name)).perform(click())
        onView(withId(R.id.tv_char_code)).check(matches(withText(current.charCode)))
        onView(withId(R.id.tv_result)).check(matches(withText("2.0000")))
        onView(withId(R.id.et_value)).perform(click()).perform(replaceText("200"))
        onView(withId(R.id.tv_result)).check(matches(withText("4.0000")))
        current = FakeRepo.listRateDto[1]
        onView(withText(current.name)).perform(click())
        onView(withId(R.id.tv_char_code)).check(matches(withText(current.charCode)))
        onView(withId(R.id.tv_result)).check(matches(withText("40.0000")))
        onView(withId(R.id.et_value)).perform(click()).perform(replaceText("1000"))
        onView(withId(R.id.tv_result)).check(matches(withText("200.0000")))


        scenario.close()
    }
}