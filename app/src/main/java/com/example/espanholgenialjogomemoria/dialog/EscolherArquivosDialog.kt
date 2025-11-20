package com.example.espanholgenialjogomemoria.dialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.espanholgenialjogomemoria.R
import com.example.espanholgenialjogomemoria.adapter.EscolherArquivoAdapter
import com.example.espanholgenialjogomemoria.model.Imagem

class EscolherArquivosDialog(
    private val listaArquivos: List<Imagem>,
    private val onConfirmar: (List<Imagem>) -> Unit
): DialogFragment()
{
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder = AlertDialog.Builder(requireContext())
        val view = layoutInflater.inflate(R.layout.dialog_escolher_arquivos, null)

        val recyclerView = view.findViewById<RecyclerView>(R.id.rvArquivos)
        val btnConfirmar = view.findViewById<Button>(R.id.btnConfirmar)
        val btnCancelar = view.findViewById<Button>(R.id.btnCancelar)

        val adapter = EscolherArquivoAdapter(listaArquivos)
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        btnConfirmar.setOnClickListener {
            val escolhidos = adapter.selecionados.toList()
            onConfirmar(escolhidos)
            dismiss()
        }

        btnCancelar.setOnClickListener { dismiss() }

        builder.setView(view)
        return builder.create()
    }
}