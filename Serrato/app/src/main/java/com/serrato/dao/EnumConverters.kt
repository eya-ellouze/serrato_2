package com.serrato.dao

import androidx.room.TypeConverter
import com.serrato.dto.HeaterStatus

class EnumConverters {

    // A first method to convert enum in string when the data will be stored in the database
    @TypeConverter
    fun fromHeaterStatus(value: HeaterStatus?): String? {
        return value?.toString()
    }

    // A second one to do the inverse operation
    @TypeConverter
    fun toHeaterStatus(value: String?):HeaterStatus? {
        return value?.let { HeaterStatus.valueOf(it) }
    }

}
