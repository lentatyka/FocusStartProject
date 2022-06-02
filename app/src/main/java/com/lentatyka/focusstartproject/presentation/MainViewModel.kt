package com.lentatyka.focusstartproject.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lentatyka.focusstartproject.common.State
import com.lentatyka.focusstartproject.domain.network.ExchangeRatesUseCase
import com.lentatyka.focusstartproject.domain.network.model.ExchangeRates
import com.lentatyka.focusstartproject.domain.network.model.Rate
import com.lentatyka.focusstartproject.domain.preferences.PreferencesUseCase
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

private const val PERIOD_UPDATE = 60000L

class MainViewModel @Inject constructor(
    private val exchangeRatesUseCase: ExchangeRatesUseCase,
    private val preferencesUseCase: PreferencesUseCase
) : ViewModel() {
    private val _state = MutableLiveData<State<ExchangeRates>>()
    val state: LiveData<State<ExchangeRates>> get() = _state

    private val _rate = MutableLiveData<Rate?>()
    val rate: LiveData<Rate?> get() = _rate

    private val _result = MutableLiveData<Double?>()
    val result: LiveData<Double?> get() = _result

    private var _isChecked = preferencesUseCase.getAutoUpdateAccess()
    val isChecked: Boolean get() = _isChecked

    private var timer: Timer? = null


    init {
        if (_isChecked)
            startAutoUpdate()
        else
            updateExchangeRates()

    }

    fun updateExchangeRates() {
        viewModelScope.launch {
            exchangeRatesUseCase().onEach {
                _state.postValue(it)
            }.collect()
        }
    }

    fun setRate(rate: Rate) {
        _rate.value = rate
    }

    fun evaluateValue(value: String) {
        _result.value = try {
            _rate.value?.let {
                (value.toDouble() * it.nominal) / it.value
            } ?: value.toDouble()
        } catch (e: NumberFormatException) {
            null
        }
    }

    fun setAutoUpdate(checked: Boolean) {
        _isChecked = checked
        if (checked)
            startAutoUpdate()
        else
            stopAutoUpdate()
    }


    private fun startAutoUpdate() {
        timer = Timer()
        timer?.schedule(
            object : TimerTask() {
                override fun run() {
                    updateExchangeRates()
                }
            },
            1000,
            PERIOD_UPDATE
        )
    }

    private fun stopAutoUpdate() {
        timer?.let {
            it.cancel()
            timer = null
        }
    }

    override fun onCleared() {
        stopAutoUpdate()
        preferencesUseCase.saveAutoUpdateAccess(_isChecked)
        super.onCleared()
    }
}