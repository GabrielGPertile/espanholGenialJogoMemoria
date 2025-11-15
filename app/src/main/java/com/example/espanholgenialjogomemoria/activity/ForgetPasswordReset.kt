package com.example.espanholgenialjogomemoria.activity

import androidx.appcompat.app.AppCompatActivity
import com.example.espanholgenialjogomemoria.viewholder.ForgetPasswordResetViewHolder
import com.example.espanholgenialjogomemoria.viewholder.ImageViewHolder
import com.google.firebase.auth.FirebaseAuth

class ForgetPasswordReset: AppCompatActivity()
{
    //declaração dos objetos
    private lateinit var imageViewHolder: ImageViewHolder
    private lateinit var forgetPasswordResetViewHolder: ForgetPasswordResetViewHolder
    private lateinit var auth: FirebaseAuth
}