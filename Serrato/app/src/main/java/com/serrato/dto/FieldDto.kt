package com.serrato.dto

data class FieldDto(
    val id: Long,
    val name: String,
    val greenhouses: List<GreenHouseDto> // A list of greenhouses in the field
)
