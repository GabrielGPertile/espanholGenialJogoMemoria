package com.example.espanholgenialjogomemoria.strategy

class Categoria {

    fun addCategoria(): List<String>
    {
        return listOf(
            "Selecione uma categoria", // Indicador para seleção obrigatória
            "Palavras Mágicas",
            "Alimentos",
            "Expressões Linguísticas",
            "Conteúdo Extra"
        )
    }
}
