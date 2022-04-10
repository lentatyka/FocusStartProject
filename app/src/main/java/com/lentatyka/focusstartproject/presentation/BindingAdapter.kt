package com.lentatyka.focusstartproject.presentation

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("mainValue", "defaultValue")
fun setText(view: TextView, mainText: String?, defaultText: String) {
    view.text = mainText ?: defaultText
}

@BindingAdapter("resultValue")
fun setResult(view: TextView, result: Double?) {
    view.text = String.format("%.4f", result ?: 0.0)
}