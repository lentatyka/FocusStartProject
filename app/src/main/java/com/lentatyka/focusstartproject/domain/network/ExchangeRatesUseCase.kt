package com.lentatyka.focusstartproject.domain.network

import android.util.Log
import com.lentatyka.focusstartproject.common.State
import com.lentatyka.focusstartproject.data.network.Mapper
import com.lentatyka.focusstartproject.domain.network.model.ExchangeRates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class ExchangeRatesUseCase @Inject constructor(
    private val repository: RateRepository,
    private val mapper: Mapper
) {
    operator fun invoke(): Flow<State<ExchangeRates>> {
        return flow {
            try {
                emit(State.Loading)
                val result = repository.getExchangeRates()
                emit(
                    State.Success(mapper.mapExchangeRatesDtoToExchangeRates(result))
                )
            } catch (e: Exception) {
                emit(State.Error(e.localizedMessage ?: "unknown error"))
            }
        }
    }
}