package com.example.geofixapp

data class CrearTicketRequest(
    val titulo: String,
    val descripcion: String,
    val prioridad: String,
    val idUsuario: Int,
    val idCategoria: Int
)