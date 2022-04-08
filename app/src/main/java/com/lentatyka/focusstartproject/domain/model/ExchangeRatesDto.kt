package com.lentatyka.focusstartproject.domain.model

data class ExchangeRates(
    val date: String,
    val previousDate: String,
    val timestamp: String,
    val rate: List<Rate>
)
