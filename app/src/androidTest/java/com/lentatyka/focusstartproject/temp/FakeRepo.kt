package com.lentatyka.focusstartproject.temp

import com.lentatyka.focusstartproject.data.network.model.ExchangeRatesDto
import com.lentatyka.focusstartproject.data.network.model.RateDto
import com.lentatyka.focusstartproject.domain.network.RateRepository
import javax.inject.Inject

class FakeRepo @Inject constructor() : RateRepository {
    override suspend fun invoke(): ExchangeRatesDto {
        return ExchangeRatesDto(
            timestamp, "", "", "", listRateDto)
    }

    companion object {
        val listRateDto = listOf(
            RateDto("1", "1", "USD", 1, "Dollar", 50.0, 100.0),
            RateDto("2", "2", "EUR", 2, "Euro", 10.00, 50.00),
            RateDto("1", "3", "STL", 3, "Stallone", 20.0, 10.0)

        )
        const val timestamp ="2022-06-08T11:30:00+03:00"
        const val convertedTime = "Wed, Jun 08, 8:30 AM"
    }
}