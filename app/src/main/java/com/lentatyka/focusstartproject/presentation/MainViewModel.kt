package com.lentatyka.focusstartproject.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.lentatyka.focusstartproject.common.State
import com.lentatyka.focusstartproject.domain.GetExchangeRatesUseCase
import com.lentatyka.focusstartproject.domain.model.ExchangeRates
import com.lentatyka.focusstartproject.domain.model.Rate

class MainViewModel(
    private val getExchangeRatesUseCase: GetExchangeRatesUseCase
) : ViewModel() {
    private val _state = MutableLiveData<State<ExchangeRates>>()
    val state: LiveData<State<ExchangeRates>> get() = _state

    private val _rate = MutableLiveData<Rate>()
    val rate: LiveData<Rate> get() = _rate
}