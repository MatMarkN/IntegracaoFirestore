package com.example.myapplication

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.view.View
import android.widget.Button

class SelecaoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_selecao)

        findViewById<Button>(R.id.btnCalculadoraBasica).setOnClickListener { abrirCalculadoraBasica(it) }
        findViewById<Button>(R.id.btnCalculadoraAvancada).setOnClickListener { abrirCalculadoraAvancada(it) }
    }


    fun abrirCalculadoraBasica(view: View) {
        val intent = Intent(this, CalculadoraBasica::class.java)  // Supondo que MainActivity é sua calculadora básica
        startActivity(intent)
    }

    fun abrirCalculadoraAvancada(view: View) {
        val intent = Intent(this, CalculadoraCientificaActivity::class.java)
        startActivity(intent)
    }
}
