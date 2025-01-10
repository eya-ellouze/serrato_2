package com.serrato.model

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.serrato.dto.GreenHouseCommandDto
import com.serrato.dto.GreenHouseDto
import com.serrato.service.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class GreenHouseViewModel : ViewModel() {
    val greenhousesState = MutableStateFlow<GreenHouseList>(GreenHouseList(emptyList()))
    var greenhouse by mutableStateOf<GreenHouseDto?>(null)
    fun findAll() {
        viewModelScope.launch(context = Dispatchers.IO) {
            runCatching { ApiServices.greenhousesApiService.findAll().execute() }
                .onSuccess {
                    val greenhouses = it.body() ?: emptyList()
                    greenhousesState.value = GreenHouseList(greenhouses)
                }
                .onFailure {
                    it.printStackTrace()
                    greenhousesState.value = GreenHouseList(emptyList(), it.stackTraceToString() )
                }
        }
    }
    fun findGreenHouse(id: Long) {
        viewModelScope.launch(context = Dispatchers.IO) {
            runCatching { ApiServices.greenhousesApiService.findById(id).execute() }
                .onSuccess {
                    greenhouse = it.body()
                }
                .onFailure {
                    it.printStackTrace()
                    greenhouse = null
                }
        }
    }
    fun updateGreenhouse(id: Long, greenhouseDto: GreenHouseDto) {
        val command = GreenHouseCommandDto(
            name = greenhouseDto.name,
            targetTemperature = greenhouseDto.targetTemperature ?.let { Math.round(it * 10) /10.0 },
            currentTemperature = greenhouseDto.currentTemperature,
            targetHumidity = greenhouseDto.targetHumidity ?.let { Math.round(it * 10) /10.0 },
            currentHumidity = greenhouseDto.currentHumidity,
        )
        viewModelScope.launch(context = Dispatchers.IO) {
            runCatching { ApiServices.greenhousesApiService.updateGreenHouse(id, command).execute() }
                .onSuccess {
                    greenhouse = it.body()
                }
                .onFailure {
                    it.printStackTrace()
                    greenhouse = null
                }
        }
    }
}