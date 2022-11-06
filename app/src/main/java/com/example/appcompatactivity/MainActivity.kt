package com.example.appcompatactivity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.net.URL
import android.util.Log
import android.widget.Button
import android.widget.TextView
import java.lang.RuntimeException

import kotlinx.coroutines.*
import kotlinx. coroutines.Dispatchers.Main
import org.json.JSONObject
import java.text.NumberFormat
import java.util.*

class MainActivity : AppCompatActivity() {

    val API_URL = "https://www.mercadobitcoin.net/api/BTC/ticker/"

    var cotacaoBitcoin: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        buscarCotacao()
        var btnCalcular = findViewById<Button>(R.id.btnCalcular)
        btnCalcular.setOnClickListener {
            calcular()
        }
    }

    fun buscarCotacao() {
        GlobalScope.async(Dispatchers.Default) {
            val resposta = URL(API_URL).readText()
            cotacaoBitcoin = JSONObject(resposta).getJSONObject("ticker").getDouble("last")
            val f = NumberFormat.getCurrencyInstance(Locale("pt", "br"))
            val cotacaoFormatada = f.format(cotacaoBitcoin)
            var txtCotacao = findViewById<TextView>(R.id.txtCotacao)
            txtCotacao.setText("$cotacaoFormatada")

            withContext(Main){
            }
        }
    }

    fun calcular() {
        val txtValor = findViewById<TextView>(R.id.btnCalcular)
        if (txtValor.text.isEmpty()) {
            txtValor.error = "Preencha um valor"
            return
        }

        val valorDigitado = txtValor.text.toString()
            .replace(",", ".").toDouble()

        val resultado = if (cotacaoBitcoin > 0) valorDigitado / cotacaoBitcoin
        else 0.0

        val txtQtdBitcoins = findViewById<TextView>(R.id.txtQtdBitcoins)
        txtQtdBitcoins.text = "%.8f".format(resultado)
    }
}