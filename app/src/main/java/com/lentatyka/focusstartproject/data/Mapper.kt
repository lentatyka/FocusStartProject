package com.lentatyka.focusstartproject.data

import com.lentatyka.focusstartproject.data.network.model.ExchangeRatesDto
import com.lentatyka.focusstartproject.domain.model.ExchangeRates
import com.lentatyka.focusstartproject.domain.model.Rate
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*

class Mapper {

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
            val formatter = SimpleDateFormat("HH:mm:ss MMM dd yyyy", Locale.getDefault())
            formatter.format(parser.parse(date))
        } catch (e: ParseException) {
            "Bad date"
        }
    }
}