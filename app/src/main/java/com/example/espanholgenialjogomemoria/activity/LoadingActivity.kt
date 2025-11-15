package com.example.espanholgenialjogomemoria.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import com.example.espanholgenialjogomemoria.R
import com.example.espanholgenialjogomemoria.viewholder.ImageViewHolder

class LoadingActivity : AppCompatActivity()
{
    private lateinit var imageViewHolder: ImageViewHolder

    override fun onCreate(savedInstanceState: Bundle?)
    {
        // Bloqueia modo escuro e força modo claro
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)

        super.onCreate(savedInstanceState)
        setContentView(R.layout.loading_activity)

        imageViewHolder = ImageViewHolder(this)

        // Definir um atraso de 3 segundos (3000 milissegundos)
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)

            // Fechar a LoadingActivity para que o usuário não consiga voltar para ela
            finish()
        },3000)

    }
}