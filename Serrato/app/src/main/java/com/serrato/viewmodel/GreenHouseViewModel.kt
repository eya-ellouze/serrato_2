package com.serrato.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.serrato.SerratoApplication
import com.serrato.dao.GreenHouseDao
import com.serrato.dao.GreenHouseEntity
import com.serrato.dao.HeaterDao
import com.serrato.dao.HeaterEntity
import com.serrato.dao.toDto
import com.serrato.dto.GreenHouseCommandDto
import com.serrato.dto.GreenHouseDto
import com.serrato.model.GreenHouseList
import com.serrato.service.ApiServices
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch


class GreenHouseViewModel(
    private val greenhouseDao: GreenHouseDao,
    private val heaterDao: HeaterDao
) : ViewModel() {

    var networkState by mutableStateOf<NetworkState>(NetworkState.ONLINE)
    var greenhousesState = mutableStateOf(GreenHouseList())

    companion object {
        val factory: ViewModelProvider.Factory =
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(
                    modelClass: Class<T>,
                    extras: CreationExtras
                ): T {
                    val application = extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as SerratoApplication
                    val greenhouseDao = application.database.greenhouseDao()
                    val heaterDao = application.database.heaterDao()
                    return GreenHouseViewModel(greenhouseDao, heaterDao) as T
                }
            }
    }

    fun findById(greenhouseId: Long): LiveData<GreenHouseDto> = liveData(Dispatchers.IO) {
        runCatching {
            ApiServices.greenhousesApiService.findById(greenhouseId).execute().body()!!
        }.onSuccess { greenhouseDto ->
            networkState = NetworkState.ONLINE
            emit(greenhouseDto)
        }.onFailure { exception ->
            networkState = NetworkState.OFFLINE
            val localGreenHouse = greenhouseDao.findById(greenhouseId).toDto(heaterDao)
            emit(localGreenHouse)
        }
    }

    fun saveInDb(greenhouseId: Long?, command: GreenHouseCommandDto): LiveData<GreenHouseDto> = liveData(Dispatchers.IO) {
        val greenhouseEntity = GreenHouseEntity(
            id = greenhouseId ?: 0,
            name = command.name,
            currentTemperature = command.currentTemperature,
            targetTemperature = command.targetTemperature,
            currentHumidity = command.currentHumidity,
            targetHumidity = command.targetHumidity
        )
        if (greenhouseId == null) {
            greenhouseDao.create(greenhouseEntity)
        } else {
            greenhouseDao.update(greenhouseEntity)
        }
        emit(greenhouseEntity.toDto(heaterDao))
    }

    fun findAll() {
        viewModelScope.launch(Dispatchers.IO) {
            runCatching {
                ApiServices.greenhousesApiService.findAll().execute()
            }.onSuccess { response ->
                val remoteGreenHouses = response.body() ?: emptyList()

                // Clear local database and save the new data
                greenhouseDao.clearAll()
                heaterDao.clearAll()

                remoteGreenHouses.forEach { greenhouse ->
                    greenhouseDao.create(
                        GreenHouseEntity(
                            id = greenhouse.id,
                            name = greenhouse.name,
                            currentTemperature = greenhouse.currentTemperature,
                            targetTemperature = greenhouse.targetTemperature,
                            currentHumidity = greenhouse.currentHumidity,
                            targetHumidity = greenhouse.targetHumidity,
                        )
                    )
                    greenhouse.heaters.forEach { heater ->
                        heaterDao.create(
                            HeaterEntity(
                                id = heater.id,
                                name = heater.name,
                                greenhouseName = greenhouse.name,
                                greenhouseId = greenhouse.id,
                                heaterStatus = heater.heaterStatus
                            )
                        )
                    }
                }
                greenhousesState.value = GreenHouseList(remoteGreenHouses)
            }.onFailure { exception ->
                exception.printStackTrace()

                val localGreenHouses = greenhouseDao.findAll().map { greenhouse ->
                    greenhouse.toDto(heaterDao)
                }
                greenhousesState.value = GreenHouseList(localGreenHouses, exception.message)
            }
        }
    }
}

