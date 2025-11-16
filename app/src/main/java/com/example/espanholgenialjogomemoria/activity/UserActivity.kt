package com.example.espanholgenialjogomemoria.activity

import com.example.espanholgenialjogomemoria.viewholder.UserActivityViewHolder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class UserActivity : BaseDrawerActivity()
{
    private lateinit var userActivityViewHolder: UserActivityViewHolder
    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
}