package com.serrato.service

import com.serrato.dto.GreenHouseCommandDto
import com.serrato.dto.GreenHouseDto
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface GreenHousesApiService {

    @GET("greenhouses")
    fun findAll(): Call<List<GreenHouseDto>>

    @GET("greenhouses/{id}")
    fun findById(@Path("id") id: Long): Call<GreenHouseDto>

    @PUT("greenhouses/{id}")
    fun updateGreenHouse(@Path("id") id: Long, @Body greenhouse: GreenHouseCommandDto): Call<GreenHouseDto>


    @POST("greenhouses")
    fun createGreenHouse(@Body greenhouse: GreenHouseDto): Call<GreenHouseDto>

    @DELETE("greenhouses/{id}")
    fun deleteGreenHouse(@Path("id") id: Long): Call<Void>
}
