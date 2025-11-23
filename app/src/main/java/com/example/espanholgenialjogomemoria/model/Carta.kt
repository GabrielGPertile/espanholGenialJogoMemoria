package com.example.espanholgenialjogomemoria.model

data class Carta (
    val id: Int,
    val tipo: TipoCarta,
    val conteudo: String,
    val imagemUrl: String? = null
)

enum class TipoCarta {
    IMAGEM,
    TEXTO_ES,
    TEXTO_PT
}
