package com.example.espanholgenialjogomemoria.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.espanholgenialjogomemoria.R
import com.example.espanholgenialjogomemoria.model.Imagem

class EscolherArquivoAdapter(
    private val lista: List<Imagem>,
): RecyclerView.Adapter<EscolherArquivoAdapter.ImagemViewHolder>()
{
    val selecionados = mutableSetOf<Imagem>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_arquivo_checkbox, parent, false)
        return ImagemViewHolder(view)
    }

    override fun onBindViewHolder(holder: ImagemViewHolder, position: Int) {
        val imagem = lista[position]

        holder.tvNome.text = imagem.nome

        holder.checkBox.setOnCheckedChangeListener(null)
        holder.checkBox.isChecked = selecionados.contains(imagem)

        holder.checkBox.setOnCheckedChangeListener { button, isChecked ->

            if (isChecked) {
                // --- LIMITE MÁXIMO ---
                if (selecionados.size >= 7) {
                    button.isChecked = false
                    Toast.makeText(
                        holder.itemView.context,
                        "Máximo de 7 itens!", Toast.LENGTH_SHORT
                    ).show()
                    return@setOnCheckedChangeListener
                }

                selecionados.add(imagem)
            } else {
                // --- LIMITE MÍNIMO ---
                if (selecionados.size <= 5) {
                    button.isChecked = true
                    Toast.makeText(
                        holder.itemView.context,
                        "Selecione pelo menos 5 itens!", Toast.LENGTH_SHORT
                    ).show()
                    return@setOnCheckedChangeListener
                }

                selecionados.remove(imagem)
            }
        }
    }

    override fun getItemCount() = lista.size

    class ImagemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvNome: TextView = itemView.findViewById(R.id.tvNomeArquivo)
        val checkBox: CheckBox = itemView.findViewById(R.id.checkboxArquivo)
    }
}