package com.serrato.dto

enum class HeaterStatus { OPENED, CLOSED}

data class HeaterDto(
    val id: Long,
    val name: String,
    val greenhouseName: String,
    val greenhouseId: Long,
    val heaterStatus: HeaterStatus
)