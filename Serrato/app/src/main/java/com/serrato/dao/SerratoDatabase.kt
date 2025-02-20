package com.serrato.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters


@Database(entities = [GreenHouseEntity::class, HeaterEntity::class], version = 1, exportSchema = false)
@TypeConverters(EnumConverters::class)
abstract class SerratoDatabase : RoomDatabase() {
    abstract fun greenhouseDao(): GreenHouseDao
    abstract fun heaterDao(): HeaterDao
}
