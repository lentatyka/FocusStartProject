package com.lentatyka.focusstartproject.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lentatyka.focusstartproject.common.State
import com.lentatyka.focusstartproject.domain.GetExchangeRatesUseCase
import com.lentatyka.focusstartproject.domain.model.ExchangeRates
import com.lentatyka.focusstartproject.domain.model.Rate
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewModel @Inject constructor(
    private val getExchangeRatesUseCase: GetExchangeRatesUseCase
) : ViewModel() {
    private val _state = MutableLiveData<State<ExchangeRates>>()
    val state: LiveData<State<ExchangeRates>> get() = _state

    private val _rate = MutableLiveData<Rate?>()
    val rate: LiveData<Rate?> get() = _rate

    init {
        updateExchangeRates()
    }

    fun updateExchangeRates() {
        getExchangeRatesUseCase().onEach {
            _state.postValue(it)
        }.launchIn(viewModelScope)
    }

    fun setRate(rate: Rate){
        _rate.value = rate
    }

    fun evaluateValue(value: String):String{
        return try {
            _rate.value?.let {
                val d = (value.toDouble() * it.nominal) / it.value
                String.format("%.4f", d)
            } ?: value
        }catch (e: NumberFormatException){
            value
        }
    }
}