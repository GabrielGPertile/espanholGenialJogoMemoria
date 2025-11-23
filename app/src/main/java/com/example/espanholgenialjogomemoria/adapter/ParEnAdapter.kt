package com.example.seuprojeto.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.espanholgenialjogomemoria.R
import com.example.espanholgenialjogomemoria.model.Carta
import com.example.espanholgenialjogomemoria.model.TipoCarta

data class ParEnItem(
    val imagemUrl: String,
    val texto: String
)

class ParEnAdapter(
    private val lista: List<Carta>,
    private val onClick: (Carta, Int) -> Unit
) : RecyclerView.Adapter<ParEnAdapter.ParEnViewHolder>() {

    private val cartasViradas = mutableMapOf<Int, Boolean>() // posição -> virada?
    private val cartasAcertadas = mutableSetOf<Int>()        // cartas que já foram acertadas

    inner class ParEnViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageViewVerso: ImageView = view.findViewById(R.id.imageViewVerso)
        val layoutFrente: View = view.findViewById(R.id.layoutFrente)
        val imageViewFrente: ImageView = view.findViewById(R.id.imageViewFrente)
        val textViewFrente: TextView = view.findViewById(R.id.textViewFrente)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParEnViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_carta_verso_par_es, parent, false)
        return ParEnViewHolder(view)
    }

    override fun onBindViewHolder(holder: ParEnViewHolder, position: Int) {
        val carta = lista[position]

        // se a carta já foi acertada, fica invisível e não clicável
        if (cartasAcertadas.contains(position)) {
            holder.itemView.visibility = View.INVISIBLE
            holder.itemView.isClickable = false
            return
        } else {
            holder.itemView.visibility = View.VISIBLE
            holder.itemView.isClickable = true
        }

        val virada = cartasViradas[position] ?: false
        holder.layoutFrente.visibility = if (virada) View.VISIBLE else View.GONE
        holder.imageViewVerso.visibility = if (virada) View.GONE else View.VISIBLE

        if (carta.tipo == TipoCarta.IMAGEM) {
            holder.imageViewFrente.visibility = View.VISIBLE
            holder.textViewFrente.visibility = View.GONE
            Glide.with(holder.itemView.context).load(carta.imagemUrl).into(holder.imageViewFrente)
        } else {
            holder.imageViewFrente.visibility = View.GONE
            holder.textViewFrente.visibility = View.VISIBLE
            holder.textViewFrente.text = carta.conteudo
        }

        holder.itemView.setOnClickListener {
            cartasViradas[position] = true
            notifyItemChanged(position)
            onClick(carta, position)
        }
    }

    override fun getItemCount() = lista.size

    // marca par correto como invisível
    fun marcarParCorreto(pos1: Int, pos2: Int) {
        cartasAcertadas.add(pos1)
        cartasAcertadas.add(pos2)
        notifyItemChanged(pos1)
        notifyItemChanged(pos2)
    }

    // vira cartas de volta após 2 segundos
    fun virarCartasDeVolta(pos1: Int, pos2: Int) {
        android.os.Handler().postDelayed({
            cartasViradas[pos1] = false
            cartasViradas[pos2] = false
            notifyItemChanged(pos1)
            notifyItemChanged(pos2)
        }, 2000)
    }

    fun isJogoConcluido(): Boolean = cartasAcertadas.size == lista.size
}