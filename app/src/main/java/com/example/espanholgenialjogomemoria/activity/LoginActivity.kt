package com.example.espanholgenialjogomemoria.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.espanholgenialjogomemoria.R
import com.example.espanholgenialjogomemoria.viewholder.ImageViewHolder
import com.example.espanholgenialjogomemoria.viewholder.LoginActivityViewHolder
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class LoginActivity: AppCompatActivity()
{
    //declaração dos objetos
    private lateinit var imageViewHolder: ImageViewHolder
    private lateinit var loginActivityViewHolder: LoginActivityViewHolder
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        //Inicializa os objetos
        imageViewHolder = ImageViewHolder(this)
        loginActivityViewHolder = LoginActivityViewHolder(this)

        //Inicializa o Auth do Firebase
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

    }
}