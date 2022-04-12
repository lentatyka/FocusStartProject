package com.lentatyka.focusstartproject.domain.network

import android.util.Log
import com.lentatyka.focusstartproject.common.State
import com.lentatyka.focusstartproject.data.network.Mapper
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExchangeRatesUseCase @Inject constructor(
    private val repository: RateRepository,
    private val mapper: Mapper
) {
    operator fun invoke() = flow {
        emit(State.Loading)
        Log.d("TAG", "START")
        try {
            val result = repository.getExchangeRates()
            emit(
                State.Success(mapper.mapExchangeRatesDtoToExchangeRates(result))
            )
        } catch (e: Exception) {
            emit(State.Error(e.localizedMessage ?: "unknown error"))
        }
    }
}