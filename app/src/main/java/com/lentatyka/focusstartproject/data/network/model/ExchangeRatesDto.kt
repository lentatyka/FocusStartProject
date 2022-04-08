package com.lentatyka.focusstartproject.data.network.model

data class ExchangeRatesDto(
    val date: String,
    val previousDate: String,
    val previousURL: String,
    val timestamp: String,
    val rate: List<RateDto>
)
