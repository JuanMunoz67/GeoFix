package com.example.geofixapp

data class Ticket(
    val idTicket: Int,
    val titulo: String,
    val descripcion: String,
    val prioridad: String,
    val estado: String,
    val fechaCreacion: String,
    val idUsuario: Int,
    val idCategoria: Int
)