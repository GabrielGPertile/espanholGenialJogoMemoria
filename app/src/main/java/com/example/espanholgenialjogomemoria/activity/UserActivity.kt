package com.example.espanholgenialjogomemoria.activity

import android.content.Intent
import android.os.Bundle
import com.example.espanholgenialjogomemoria.R
import com.example.espanholgenialjogomemoria.strategy.FirebaseStorageProfileImageStrategy
import com.example.espanholgenialjogomemoria.viewholder.UserActivityViewHolder
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class UserActivity : BaseDrawerActivity()
{
    private lateinit var userActivityViewHolder: UserActivityViewHolder
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_perfile_activity)

        userActivityViewHolder = UserActivityViewHolder(this)

        //Inicializa o Auth do Firebase
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        setupDrawer(
            userActivityViewHolder.drawerLayout,
            userActivityViewHolder.navView,
            userActivityViewHolder.toolbar
        )

        loadProfilePhotoInDrawer()
        loadProfilePhotoWithStrategy()
        loadUserdata()

        //configuração do botão
        userActivityViewHolder.btnEditar.setOnClickListener {
            navigateToUserPerfileEditableActivity()
        }
    }

    private fun navigateToUserPerfileEditableActivity()
    {
        val intent = Intent(this, UserPerfileEditableActivity::class.java)
        startActivity(intent)
    }

    private fun loadProfilePhotoWithStrategy() {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val strategy = FirebaseStorageProfileImageStrategy(FirebaseStorage.getInstance())

        strategy.loadProfileImage(
            context = this,
            imageView = userActivityViewHolder.ivPerfilUsuario,
            userId = userId
        )
    }
}