package com.example.espanholgenialjogomemoria.viewholder

import android.app.Activity
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.example.espanholgenialjogomemoria.R

class ForgetPasswordResetViewHolder(activity: Activity)
{
    val tvEmail: TextView = activity.findViewById(R.id.tvEmail)
    val etLoginMail: EditText = activity.findViewById(R.id.etLoginMail)
    val btnEnviar: Button = activity.findViewById(R.id.btnEnviar)
    val btnVoltar: Button = activity.findViewById(R.id.btnVoltar)
}