package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.sqrt
import kotlin.math.log10
import android.content.Intent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

class CalculadoraCientificaActivity : AppCompatActivity() {

    //iniciando as variaveis
    private lateinit var input: EditText
    private var percentageValue: Double? = null
    private var valornaMemoria: Double = 0.0
    private lateinit var editTextNumber: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_calculadora_cientifica)

        input = findViewById(R.id.editTextNumber)
        editTextNumber = findViewById(R.id.editTextNumber)
    }

    //chama a outra activity
    fun openCalculadoraBasica(view: View) {
        val intent = Intent(this, CalculadoraBasica::class.java)
        startActivity(intent)
    }

    fun calcularFatorial(view: View) {
        val number = input.text.toString().toIntOrNull()
        if (number != null && number < 66) {
            val result = factorial(number)
            input.setText(result.toString())
        }else {
            Toast.makeText(this, getString(R.string.fatorial_muito_alto), Toast.LENGTH_SHORT).show()
        }
    }

    private fun factorial(n: Int): Long {
        if (n <= 1) return 1
        return n * factorial(n - 1)
    }

    fun calcularRaizQuadrada(view: View) {
        val number = input.text.toString().toDoubleOrNull()
        if (number != null) {
            val result = sqrt(number)
            input.setText(result.toString())
        }
    }

    fun calcularLogaritimo(view: View) {
        val number = input.text.toString().toDoubleOrNull()
        if (number != null) {
            if (number.compareTo(0) <= 0) {
                Toast.makeText(this, getString(R.string.invalido_logaritimo), Toast.LENGTH_SHORT).show()
                return
            }
            val result = log10(number)
            input.setText(result.toString())
        }
    }
    fun onNumberClick(view: View) {
        if (view is Button) {
            if (input.text.toString() == "0" && view.text != ".") {
                input.setText("")
            }
            val numberClicked = view.text.toString()
            input.append(numberClicked)
            percentageValue = null // Limpa a porcentagem quando um novo número é inserido
        }
    }
    fun onLimpaClick(view: View) {
        input.setText("")          // Limpa o input
        percentageValue = null    // Limpa o valor da porcentagem
    }

    fun calcularPercentual(view: View) {
        val percentage = input.text.toString().toDoubleOrNull()
        if (percentage != null) {
            Toast.makeText(this, "Porcentagem definida: $percentage", Toast.LENGTH_SHORT).show()
            input.tag = percentage  // Salva o valor no Tag do EditText
            input.setText("")  // Limpa o EditText para a próxima entrada
        } else {
            Toast.makeText(this, getString(R.string.erro_definir_porcentagem), Toast.LENGTH_SHORT).show()
        }
    }

    fun onIgualClick(view: View) {
        try {
            val percentage = input.tag as? Double
            if (percentage != null) {
                calcularResultado(percentage)
            } else {
                Toast.makeText(this, getString(R.string.sem_valor_porcentagem), Toast.LENGTH_SHORT).show()
            }
        } catch (e: Exception) {
            // tratando a exceção, exibindo uma mensagem.
            Toast.makeText(this, "Ocorreu um erro ao processar a entrada.", Toast.LENGTH_SHORT).show()
        }
    }

    fun calcularResultado(percentage: Double) {
        val number = input.text.toString().toDoubleOrNull()
        if (number != null) {
            val result = (percentage / 100) * number
            input.setText(result.toString())
            input.tag = null  // Limpa o Tag após o cálculo
        } else {
            Toast.makeText(this, getString(R.string.erro_valores_inseridos), Toast.LENGTH_SHORT).show()
        }
    }

    private fun showToastMessage(message: String) {
        val inflater = layoutInflater
        val layout: View = inflater.inflate(R.layout.custom_toast_layout, null)

        val text: TextView = layout.findViewById(R.id.text)
        text.text = message

        val toast = Toast(applicationContext)
        toast.duration = Toast.LENGTH_SHORT
        toast.view = layout
        toast.show()
    }

    fun onAddMemoriaClick(view: View) {
        val currentValue = editTextNumber.text.toString().toDoubleOrNull() ?: 0.0
        valornaMemoria += currentValue
        editTextNumber.setText("")
    }

    fun onRemoveMemoriaClick(view: View) {
        valornaMemoria = 0.0
        showToastMessage(getString(R.string.memoria_limpada_mensagem))
    }

    fun onPegaNumeroMemoriaClick(view: View) {
        editTextNumber.setText(valornaMemoria.toString())
    }
}
