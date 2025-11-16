package com.example.espanholgenialjogomemoria.activity

import android.graphics.Color
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import com.example.espanholgenialjogomemoria.R
import com.google.android.material.navigation.NavigationView

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
}