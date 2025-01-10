package com.serrato.dto

data class GreenHouseDto(
    val id: Long,
    val name: String,
    val currentTemperature: Double?,
    val targetTemperature: Double?,
    val currentHumidity: Double?,
    val targetHumidity: Double?,
    val heaters: List<HeaterDto>,
)