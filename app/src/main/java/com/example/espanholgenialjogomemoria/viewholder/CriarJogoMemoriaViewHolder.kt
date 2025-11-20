package com.example.espanholgenialjogomemoria.viewholder

import android.app.Activity
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.espanholgenialjogomemoria.R
import com.google.android.material.navigation.NavigationView

class CriarJogoMemoriaViewHolder(activity: Activity)
{
    val tvComentario: TextView = activity.findViewById(R.id.tvComentario)
    val tvNomeJogoMemoria: TextView = activity.findViewById(R.id.tvNomeJogoMemoria)
    val etNomeJogoMemoria: EditText = activity.findViewById(R.id.etNomeJogoMemoria)
    val tvTipoJogoMemoria: TextView = activity.findViewById(R.id.tvTipoJogoMemoria)
    val spinnerJogoMemoriaOpcoes: Spinner = activity.findViewById(R.id.spinnerJogoMemoriaOpcoes)
    val tvCategoria: TextView = activity.findViewById(R.id.tvCategoria)
    val spinnerCategoriaOpcoes: Spinner = activity.findViewById(R.id.spinnerCategoriaOpcoes)
    val tvEscolherArquivos: TextView = activity.findViewById(R.id.tvEscolherArquivos)
    val btnEscolherArquivos: Button = activity.findViewById(R.id.btnEscolherArquivos)
    val layoutArquivosSelecionados: LinearLayout = activity.findViewById(R.id.layoutArquivosSelecionados)
    val btnCasoDeUso: Button = activity.findViewById(R.id.btnCasoDeUso)
    val btnSalvar: Button = activity.findViewById(R.id.btnSalvar)
    val btnCanelar: Button = activity.findViewById(R.id.btnCancelar)

    // Elementos do menu lateral
    var toolbar: Toolbar =  activity.findViewById(R.id.toolbar)
    val drawerLayout: DrawerLayout = activity.findViewById(R.id.drawer_layout)
    val navView: NavigationView = activity.findViewById(R.id.nav_view)
}