package com.example.espanholgenialjogomemoria.strategy

class SanitizeNameStrategy : SanitizeNameInterface
{
    override fun sanitizeFileName(input: String): String? {
        val regex = Regex("[#\\[\\]*?\"<>|%\\\\{}^~:/ .]")

        if(regex.containsMatchIn(input))
        {
            throw IllegalArgumentException("O nome contém caracteres proibidos: # [ ] * ? \" < > | % \\ { } ^ ~ : / espaço")
        }

        return input
    }
}