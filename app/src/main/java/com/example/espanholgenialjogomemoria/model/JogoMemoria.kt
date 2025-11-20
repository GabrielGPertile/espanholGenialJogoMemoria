package com.example.espanholgenialjogomemoria.model

data class JogoMemoria(
    val nome: String = "",
    val tipoJogoMemoria: String = "",
    val categoria: String = "",
    val arquivos: List<String> = emptyList(),
    val criadoPor: String = "",
    val id: String = ""
)