package com.example.espanholgenialjogomemoria.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.espanholgenialjogomemoria.R
import com.example.espanholgenialjogomemoria.viewholder.ForgetPasswordResetViewHolder
import com.example.espanholgenialjogomemoria.viewholder.ImageViewHolder
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordReset: AppCompatActivity()
{
    //declaração dos objetos
    private lateinit var imageViewHolder: ImageViewHolder
    private lateinit var forgetPasswordResetViewHolder: ForgetPasswordResetViewHolder
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.forget_password_reset)

        //Inicializa os objetos
        imageViewHolder = ImageViewHolder(this)
        forgetPasswordResetViewHolder = ForgetPasswordResetViewHolder(this)

        //Inicializa o Auth do Firebase
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()

        // Configuração dos Botões
        forgetPasswordResetViewHolder.btnEnviar.setOnClickListener {
            sendPasswordResetEmail()
        }

        forgetPasswordResetViewHolder.btnVoltar.setOnClickListener {
            navigateBackToLogin()
        }
    }
}