package com.lentatyka.focusstartproject.data.network

import com.lentatyka.focusstartproject.data.network.model.ExchangeRatesDto
import com.lentatyka.focusstartproject.domain.network.model.ExchangeRates
import com.lentatyka.focusstartproject.domain.network.model.Rate
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class Mapper @Inject constructor() {

    fun mapExchangeRatesDtoToExchangeRates(excRates: ExchangeRatesDto): ExchangeRates {
        return ExchangeRates(
            date = parseDate(excRates.date),
            previousDate = parseDate(excRates.previousDate),
            timestamp = parseDate(excRates.timestamp),
            rate = excRates.rate.map {
                Rate(
                    id = it.id,
                    charCode = it.charCode,
                    nominal = it.nominal,
                    name = it.name,
                    value = it.value,
                    previous = it.previous
                )
            }
        )
    }

    private fun parseDate(date: String): String {
        return try {
            val parser = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX", Locale.getDefault())
            val formatter = SimpleDateFormat("EEE, MMM dd, h:mm a", Locale.getDefault())
            formatter.format(parser.parse(date))
        } catch (e: ParseException) {
            "Bad date"
        }
    }
}