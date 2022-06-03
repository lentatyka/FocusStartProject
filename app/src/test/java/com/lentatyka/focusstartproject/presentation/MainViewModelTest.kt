package com.lentatyka.focusstartproject.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.lentatyka.focusstartproject.MainCoroutineRule
import com.lentatyka.focusstartproject.common.State
import com.lentatyka.focusstartproject.data.network.Mapper
import com.lentatyka.focusstartproject.data.network.model.ExchangeRatesDto
import com.lentatyka.focusstartproject.domain.network.ExchangeRatesUseCase
import com.lentatyka.focusstartproject.domain.network.RateRepository
import com.lentatyka.focusstartproject.domain.network.model.ExchangeRates
import com.lentatyka.focusstartproject.domain.network.model.Rate
import com.lentatyka.focusstartproject.domain.preferences.PreferencesHelper
import com.lentatyka.focusstartproject.domain.preferences.PreferencesUseCase
import com.lentatyka.focusstartproject.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.HttpException
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class MainViewModelTest{

    private lateinit var viewModel: MainViewModel
    private val fakeRepository = mock(RateRepository::class.java)
    private val fakePreferences = mock(PreferencesHelper::class.java)

    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @Before
    fun setUp(){
        viewModel = MainViewModel(
            ExchangeRatesUseCase(fakeRepository, Mapper()),
            PreferencesUseCase(fakePreferences)
        )
    }

    @Test
    fun `set Rate return equals value`(){
        viewModel.setRate(Rate("ID", "USD", 1, "DOLLAR", 1.0, 1.0))
        val actual = viewModel.rate.getOrAwaitValue()
        assertThat(actual).isEqualTo(
            Rate("ID", "USD", 1, "DOLLAR", 1.0, 1.0))
    }

    @Test
    fun `set converted Rate return expected equals`(){
        val expected = 2.0
        viewModel.convertRate("2.0")
        var actual = viewModel.result.getOrAwaitValue()
        //rate livedata has not been init
        assertThat(actual).isEqualTo(expected)

        val rate = Rate("ID", "USD", 2, "DOLLAR", 2.0, 1.0)
        viewModel.setRate(rate) //init rate
        viewModel.convertRate("2.0")
        actual = viewModel.result.getOrAwaitValue()
        assertThat(actual).isEqualTo(expected)
    }

    @Throws(NumberFormatException::class)
    @Test
    fun `set converted Rate return expected is not equals`(){
        val expected = 2.0
        viewModel.convertRate("1.0")
        var actual = viewModel.result.getOrAwaitValue()
        //rate livedata has not been init
        assertThat(actual).isNotEqualTo(expected)
        val rate = Rate("ID", "USD", 2, "DOLLAR", 2.0, 1.0)

        viewModel.setRate(rate) //init rate
        viewModel.convertRate("1.0")
        actual = viewModel.result.getOrAwaitValue()
        assertThat(actual).isNotEqualTo(expected)

        viewModel.convertRate("")
        actual = viewModel.result.getOrAwaitValue()
        assertThat(actual).isNotEqualTo(expected)
    }

    @Test
    fun `emit state return success value`() =runTest{
        val exchangeRatesDto = ExchangeRatesDto(
            "", "", "","", emptyList()
        )
        val expected = ExchangeRates(
            "Bad date", "Bad date", "Bad date", emptyList())
        `when`(fakeRepository.getExchangeRates()).thenReturn(exchangeRatesDto)

        viewModel.updateExchangeRates()
        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(State.Loading)
            val actual = awaitItem()
            cancelAndConsumeRemainingEvents()
            assertThat(actual is State.Success).isTrue()
            actual as State.Success
            assertThat(actual.data).isEqualTo(expected)
        }
    }

    @Test
    fun `emit state return error value`() =runTest{

        `when`(fakeRepository.getExchangeRates()).thenThrow(HttpException::class.java)

        viewModel.updateExchangeRates()
        viewModel.state.test {
            assertThat(awaitItem()).isEqualTo(State.Loading)
            val actual = awaitItem()
            cancelAndConsumeRemainingEvents()
            assertThat(actual is State.Error).isTrue()
            actual as State.Error
            assertThat(actual.message).isEqualTo("unknown error")
        }
    }
}