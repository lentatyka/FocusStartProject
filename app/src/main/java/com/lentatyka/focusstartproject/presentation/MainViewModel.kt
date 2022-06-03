package com.lentatyka.focusstartproject.presentation

import androidx.lifecycle.*
import com.lentatyka.focusstartproject.common.State
import com.lentatyka.focusstartproject.domain.network.ExchangeRatesUseCase
import com.lentatyka.focusstartproject.domain.network.model.ExchangeRates
import com.lentatyka.focusstartproject.domain.network.model.Rate
import com.lentatyka.focusstartproject.domain.preferences.PreferencesUseCase
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

private const val PERIOD_UPDATE = 60000L

class MainViewModel @Inject constructor(
    private val exchangeRatesUseCase: ExchangeRatesUseCase,
    private val preferencesUseCase: PreferencesUseCase
) : ViewModel() {

    private val _state = MutableSharedFlow<State<ExchangeRates>>(
        replay = 1,
        onBufferOverflow = BufferOverflow.DROP_OLDEST
    )
    val state: SharedFlow<State<ExchangeRates>> = _state.asSharedFlow()

    private val _rate = MutableLiveData<Rate>()
    val rate: LiveData<Rate> get() = _rate

    private val _result = MutableLiveData<Double>()
    val result: LiveData<Double> get() = _result

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
            exchangeRatesUseCase().collect {
                _state.emit(it)
            }
        }

//        viewModelScope.launch {
//            exchangeRatesUseCase().onEach {
//                _state.postValue(it)
//            }.collect()
//        }
    }

    fun setRate(rate: Rate) {
        _rate.value = rate
    }

    /*
    Await numeric value only. EditText type...
    NumberFormatException not expected
    */
    fun convertRate(value: String) {
        val rate = if (value.isEmpty()) 0.0 else value.toDouble()
        _result.value = _rate.value?.let {
            (rate * it.nominal) / it.value
        } ?: rate
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