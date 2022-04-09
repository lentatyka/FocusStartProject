package com.lentatyka.focusstartproject.domain.network.model

data class Rate(
    val id: String,
    val charCode: String,
    val nominal: Int,
    val name: String,
    val value: Double,
    val previous: Double
)