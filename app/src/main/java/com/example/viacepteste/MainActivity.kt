package com.example.viacepteste


import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val retrofit = Retrofit.Builder()
            .baseUrl("https://viacep.com.br/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val cepService = retrofit.create(CepService::class.java)

        val cepInput = findViewById<EditText>(R.id.cepInput)
        val buscarButton = findViewById<Button>(R.id.buscarButton)
        val resultadoText = findViewById<TextView>(R.id.resultadoText)

        buscarButton.setOnClickListener {
            val cep = cepInput.text.toString()
            cepService.buscarCep(cep).enqueue(object : retrofit2.Callback<Endereco> {
                override fun onResponse(call: retrofit2.Call<Endereco>, response: retrofit2.Response<Endereco>) {
                    if (response.isSuccessful) {
                        val endereco = response.body()
                        resultadoText.text = endereco?.let {
                            "${it.logradouro}, ${it.bairro}, ${it.localidade} - ${it.uf}"
                        } ?: "Endereço não encontrado."
                    } else {
                        resultadoText.text = "Erro ao buscar o CEP."
                    }
                }

                override fun onFailure(call: retrofit2.Call<Endereco>, t: Throwable) {
                    resultadoText.text = "Falha na comunicação com o serviço."
                }
            })
        }
    }
}
