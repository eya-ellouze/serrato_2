package com.serrato.model

import com.serrato.dto.GreenHouseDto

class GreenHouseList(
    val greenhouses: List<GreenHouseDto> = emptyList(),
    val error: String? = null
)