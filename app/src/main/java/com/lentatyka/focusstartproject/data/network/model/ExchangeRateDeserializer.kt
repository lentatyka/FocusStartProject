package com.lentatyka.focusstartproject.data.network.model

import com.google.gson.Gson
import com.google.gson.JsonDeserializationContext
import com.google.gson.JsonDeserializer
import com.google.gson.JsonElement
import java.lang.reflect.Type
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ExchangeRateDeserializer @Inject constructor() : JsonDeserializer<ExchangeRatesDto> {
    override fun deserialize(
        json: JsonElement?,
        typeOfT: Type?,
        context: JsonDeserializationContext?
    ): ExchangeRatesDto? {
        val jsonObject = json?.asJsonObject
        return jsonObject?.let {
            val date = it.get("Date").asString
            val prevDate = it.get("PreviousDate").asString
            val prevUrl = it.get("PreviousURL").asString
            val timestamp = it.get("Timestamp").asString
            val rate = it.get("Valute").asJsonObject

            //"Valute" object look like enum at original json object. So we need to parse it as list
            val gson = Gson()
            val rateList = mutableListOf<RateDto>()
            rate.entrySet().forEach { mask ->
                val value = gson.fromJson(mask.value.toString(), RateDto::class.java)
                rateList += value
            }
            //So looks better then ExcRatesDato( ...., SomeClass)
            ExchangeRatesDto(date, prevDate, prevUrl, timestamp, rateList)
        }
    }
}