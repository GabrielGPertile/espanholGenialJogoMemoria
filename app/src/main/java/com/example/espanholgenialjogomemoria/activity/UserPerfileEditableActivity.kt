package com.example.espanholgenialjogomemoria.activity

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import com.example.espanholgenialjogomemoria.R
import com.example.espanholgenialjogomemoria.viewholder.UserPerfileEditableViewHolder
import com.example.espanholgenialstorageandroid.model.UserClass
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class UserPerfileEditableActivity : BaseDrawerActivity()
{
    private lateinit var userPerfileEditableViewHolder: UserPerfileEditableViewHolder
    private lateinit var pickImageLauncher: ActivityResultLauncher<Intent>
    private var selectedImageUri: Uri? = null
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var user: UserClass

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.user_perfile_editable_activity)

        //Inicializa os objetos
        userPerfileEditableViewHolder = UserPerfileEditableViewHolder(this)

        //inicializando a classe user
        user = UserClass()

        //Inicializa o Auth do Firebase
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        //configura o menu lateral
        setupDrawer(
            userPerfileEditableViewHolder.drawerLayout,
            userPerfileEditableViewHolder.navView,
            userPerfileEditableViewHolder.toolbar
        )

        userPerfileEditableViewHolder.etNomeCompletoDado.clearFocus()
        userPerfileEditableViewHolder.etIdadeDado.clearFocus()

        loadProfilePhotoInDrawer()
        loadProfilePhotoWithStrategy()

        pickImageLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if(result.resultCode == RESULT_OK)
            {
                val data: Intent? = result.data
                val uri = data?.data

                if(uri != null)
                {
                    selectedImageUri = uri

                    val bitmap = getCorrectlyOrientedBitmap(uri)

                    if (bitmap != null) {
                        userPerfileEditableViewHolder.ivPerfilUsuario.setImageBitmap(bitmap)
                    }
                }
            }
        }
    }
}