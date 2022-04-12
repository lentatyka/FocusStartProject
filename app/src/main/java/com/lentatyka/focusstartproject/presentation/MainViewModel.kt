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
import java.util.*
import javax.inject.Inject
import javax.inject.Singleton
private const val PERIOD_UPDATE = 6000L
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

    private val _result = MutableLiveData<Double?>()
    val result: LiveData<Double?> get() = _result

    private  var timer: Timer? = null


    init {
        updateExchangeRates()
    }

    fun updateExchangeRates() {
        getExchangeRatesUseCase().onEach {
            _state.postValue(it)
        }.launchIn(viewModelScope)
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

    }

    fun getChekedState() = getAutoUpdateUseCase()

    fun saveCheckedState(checked: Boolean) = setAutoUpdateUseCase(checked)

    fun startAutoUpdate(){
        stopAutoUpdate()
        timer = Timer()
        timer?.schedule(
            object : TimerTask() {
                override fun run() {
                    getExchangeRatesUseCase()
                }
            },
            1000,
            PERIOD_UPDATE
        )
    }

    fun stopAutoUpdate(){
        timer?.let {
            it.cancel()
            timer = null
        }
    }
}