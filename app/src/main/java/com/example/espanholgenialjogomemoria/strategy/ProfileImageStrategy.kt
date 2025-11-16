package com.example.espanholgenialjogomemoria.strategy

import android.content.Context
import android.widget.ImageView

interface ProfileImageStrategy {
    fun loadProfileImage(context: Context, imageView: ImageView, userId: String)
}