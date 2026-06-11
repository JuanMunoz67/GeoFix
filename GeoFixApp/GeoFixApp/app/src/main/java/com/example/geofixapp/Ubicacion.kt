package com.example.geofixapp

data class Ubicacion(
    val idUbicacion: Int,
    val idTicket: Int,
    val latitud: Double,
    val longitud: Double,
    val edificio: String,
    val aula: String
)