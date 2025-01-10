package com.serrato.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Update
import androidx.room.Query

@Dao
interface GreenHouseDao {
    @Query("select * from greenhouse order by name")
    fun findAll(): List<GreenHouseEntity>

    @Query("select * from greenhouse where id = :greenhouseId")
    fun findById(greenhouseId: Long): GreenHouseEntity

    @Insert
    suspend fun create(greenhouse: GreenHouseEntity)

    @Update
    suspend fun update(greenhouse: GreenHouseEntity): Int

    @Delete
    suspend fun delete(greenhouse: GreenHouseEntity)

    @Query("delete from greenhouse")
    suspend fun clearAll()
}
