package com.lentatyka.focusstartproject.data.network

import com.google.common.truth.Truth.assertThat
import com.lentatyka.focusstartproject.data.network.model.ExchangeRatesDto
import com.lentatyka.focusstartproject.data.network.model.RateDto
import com.lentatyka.focusstartproject.domain.network.model.ExchangeRates
import com.lentatyka.focusstartproject.domain.network.model.Rate
import org.junit.Test


class MapperTest{

    private val mapper = Mapper()

    @Test
    fun `wrong data format return 'Bad date' string`(){
        val declaredMethod = mapper.javaClass.getDeclaredMethod("parseDate", String::class.java)
        declaredMethod.isAccessible = true
        val params = arrayOfNulls<Any>(1)
        params[0] = ""
        val actual = declaredMethod.invoke(mapper, *params)
        assertThat(actual).isEqualTo("Bad date")
    }

    @Test
    fun `right data format return right string`(){
        val declaredMethod = mapper.javaClass.getDeclaredMethod("parseDate", String::class.java)
        declaredMethod.isAccessible = true
        val params = arrayOfNulls<Any>(1)
        params[0] = "2022-06-02T11:30:00+03:00"
        val actual = declaredMethod.invoke(mapper, *params)
        val expected = "чт, июн. 02, 1:30 PM" // await default Locale  RU
        assertThat(actual).isEqualTo(expected)
    }

    @Test
    fun `ExchangeRateDto object return ExchangeRate mapped equals object`(){
        val exchangeRateDto = ExchangeRatesDto(
            date = "",
            previousDate = "2022-06-02T11:30:00+03:00",
            timestamp = "wrong",
            rate = listOf(
                RateDto("ID", "", "USD", 1, "Dollar", 10.99, 30.99)
            ),
            previousURL = ""
        )
        val expected = ExchangeRates(
            date = "Bad date",
            previousDate = "чт, июн. 02, 1:30 PM",
            timestamp = "Bad date",
            rate = listOf(
                Rate(
                    id = "ID",
                    charCode = "USD",
                    nominal = 1,
                    name = "Dollar",
                    value = 10.99,
                    previous = 30.99
                )
            )
        )
        val actual = mapper.mapExchangeRatesDtoToExchangeRates(exchangeRateDto)
        assertThat(actual).isEqualTo(expected)
    }
    @Test
    fun `ExchangeRateDto object return ExchangeRate mapped not equals object`(){
        val exchangeRateDto = ExchangeRatesDto(
            date = "",
            previousDate = "2022-06-02T11:30:00+03:00",
            timestamp = "wrong",
            rate = listOf(
                RateDto("ID", "", "USD", 1, "Dollar", 10.99, 30.99)
            ),
            previousURL = ""
        )
        val expected = ExchangeRates(
            date = "Bad date",
            previousDate = "чт, июн. 02, 1:30 PM",
            timestamp = "Bad date",
            rate = listOf(
                Rate(
                    id = "",
                    charCode = "",
                    nominal = 1,
                    name = "",
                    value = 10.99,
                    previous = 30.99
                )
            )
        )
        val actual = mapper.mapExchangeRatesDtoToExchangeRates(exchangeRateDto)
        assertThat(actual).isNotEqualTo(expected)
    }
}