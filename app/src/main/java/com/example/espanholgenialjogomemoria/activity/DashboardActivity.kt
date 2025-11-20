package com.example.espanholgenialjogomemoria.activity

import android.os.Bundle
import com.example.espanholgenialjogomemoria.R
import com.example.espanholgenialjogomemoria.viewholder.DashboardActivityViewHolder

class DashboardActivity: BaseDrawerActivity()
{
    private lateinit var dashboardActivityViewHolder: DashboardActivityViewHolder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dashboard_activity)

        dashboardActivityViewHolder = DashboardActivityViewHolder(this)

        // 🔹 Inicializa as views do Drawer e Toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val drawerLayout =
            findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawer_layout)
        val navView =
            findViewById<com.google.android.material.navigation.NavigationView>(R.id.nav_view)

        setupDrawer(drawerLayout, navView, toolbar)
        loadProfilePhotoInDrawer()

        //configuração do botão
        dashboardActivityViewHolder.btnCriarJogoMemoria.setOnClickListener {
            navigateToCreateGame()
        }
    }
}