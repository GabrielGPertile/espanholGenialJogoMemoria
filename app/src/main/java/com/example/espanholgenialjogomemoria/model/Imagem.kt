package com.example.espanholgenialjogomemoria.model

data class Imagem(
    val nome: String,
    val url: String? = null,
    var selecionado: Boolean = false
)
