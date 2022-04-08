package com.lentatyka.focusstartproject.domain.model

data class Rate(
    val id: String,
    val charCode: String,
    val nominal: Int,
    val name: String,
    val value: Double,
    val previous: Double
)