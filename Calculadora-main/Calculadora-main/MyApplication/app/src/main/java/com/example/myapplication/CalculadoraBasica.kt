package com.example.myapplication

import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import kotlin.math.roundToInt
import android.widget.Toast
import android.content.Intent
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class CalculadoraBasica : AppCompatActivity() {

    //iniciando as variaveis
    val db = Firebase.firestore
    private val historyList = mutableListOf<String>()
    private lateinit var editTextNumber: EditText
    private var firstNumber: Double = 0.0
    private var secondNumber: Double = 0.0
    private var currentOperator: String? = null
    private var valornaMemoria: Double = 0.0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        editTextNumber = findViewById(R.id.editTextNumber)

        // Carregar o histórico do SharedPreferences ao iniciar
        carregaHistorico()

        // Atualizar a TextView com o histórico carregado
        atualizahistoricoView()
    }

    fun onNumberClick(view: View) {
        if (view is Button) {
            editTextNumber.append(view.text)
        }
    }

    fun onOperatorClick(view: View) {
        if (view is Button) {
            firstNumber = editTextNumber.text.toString().toDouble()
            currentOperator = view.text.toString()
            editTextNumber.text.clear()
        }
    }

    fun onEqualsClick(view: View) {
        try {
            secondNumber = editTextNumber.text.toString().toDoubleOrNull() ?: 0.0

            val result = when (currentOperator) {
                "+" -> {
                    showToastMessage(getString(R.string.adicao_mensagem))
                    firstNumber + secondNumber
                }
                "-" -> {
                    showToastMessage(getString(R.string.subtracao_mensagem))
                    firstNumber - secondNumber
                }
                "*" -> {
                    showToastMessage(getString(R.string.multiplicacao_mensagem))
                    firstNumber * secondNumber
                }
                "/" -> {
                    if (secondNumber == 0.0) {
                        showToastMessage(getString(R.string.divisao_por_zero_mensagem))
                        return
                    } else {
                        showToastMessage(getString(R.string.divisao_mensagem))
                        firstNumber / secondNumber
                    }
                }
                else -> null
            }

            if(result != null) {
                // Verificar e remover o cálculo mais antigo se o histórico já tiver 10 entradas
                if (historyList.size == 10) {
                    historyList.removeAt(0)
                }

                // Adicionar a operação à lista de histórico
                historyList.add("$firstNumber $currentOperator $secondNumber = ${result.roundToInt()}")

                // Atualizar a TextView com o histórico
                atualizahistoricoView()

                editTextNumber.setText(result.toString())

                // Salvar o histórico no SharedPreferences após cada operação
                salvaHistorico()
            } else {
                // Pode adicionar algum feedback ao usuário sobre a operação inválida
            }
        } catch (e: NumberFormatException) {
            showToastMessage(getString(R.string.erro_numero_invalido))
        }
    }

    //atualiza historico
    private fun atualizahistoricoView() {
        val historyView: TextView = findViewById(R.id.historyView)
        historyView.text = historyList.joinToString("\n")
        historyView.gravity = Gravity.BOTTOM or Gravity.END
    }

    //limpa os numeros da calculadora
    fun onLimpaClick(view: View) {
        editTextNumber.setText("")
        firstNumber = 0.0
        secondNumber = 0.0
        currentOperator = null
        limpaHistoricoFirestore()
        limpaHistoricoSharedPreferences()
        limpaHistoricoVisualizacao()



    }

    private fun salvaHistorico() {
        // Salvando no SharedPreferences
        val sharedPreferences = getSharedPreferences("calculator_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        val historyString = historyList.joinToString(";")
        editor.putString("history_key", historyString)
        editor.apply()

        // Salvando no Firestore
        val docRef = db.collection("global_histories").document("calculator_history")

        val data = hashMapOf(
            "history" to historyList
        )
        docRef.set(data)
    }

    private fun carregaHistorico() {
        val sharedPreferences = getSharedPreferences("calculator_prefs", MODE_PRIVATE)
        val historyString = sharedPreferences.getString("history_key", null)

        if (historyString != null) {
            historyList.clear()
            val loadedList = historyString.split(";")
            val startIndex = if (loadedList.size > 10) loadedList.size - 10 else 0
            for (i in startIndex until loadedList.size) {
                historyList.add(loadedList[i])
            }
        }
    }

    // função para pop up de mensagem na tela
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

    //chama a outra activity
    fun openCalculadoraCientifica(view: View) {
        val intent = Intent(this, CalculadoraCientificaActivity::class.java)
        startActivity(intent)
    }

    fun onAddMemoriaClick(view: View) {
        val currentValue = editTextNumber.text.toString().toDoubleOrNull() ?: 0.0
        valornaMemoria += currentValue
        editTextNumber.setText("")
    }

    fun onRemoveMemoriaClick(view: View) {
        valornaMemoria = 0.0
        showToastMessage(getString(R.string.memoria_limpada_mensagem))  // Você precisará adicionar essa string no seu arquivo de recursos
    }

    fun onPegaNumeroMemoriaClick(view: View) {
        editTextNumber.setText(valornaMemoria.toString())
    }
    private fun limpaHistoricoFirestore() {
        val docRef = db.collection("global_histories").document("calculator_history")

        // Define um hashMap vazio para limpar os dados do documento
        val emptyData = hashMapOf<String, List<String>>()
        docRef.set(emptyData)
    }
    private fun limpaHistoricoSharedPreferences() {
        val sharedPreferences = getSharedPreferences("calculator_prefs", MODE_PRIVATE)
        val editor = sharedPreferences.edit()
        editor.clear()
        editor.apply()
    }
    private fun limpaHistoricoVisualizacao() {
        historyList.clear()  // Limpa a lista de histórico
        atualizahistoricoView()  // Atualiza a visualização
    }


}
