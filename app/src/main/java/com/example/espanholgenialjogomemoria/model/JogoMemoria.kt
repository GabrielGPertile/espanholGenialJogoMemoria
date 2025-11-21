package com.example.espanholgenialjogomemoria.model

data class JogoMemoria(
    val nome: String = "",
    val tipoJogoMemoria: String = "",
    val categoria: String = "",
    val itens: List<ItemJogoMemoria> = emptyList(), // <-- aqui!
    val criadoPor: String = "",
    val id: String = ""
)

data class ItemJogoMemoria(
    val imagemURL: String = "",   // sempre existe
    val pt: String? = null,    // só quando o tipo exigir
    val es: String? = null     // só quando o tipo exigir
)