package com.example.geofixapp

import android.app.Activity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : Activity() {

    private lateinit var etCorreo: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnLogin: Button
    private lateinit var tvResultado: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        etCorreo = findViewById(R.id.etCorreo)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        tvResultado = findViewById(R.id.tvResultado)

        btnLogin.setOnClickListener {

            val request = LoginRequest(
                correo = etCorreo.text.toString(),
                password = etPassword.text.toString()
            )

            ApiClient.api.login(request)
                .enqueue(object : Callback<LoginResponse> {

                    override fun onResponse(
                        call: Call<LoginResponse>,
                        response: Response<LoginResponse>
                    ) {

                        if (response.isSuccessful && response.body() != null) {

                            val token = response.body()!!.token

                            Toast.makeText(
                                this@MainActivity,
                                "Login correcto",
                                Toast.LENGTH_SHORT
                            ).show()

                            val intent = android.content.Intent(
                                this@MainActivity,
                                DashboardActivity::class.java
                            )

                            intent.putExtra("token", token)
                            intent.putExtra("nombre", response.body()!!.usuario.nombre)

                            startActivity(intent)
                            finish()

                        } else {

                            Toast.makeText(
                                this@MainActivity,
                                "Login incorrecto",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }

                    override fun onFailure(
                        call: Call<LoginResponse>,
                        t: Throwable
                    ) {

                        Toast.makeText(
                            this@MainActivity,
                            "Error: ${t.message}",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                })
        }
    }

    private fun obtenerTickets(token: String) {

        ApiClient.api.obtenerTickets("Bearer $token")
            .enqueue(object : Callback<List<Ticket>> {

                override fun onResponse(
                    call: Call<List<Ticket>>,
                    response: Response<List<Ticket>>
                ) {

                    if (response.isSuccessful) {

                        val tickets = response.body()

                        val texto = StringBuilder()

                        tickets?.forEach {

                            texto.append("ID: ${it.idTicket}\n")
                            texto.append("Título: ${it.titulo}\n")
                            texto.append("Estado: ${it.estado}\n\n")
                        }

                        tvResultado.text = texto.toString()

                    } else {

                        tvResultado.text =
                            "Error HTTP: ${response.code()}"
                    }
                }

                override fun onFailure(
                    call: Call<List<Ticket>>,
                    t: Throwable
                ) {

                    Toast.makeText(
                        this@MainActivity,
                        "Error: ${t.message}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
    }
}