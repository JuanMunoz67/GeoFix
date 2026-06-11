package com.example.geofixapp

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class DashboardActivity : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val tvBienvenida = findViewById<TextView>(R.id.tvBienvenida)
        val btnVerTickets = findViewById<Button>(R.id.btnVerTickets)
        val btnCrearTicket = findViewById<Button>(R.id.btnCrearTicket)

        val nombre = intent.getStringExtra("nombre") ?: "Usuario"
        val token = intent.getStringExtra("token")

        if (token.isNullOrEmpty()) {
            Toast.makeText(this, "Token no recibido", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        tvBienvenida.text = "Bienvenido, $nombre"

        btnVerTickets.setOnClickListener {
            val abrirTickets = Intent(this, TicketsActivity::class.java)
            abrirTickets.putExtra("token", token)
            startActivity(abrirTickets)
        }

        btnCrearTicket.setOnClickListener {
            val abrirCrearTicket = Intent(this, CrearTicketActivity::class.java)
            abrirCrearTicket.putExtra("token", token)
            startActivity(abrirCrearTicket)
        }
    }
}