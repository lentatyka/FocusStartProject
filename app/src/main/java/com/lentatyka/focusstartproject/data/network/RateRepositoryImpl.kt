package com.lentatyka.focusstartproject.data.network

import com.lentatyka.focusstartproject.domain.network.RateRepository
import javax.inject.Inject

class RateRepositoryImpl @Inject constructor(
    private val exchangeRatesApi: ExchangeRatesApi
) : RateRepository {
    override suspend fun getExchangeRates() = exchangeRatesApi.getExchangeRates()
}