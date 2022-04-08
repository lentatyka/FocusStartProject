package com.lentatyka.focusstartproject.domain

interface RateRepository {
    suspend fun getExchangeRates(): List<Any>
}