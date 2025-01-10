package com.serrato.dto

data class GreenHouseCommandDto(
    val name: String,
    val currentTemperature: Double?,
    val targetTemperature: Double?,
    val currentHumidity: Double?,
    val targetHumidity: Double?,
    val fieldId: Long = -10
)