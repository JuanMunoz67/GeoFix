package com.example.geofixapp

data class CrearUbicacionRequest(
    val idTicket: Int,
    val latitud: Double,
    val longitud: Double,
    val edificio: String,
    val aula: String
)