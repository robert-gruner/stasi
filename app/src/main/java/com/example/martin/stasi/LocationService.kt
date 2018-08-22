package com.example.martin.stasi


import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LocationService {
    @POST("/location")
    fun sendLocation(@Body coordinates: RequestBody): Call<String>
}