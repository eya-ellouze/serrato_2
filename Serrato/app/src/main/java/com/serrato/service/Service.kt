package com.serrato.service


import com.serrato.dto.GreenHouseDto
import com.serrato.dto.HeaterDto
import com.serrato.dto.HeaterStatus
import androidx.core.text.isDigitsOnly

object GreenHouseService {
    val GREENHOUSE_NUMBER: List<Char> = ('A'..'Z').toList()

    fun generateHeater(id: Long, greenhouseId: Long, greenhouseName: String): HeaterDto {
        val name = "Heater-$id"
        return HeaterDto(
            id = id,
            name = name,
            greenhouseName = greenhouseName,
            greenhouseId = greenhouseId,
            heaterStatus = HeaterStatus.values().random()
        )
    }

    fun generateGreenHouse(id: Long): GreenHouseDto {
        val greenhouseName = "${GREENHOUSE_NUMBER.random()}"
        val heaters = (1..(1..6).random()).map { generateHeater(it.toLong(), id, greenhouseName) }
        return GreenHouseDto(
            id = id,
            name = greenhouseName,
            currentTemperature = (15..30).random().toDouble(),
            targetTemperature = (15..22).random().toDouble(),
            currentHumidity = (40..80).random().toDouble(),
            targetHumidity = (40..80).random().toDouble(),
            heaters = heaters
        )
    }

    val GREENHOUSES = (1..50).map { generateGreenHouse(it.toLong()) }.toMutableList()

    fun findAll(): List<GreenHouseDto> {
        return GREENHOUSES.sortedBy { it.name }
    }

    fun findById(id: Long): GreenHouseDto? {
        return GREENHOUSES.find { it.id == id }
    }

    fun findByName(name: String): GreenHouseDto? {
        return GREENHOUSES.find { it.name == name }
    }

    fun updateGreenHouse(id: Long, greenhouse: GreenHouseDto): GreenHouseDto? {
        val index = GREENHOUSES.indexOfFirst { it.id == id }
        val updatedGreenHouse = findById(id)?.copy(
            name = greenhouse.name,
            targetTemperature = greenhouse.targetTemperature,
            currentTemperature = greenhouse.currentTemperature,
            targetHumidity = greenhouse.targetHumidity,
            currentHumidity = greenhouse.currentHumidity
        ) ?: throw IllegalArgumentException()
        return GREENHOUSES.set(index, updatedGreenHouse)
    }

    fun findByNameOrId(nameOrId: String?): GreenHouseDto? {
        if (!nameOrId.isNullOrEmpty()) {
            return if (nameOrId.isDigitsOnly()) {
                findById(nameOrId.toLong())
            } else {
                findByName(nameOrId)
            }
        }
        return null
    }
}