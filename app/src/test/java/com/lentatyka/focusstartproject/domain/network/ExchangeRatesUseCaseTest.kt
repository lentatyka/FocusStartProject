package com.lentatyka.focusstartproject.domain.network

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.lentatyka.focusstartproject.common.State
import com.lentatyka.focusstartproject.data.network.Mapper
import com.lentatyka.focusstartproject.data.network.model.ExchangeRatesDto
import com.lentatyka.focusstartproject.data.network.model.RateDto
import com.lentatyka.focusstartproject.domain.network.model.ExchangeRates
import com.lentatyka.focusstartproject.domain.network.model.Rate
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock
import retrofit2.HttpException
import kotlin.time.ExperimentalTime

@ExperimentalTime
@ExperimentalCoroutinesApi
class ExchangeRatesUseCaseTest {

    private val fakeRepository = mock(RateRepository::class.java)
    private val useCase = ExchangeRatesUseCase(fakeRepository, Mapper())

    @Test
    fun `flow emit success State`() = runTest {
        val exchangeRateDto = ExchangeRatesDto(
            date = "",
            previousDate = "",
            timestamp = "",
            rate = listOf(
                RateDto("ID", "", "USD", 1, "Dollar", 10.99, 30.99)
            ),
            previousURL = ""
        )
        val exchangeRates = ExchangeRates(
            date = "Bad date",
            previousDate = "Bad date",
            timestamp = "Bad date",
            rate = listOf(
                Rate("ID", "USD", 1, "Dollar", 10.99, 30.99)
            )
        )
        `when`(fakeRepository.getExchangeRates()).thenReturn(exchangeRateDto)
        val flow = useCase.invoke()
        flow.test {
            assertThat(awaitItem()).isEqualTo(State.Loading)
            val actual = awaitItem()
            assertThat(actual is State.Success).isTrue()
            awaitComplete()
            actual as State.Success
            assertThat(actual.data).isEqualTo(exchangeRates)
        }
    }

    @Test
    fun `flow emit error State`() = runTest {
        `when`(fakeRepository.getExchangeRates()).thenThrow(HttpException::class.java)
        val flow = useCase.invoke()
        flow.test {
            assertThat(awaitItem()).isEqualTo(State.Loading)
            val actual = awaitItem()
            assertThat(actual is State.Error).isTrue()
            awaitComplete()
            actual as State.Error
            assertThat(actual.message).isEqualTo("unknown error")
        }
    }
}