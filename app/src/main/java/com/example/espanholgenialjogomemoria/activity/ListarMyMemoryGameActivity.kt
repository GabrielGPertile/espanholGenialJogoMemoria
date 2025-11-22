package com.example.espanholgenialjogomemoria.activity

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.espanholgenialjogomemoria.R
import com.example.espanholgenialjogomemoria.adapter.ListarJogoMemoriaAdapter
import com.example.espanholgenialjogomemoria.model.SanitizeNameStrategy
import com.example.espanholgenialjogomemoria.strategy.SanitizeNameInterface
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class ListarMyMemoryGameActivity : BaseDrawerActivity()
{
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListarJogoMemoriaAdapter
    private val listaJogoMemoria = mutableListOf<String>()
    private lateinit var btnCasoDeUso: FloatingActionButton
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore
    private val sanitizer: SanitizeNameInterface = SanitizeNameStrategy()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.listar_jogo_memoria)

        btnCasoDeUso = findViewById(R.id.btnCasoDeUso)

        // 🔹 Inicializa as views do Drawer e Toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val drawerLayout = findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawer_layout)
        val navView = findViewById<com.google.android.material.navigation.NavigationView>(R.id.nav_view)

        //Inicializa o Auth do Firebase
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        firestore = FirebaseFirestore.getInstance()

        setupDrawer(
            drawerLayout,
            navView,
            toolbar
        )

        loadProfilePhotoInDrawer()

        recyclerView = findViewById(R.id.recyclerViewJogoMemoria)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = ListarJogoMemoriaAdapter(
            listaJogoMemoria,
            onJogar = { nome -> jogarJogoMemoria(nome) },
            onEditar = { nome -> editarJogoMemoria(nome) },
            onExcluir = { nome -> excluirJogoMemoria(nome) }
        )
        recyclerView.adapter = adapter
    }
}