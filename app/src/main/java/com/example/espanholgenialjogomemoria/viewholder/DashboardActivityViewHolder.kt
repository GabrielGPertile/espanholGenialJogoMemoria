package com.example.espanholgenialjogomemoria.viewholder

import android.app.Activity
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.drawerlayout.widget.DrawerLayout
import com.example.espanholgenialjogomemoria.R
import com.google.android.material.navigation.NavigationView

class DashboardActivityViewHolder(activity: Activity)
{
    //layout principal
    val ivLogoAplicativo: ImageView = activity.findViewById(R.id.ivLogoAplicativo)
    val btnCriarJogoMemoria: Button = activity.findViewById(R.id.btnCriarJogoMemoria)

    // Elementos do menu lateral
    var toolbar: Toolbar =  activity.findViewById(R.id.toolbar)
    val drawerLayout: DrawerLayout = activity.findViewById(R.id.drawer_layout)
    val navView: NavigationView = activity.findViewById(R.id.nav_view)
}