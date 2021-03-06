package com.lentatyka.focusstartproject.domain.network

import com.lentatyka.focusstartproject.data.network.model.ExchangeRatesDto

interface RateRepository {
    suspend operator fun invoke(): ExchangeRatesDto
}