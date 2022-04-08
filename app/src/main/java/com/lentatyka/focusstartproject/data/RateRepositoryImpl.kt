package com.lentatyka.focusstartproject.data

import com.lentatyka.focusstartproject.data.network.ExchangeRatesApi
import com.lentatyka.focusstartproject.domain.RateRepository

class RateRepositoryImpl(private val exchangeRatesApi: ExchangeRatesApi): RateRepository {
    override suspend fun getExchangeRates() = exchangeRatesApi.getExchangeRates()
}