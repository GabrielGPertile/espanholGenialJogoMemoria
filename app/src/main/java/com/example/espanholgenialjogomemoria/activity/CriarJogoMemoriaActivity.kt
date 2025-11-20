package com.example.espanholgenialjogomemoria.activity

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import com.example.espanholgenialjogomemoria.R
import com.example.espanholgenialjogomemoria.model.JogoMemoria
import com.example.espanholgenialjogomemoria.strategy.Categoria
import com.example.espanholgenialjogomemoria.strategy.TipoJogoMemoria
import com.example.espanholgenialjogomemoria.viewholder.CriarJogoMemoriaViewHolder
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.storage.FirebaseStorage

class CriarJogoMemoriaActivity: BaseDrawerActivity()
{
    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var jogoMemoria: JogoMemoria
    private lateinit var categoria: Categoria
    private lateinit var tipoJogoMemoria: TipoJogoMemoria
    private lateinit var criarJogoMemoriaViewHolder: CriarJogoMemoriaViewHolder

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.criar_jogo_memoria)

        criarJogoMemoriaViewHolder = CriarJogoMemoriaViewHolder(this)
        categoria = Categoria()
        tipoJogoMemoria = TipoJogoMemoria()

        //Inicializa o Auth do Firebase
        FirebaseApp.initializeApp(this)
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()

        //obtem a lista de dados
        val categoriaList = categoria.addCategoria()
        val tipoJogoMemoriaList = tipoJogoMemoria.addTipoJogoMemoria()

        //Referencia o spinner do layout
        val spinnerCategoria: Spinner = criarJogoMemoriaViewHolder.spinnerCategoriaOpcoes
        val spinnerTipoJogoMemoria: Spinner = criarJogoMemoriaViewHolder.spinnerJogoMemoriaOpcoes

        //Cria um adapter com os dados das listas
        val adapterCategoria = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categoriaList)
        val adapterTipoJogoMemoria = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, tipoJogoMemoriaList)

        spinnerCategoria.adapter = adapterCategoria
        spinnerTipoJogoMemoria.adapter = adapterTipoJogoMemoria

        //configura o menu lateral
        setupDrawer(
            criarJogoMemoriaViewHolder.drawerLayout,
            criarJogoMemoriaViewHolder.navView,
            criarJogoMemoriaViewHolder.toolbar
        )

        loadProfilePhotoInDrawer()
    }
}