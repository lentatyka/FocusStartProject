package com.lentatyka.focusstartproject.domain

import com.lentatyka.focusstartproject.common.State
import kotlinx.coroutines.flow.flow

class GetExchangeRatesUseCase(private val repository: RateRepository) {
    operator fun invoke() = flow{
        emit(State.Loading)
        try {
            val rateList = repository.getExchangeRates().map {
                //transform dtoClass -> domainClass
            }
            emit(rateList)
        }catch (e: Exception){
            emit(State.Error(e.localizedMessage ?: "unknown error"))
        }
    }
}