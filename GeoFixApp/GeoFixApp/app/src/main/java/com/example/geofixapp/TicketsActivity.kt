package com.example.geofixapp

import android.app.Activity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TicketsActivity : Activity() {

    private lateinit var listViewTickets: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tickets)

        listViewTickets = findViewById(R.id.listViewTickets)

        val token = intent.getStringExtra("token")

        if (token != null) {
            cargarTickets(token)
        } else {
            Toast.makeText(this, "Token no recibido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun cargarTickets(token: String) {
        ApiClient.api.obtenerTickets("Bearer $token")
            .enqueue(object : Callback<List<Ticket>> {

                override fun onResponse(
                    call: Call<List<Ticket>>,
                    response: Response<List<Ticket>>
                ) {
                    if (response.isSuccessful) {
                        val tickets = response.body() ?: emptyList()

                        val lista = tickets.map {
                            "ID: ${it.idTicket}\n" +
                                    "Título: ${it.titulo}\n" +
                                    "Prioridad: ${it.prioridad}\n" +
                                    "Estado: ${it.estado}"
                        }

                        listViewTickets.adapter = ArrayAdapter(
                            this@TicketsActivity,
                            android.R.layout.simple_list_item_1,
                            lista
                        )
                    } else {
                        Toast.makeText(
                            this@TicketsActivity,
                            "Error HTTP: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<List<Ticket>>, t: Throwable) {
                    Toast.makeText(
                        this@TicketsActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}