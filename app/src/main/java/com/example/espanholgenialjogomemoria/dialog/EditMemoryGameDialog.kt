package com.example.espanholgenialjogomemoria.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.example.espanholgenialjogomemoria.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class EditMemoryGameDialog : DialogFragment() {

    private lateinit var nomeJogo: String
    private lateinit var etNome: EditText
    private lateinit var spinnerTipo: Spinner
    private lateinit var spinnerCategoria: Spinner
    private lateinit var layoutArquivos: LinearLayout
    private lateinit var btnSalvar: Button
    private lateinit var btnCancelar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 Agora a variável é inicializada no momento correto
        nomeJogo = arguments?.getString("nomeJogo") ?: ""
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())

        val view = layoutInflater.inflate(R.layout.dialog_editar_dados_jogo_memoria, null)
        dialog.setContentView(view)

        dialog.window?.setBackgroundDrawableResource(android.R.color.white)

        iniciarViews(view)

        // Agora nomeJogo JÁ está inicializado
        carregarDadosFirestore()

        btnCancelar.setOnClickListener { dismiss() }

        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    private fun iniciarViews(view: View) {
        etNome = view.findViewById(R.id.etNomeJogoMemoria)
        spinnerTipo = view.findViewById(R.id.spinnerJogoMemoriaOpcoes)
        spinnerCategoria = view.findViewById(R.id.spinnerCategoriaOpcoes)
        layoutArquivos = view.findViewById(R.id.layoutArquivosSelecionados)
        btnSalvar = view.findViewById(R.id.btnSalvar)
        btnCancelar = view.findViewById(R.id.btnCancelar)
    }

    private fun carregarDadosFirestore()
    {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("users")
            .document(userId)
            .collection("jogoMemoria")
            .document(nomeJogo)
            .get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) return@addOnSuccessListener

                val nome = doc.getString("nome") ?: ""
                val tipo = doc.getString("tipo") ?: ""
                val categoria = doc.getString("categoria") ?: ""
                val arquivos = doc.get("arquivos") as? List<String> ?: emptyList()

                etNome.setText(nome)

                selecionarItemSpinner(spinnerTipo, tipo)
                selecionarItemSpinner(spinnerCategoria, categoria)

                mostrarArquivosSelecionados(arquivos)
            }
    }

    private fun selecionarItemSpinner(spinner: Spinner, valor: String) {
        val adapter = spinner.adapter ?: return
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i).toString() == valor) {
                spinner.setSelection(i)
                break
            }
        }
    }

    private fun mostrarArquivosSelecionados(arquivos: List<String>) {
        layoutArquivos.removeAllViews()

        for (nomeArquivo in arquivos) {
            val tv = TextView(requireContext())
            tv.text = "• $nomeArquivo"
            tv.textSize = 16f
            tv.setPadding(8, 8, 8, 8)
            layoutArquivos.addView(tv)
        }
    }
}