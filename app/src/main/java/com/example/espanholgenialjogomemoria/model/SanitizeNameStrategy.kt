package com.example.espanholgenialjogomemoria.model

import com.example.espanholgenialjogomemoria.strategy.SanitizeNameInterface

class SanitizeNameStrategy : SanitizeNameInterface
{
    override fun sanitizeFileName(input: String): String? {
        val regex = Regex("[#\\[\\]*?\"<>|%\\\\{}^~:/ ]")

        if(regex.containsMatchIn(input))
        {
            throw IllegalArgumentException("O nome contém caracteres proibidos: # [ ] * ? \" < > | % \\ { } ^ ~ : / espaço")
        }

        return input
    }
}