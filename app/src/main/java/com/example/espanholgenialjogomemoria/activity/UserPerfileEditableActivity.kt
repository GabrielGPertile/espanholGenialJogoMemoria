package com.example.espanholgenialjogomemoria.activity

import android.content.Intent
import android.net.Uri
import androidx.activity.result.ActivityResultLauncher
import com.example.espanholgenialjogomemoria.viewholder.UserPerfileEditableViewHolder
import com.example.espanholgenialstorageandroid.model.UserClass
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
}