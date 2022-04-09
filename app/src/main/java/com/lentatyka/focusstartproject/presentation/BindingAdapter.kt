package com.lentatyka.focusstartproject.presentation

import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("mainValue", "defaultValue")
fun setText(view: TextView, mainText: String?, defaultText: String) {
    view.text = mainText ?: defaultText
}