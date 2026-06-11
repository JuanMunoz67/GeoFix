package com.example.geofixapp

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST

interface ApiService {

    @POST("api/auth/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

    @GET("api/tickets")
    fun obtenerTickets(
        @Header("Authorization") token: String
    ): Call<List<Ticket>>

    @POST("api/tickets")
    fun crearTicket(
        @Header("Authorization") token: String,
        @Body request: CrearTicketRequest
    ): Call<Ticket>

    @POST("api/ubicaciones")
    fun crearUbicacion(
        @Header("Authorization") token: String,
        @Body request: CrearUbicacionRequest
    ): Call<Ubicacion>
}