package com.lentatyka.focusstartproject.common

sealed class State<out R> {
    class Success<out T>(val data: T) : State<T>()
    class Error(val message: String) : State<Nothing>()
    object Loading : State<Nothing>()
}
