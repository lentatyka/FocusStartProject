package com.lentatyka.focusstartproject.presentation

import android.content.res.Resources
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("mainValue", "defaultValue")
fun setDate(view: TextView, mainText: String?, defaultText: String) {
    view.text = mainText ?: defaultText
}