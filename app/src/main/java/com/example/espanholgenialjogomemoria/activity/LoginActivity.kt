package com.example.espanholgenialjogomemoria.activity

import androidx.appcompat.app.AppCompatActivity
import com.example.espanholgenialjogomemoria.viewholder.ImageViewHolder
import com.example.espanholgenialjogomemoria.viewholder.LoginActivityViewHolder
import com.google.firebase.auth.FirebaseAuth

class LoginActivity: AppCompatActivity()
{
    //declaração dos objetos
    private lateinit var imageViewHolder: ImageViewHolder
    private lateinit var loginActivityViewHolder: LoginActivityViewHolder
    private lateinit var auth: FirebaseAuth
}