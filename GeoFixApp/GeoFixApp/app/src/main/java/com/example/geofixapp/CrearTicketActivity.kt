package com.example.geofixapp

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.*
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CrearTicketActivity : Activity() {

    private lateinit var etTitulo: EditText
    private lateinit var etDescripcion: EditText
    private lateinit var etEdificio: EditText
    private lateinit var etAula: EditText
    private lateinit var spPrioridad: Spinner
    private lateinit var btnGuardarTicket: Button

    private val permisoUbicacion = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_crear_ticket)

        etTitulo = findViewById(R.id.etTitulo)
        etDescripcion = findViewById(R.id.etDescripcion)
        etEdificio = findViewById(R.id.etEdificio)
        etAula = findViewById(R.id.etAula)
        spPrioridad = findViewById(R.id.spPrioridad)
        btnGuardarTicket = findViewById(R.id.btnGuardarTicket)

        val prioridades = listOf("Baja", "Media", "Alta", "Crítica")

        spPrioridad.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            prioridades
        )

        btnGuardarTicket.setOnClickListener {
            verificarPermisoYCrearTicket()
        }
    }

    private fun verificarPermisoYCrearTicket() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                permisoUbicacion
            )
            return
        }

        crearTicketConUbicacion()
    }

    private fun crearTicketConUbicacion() {
        val token = intent.getStringExtra("token")

        if (token == null) {
            Toast.makeText(this, "Token no recibido", Toast.LENGTH_SHORT).show()
            return
        }

        val fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        fusedLocationClient.lastLocation.addOnSuccessListener { location ->

            if (location == null) {
                Toast.makeText(this, "No se pudo obtener ubicación", Toast.LENGTH_LONG).show()
                return@addOnSuccessListener
            }

            val requestTicket = CrearTicketRequest(
                titulo = etTitulo.text.toString(),
                descripcion = etDescripcion.text.toString(),
                prioridad = spPrioridad.selectedItem.toString(),
                idUsuario = 1,
                idCategoria = 1
            )

            ApiClient.api.crearTicket("Bearer $token", requestTicket)
                .enqueue(object : Callback<Ticket> {
                    override fun onResponse(
                        call: Call<Ticket>,
                        response: Response<Ticket>
                    ) {
                        if (response.isSuccessful && response.body() != null) {
                            val ticketCreado = response.body()!!

                            guardarUbicacion(
                                token,
                                ticketCreado.idTicket,
                                location.latitude,
                                location.longitude
                            )

                        } else {
                            Toast.makeText(
                                this@CrearTicketActivity,
                                "Error al crear ticket: ${response.code()}",
                                Toast.LENGTH_LONG
                            ).show()
                        }
                    }

                    override fun onFailure(call: Call<Ticket>, t: Throwable) {
                        Toast.makeText(
                            this@CrearTicketActivity,
                            "Error: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }

    private fun guardarUbicacion(
        token: String,
        idTicket: Int,
        latitud: Double,
        longitud: Double
    ) {
        val requestUbicacion = CrearUbicacionRequest(
            idTicket = idTicket,
            latitud = latitud,
            longitud = longitud,
            edificio = etEdificio.text.toString(),
            aula = etAula.text.toString()
        )

        ApiClient.api.crearUbicacion("Bearer $token", requestUbicacion)
            .enqueue(object : Callback<Ubicacion> {
                override fun onResponse(
                    call: Call<Ubicacion>,
                    response: Response<Ubicacion>
                ) {
                    if (response.isSuccessful) {
                        Toast.makeText(
                            this@CrearTicketActivity,
                            "Ticket y ubicación guardados",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    } else {
                        Toast.makeText(
                            this@CrearTicketActivity,
                            "Ticket creado, pero error ubicación: ${response.code()}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }

                override fun onFailure(call: Call<Ubicacion>, t: Throwable) {
                    Toast.makeText(
                        this@CrearTicketActivity,
                        "Error ubicación: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}