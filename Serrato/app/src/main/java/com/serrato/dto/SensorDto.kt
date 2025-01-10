package com.serrato.dto


data class SensorDto(
    val id: Long,
    val name: String,
    val value: Double?, // Current sensor value (e.g., temperature or humidity)
    val sensorType: SensorType // Enum for the type of sensor
)
