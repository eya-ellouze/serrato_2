package com.serrato.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Update
import androidx.room.Query

@Dao
interface HeaterDao {
    @Query("SELECT * FROM heater ORDER BY name")
    fun findAll(): List<HeaterEntity>

    @Query("SELECT * FROM heater WHERE id = :heaterId")
    fun findById(heaterId: Long): HeaterEntity

    @Query("SELECT * FROM heater WHERE greenhouse_id = :greenhouseId")
    fun findByGreenHouseId(greenhouseId: Long): List<HeaterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun create(heater: HeaterEntity)

    @Update
    suspend fun update(heater: HeaterEntity): Int

    @Delete
    suspend fun delete(heater: HeaterEntity)

    @Query("delete from heater")
    suspend fun clearAll()
}
