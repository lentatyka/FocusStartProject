package com.lentatyka.focusstartproject.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lentatyka.focusstartproject.common.State
import com.lentatyka.focusstartproject.domain.network.ExchangeRatesUseCase
import com.lentatyka.focusstartproject.domain.network.model.ExchangeRates
import com.lentatyka.focusstartproject.domain.network.model.Rate
import com.lentatyka.focusstartproject.domain.preferences.GetAutoUpdateUseCase
import com.lentatyka.focusstartproject.domain.preferences.SetAutoUpdateUseCase
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class MainViewModel @Inject constructor(
    private val getExchangeRatesUseCase: ExchangeRatesUseCase,
    private val getAutoUpdateUseCase: GetAutoUpdateUseCase,
    private val setAutoUpdateUseCase: SetAutoUpdateUseCase
) : ViewModel() {
    private val _state = MutableLiveData<State<ExchangeRates>>()
    val state: LiveData<State<ExchangeRates>> get() = _state

    private val _rate = MutableLiveData<Rate?>()
    val rate: LiveData<Rate?> get() = _rate

    private val _autoUpdate = MutableLiveData<Boolean>()
    val autoUpdate: LiveData<Boolean> get() = _autoUpdate


    init {
        updateExchangeRates()
        _autoUpdate.value = getAutoUpdateUseCase()
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
        return if (value.isEmpty())
            "0"
        else
            return try {
                val d =_rate.value?.let {
                    (value.toDouble() * it.nominal) / it.value
                } ?: value.toDouble()
                String.format("%.4f", d)
            }catch (e: NumberFormatException){
                value
            }
    }

    override fun onCleared() {
        _autoUpdate.value?.let {
            setAutoUpdateUseCase(it)
        }
        super.onCleared()
    }

    fun setAutoUpdate(checked: Boolean) {
        _autoUpdate.value = checked
    }
}