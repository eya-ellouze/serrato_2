package com.serrato.dao

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.serrato.dto.HeaterStatus


@Entity(tableName = "heater")
data class HeaterEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,

    @ColumnInfo
    val name: String,

    @ColumnInfo(name = "greenhouse_name")
    val greenhouseName: String,

    @ColumnInfo(name = "greenhouse_id")
    val greenhouseId: Long,

    @ColumnInfo(name = "heater_status")
    val heaterStatus: HeaterStatus
)
