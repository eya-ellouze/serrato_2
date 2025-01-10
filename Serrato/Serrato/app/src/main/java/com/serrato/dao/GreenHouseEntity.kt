package com.serrato.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.serrato.dto.GreenHouseDto
import com.serrato.dto.HeaterDto

@Entity(tableName = "greenhouse")
data class GreenHouseEntity(
    @PrimaryKey val id: Long,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "current_temperature") val currentTemperature: Double?,
    @ColumnInfo(name = "target_temperature") val targetTemperature: Double?,
    @ColumnInfo(name = "current_humidity") val currentHumidity: Double?,
    @ColumnInfo(name = "target_humidity") val targetHumidity: Double?,
)
fun GreenHouseEntity.toDto(heaterDao: HeaterDao): GreenHouseDto {
    val heaters = heaterDao.findByGreenHouseId(this.id).map {
        HeaterDto(
            id = it.id,
            name = it.name,
            greenhouseName = it.greenhouseName,
            greenhouseId = it.greenhouseId,
            heaterStatus = it.heaterStatus
        )
    }

    return GreenHouseDto(
        id = this.id,
        name = this.name,
        currentTemperature = this.currentTemperature,
        targetTemperature = this.targetTemperature,
        currentHumidity = this.currentHumidity,
        targetHumidity = this.targetHumidity,
        heaters = heaters
    )
}
