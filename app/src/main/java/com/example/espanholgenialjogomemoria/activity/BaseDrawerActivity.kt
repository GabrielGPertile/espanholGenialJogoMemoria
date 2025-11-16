package com.example.espanholgenialjogomemoria.activity

import android.content.Intent
import android.graphics.Color
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.espanholgenialjogomemoria.R
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class BaseDrawerActivity : AppCompatActivity()
{
    protected lateinit var drawerLayout: DrawerLayout
    protected lateinit var navView: NavigationView
    protected lateinit var toggle: ActionBarDrawerToggle

    protected fun setupDrawer(drawerLayout: DrawerLayout, navView: NavigationView, toolbar: androidx.appcompat.widget.Toolbar)
    {
        this.drawerLayout = drawerLayout
        this.navView = navView

        toggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            toolbar,
            R.string.navigation_drawer_open,
            R.string.navigation_drawer_close
        )

        toggle.drawerArrowDrawable.color = Color.WHITE  // substitua RED pela cor que quiser

        drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        setupNavigationMenu()
    }

    private fun setupNavigationMenu()
    {
        navView.setNavigationItemSelectedListener { item ->
            when(item.itemId) {
                R.id.menu_perfil -> {
                    navigateToUserActivity()
                }
                R.id.menu_dashboard_principal -> { navigateToMainDashboard() }
                R.id.menu_jogo_memoria -> {}
                R.id.menu_SobreNos -> { navigateToMainAboutUs() }
                R.id.menu_sair -> {
                    deslogFirebase()
                }
            }

            drawerLayout.closeDrawers()
            true
        }
    }

    private fun deslogFirebase()
    {
        // desloga do Firebase
        FirebaseAuth.getInstance().signOut()
        Toast.makeText(this, "Deslogado com sucesso!", Toast.LENGTH_SHORT).show()

        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun navigateToUserActivity()
    {
        val intent = Intent(this, UserActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun navigateToMainDashboard()
    {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
    }
}