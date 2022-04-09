package com.lentatyka.focusstartproject.domain

import com.lentatyka.focusstartproject.common.State
import com.lentatyka.focusstartproject.data.Mapper
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class GetExchangeRatesUseCase @Inject constructor(
    private val repository: RateRepository,
    private val mapper: Mapper
) {
    operator fun invoke() = flow {
        emit(State.Loading)
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