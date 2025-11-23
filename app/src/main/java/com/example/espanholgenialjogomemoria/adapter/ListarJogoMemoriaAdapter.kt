package com.example.espanholgenialjogomemoria.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.espanholgenialjogomemoria.R

class ListarJogoMemoriaAdapter(
    private val listaJogosMemoria: MutableList<String>,
    private val onJogar: (String) -> Unit,
    private val onEditar: (String) -> Unit,
    private val onExcluir: (String) -> Unit,
): RecyclerView.Adapter<ListarJogoMemoriaAdapter.JogoMemoriaViewHolder>()
{
    class JogoMemoriaViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {
        val textViewNomeJogoMemoria: TextView = itemView.findViewById(R.id.textViewNomeJogoMemoria)
        val btnJogar: ImageButton = itemView.findViewById(R.id.btnJogar)
        val btnEditar: ImageButton = itemView.findViewById(R.id.btnEditar)
        val btnExcluir: ImageButton = itemView.findViewById(R.id.btnExcluir)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): JogoMemoriaViewHolder
    {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_jogo_memoria, parent, false)
        return JogoMemoriaViewHolder(view)
    }

    override fun onBindViewHolder(holder: JogoMemoriaViewHolder, position: Int) {
        val nome = listaJogosMemoria[position]
        holder.textViewNomeJogoMemoria.text = nome

        holder.btnJogar.setOnClickListener { onJogar(nome) }
        holder.btnEditar.setOnClickListener { onEditar(nome) }
        holder.btnExcluir.setOnClickListener { onExcluir(nome) }
    }

    override fun getItemCount(): Int = listaJogosMemoria.size
}