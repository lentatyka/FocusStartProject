package com.lentatyka.focusstartproject.data.network

import com.lentatyka.focusstartproject.common.Constants
import com.lentatyka.focusstartproject.data.network.model.ExchangeRatesDto
import retrofit2.http.GET

interface ExchangeRatesApi {

    @GET(Constants.TARGET)
    suspend fun getExchangeRates(): ExchangeRatesDto
}