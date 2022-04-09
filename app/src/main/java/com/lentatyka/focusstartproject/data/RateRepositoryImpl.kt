package com.lentatyka.focusstartproject.data

import com.lentatyka.focusstartproject.data.network.ExchangeRatesApi
import com.lentatyka.focusstartproject.domain.RateRepository
import javax.inject.Inject

class RateRepositoryImpl @Inject constructor(private val exchangeRatesApi: ExchangeRatesApi): RateRepository {
    override suspend fun getExchangeRates() = exchangeRatesApi.getExchangeRates()
}