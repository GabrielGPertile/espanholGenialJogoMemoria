package com.example.espanholgenialjogomemoria.activity

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.espanholgenialjogomemoria.R
import com.example.espanholgenialjogomemoria.dialog.EscolherArquivosDialog
import com.example.espanholgenialjogomemoria.model.Imagem
import com.example.espanholgenialjogomemoria.model.ItemJogoMemoria
import com.example.espanholgenialjogomemoria.model.JogoMemoria
import com.example.espanholgenialjogomemoria.model.SanitizeNameStrategy
import com.example.espanholgenialjogomemoria.strategy.Categoria
import com.example.espanholgenialjogomemoria.strategy.SanitizeNameInterface
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
    private var selectTipoJogoMemoria: String? = null
    private var selectCategoria: String? = null
    private var imagensSelecionadas: List<Imagem> = emptyList()
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

        // Spinner da categoria
        spinnerCategoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectCategoria = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        //spiner do tipo de jogo da memória
        spinnerTipoJogoMemoria.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: View?, position: Int, id: Long) {
                selectTipoJogoMemoria = parent.getItemAtPosition(position).toString()
            }

            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        //configura o menu lateral
        setupDrawer(
            criarJogoMemoriaViewHolder.drawerLayout,
            criarJogoMemoriaViewHolder.navView,
            criarJogoMemoriaViewHolder.toolbar
        )

        loadProfilePhotoInDrawer()

        //configuração dos botões
        criarJogoMemoriaViewHolder.btnEscolherArquivos.setOnClickListener {
            choiceFiles()
        }


        criarJogoMemoriaViewHolder.btnSalvar.setOnClickListener {
           saveCreatedGame(selectTipoJogoMemoria, selectCategoria)
        }

        criarJogoMemoriaViewHolder.btnCanelar.setOnClickListener {
            cancelCreateGame()
        }

        criarJogoMemoriaViewHolder.btnCasoDeUso.setOnClickListener {

        }
    }

    private fun choiceFiles() {
        val userId = auth.currentUser?.uid ?: return
        val storageRef = storage.reference.child("arquivos/$userId/imagensPublicas")

        storageRef.listAll()
            .addOnSuccessListener { result ->

                val listaImagens = mutableListOf<Imagem>()

                result.items.forEach { item ->
                    item.downloadUrl
                        .addOnSuccessListener { url ->
                            listaImagens.add(Imagem(item.name, url.toString()))

                            if (listaImagens.size == result.items.size) {
                                abrirDialog(listaImagens)
                            }
                        }
                        .addOnFailureListener {
                            Log.e("FIREBASE", "Erro ao carregar URL de ${item.name}", it)
                        }
                }
            }
    }

    private fun abrirDialog(lista: List<Imagem>) {
        val dialog = EscolherArquivosDialog(lista) { imagensSelecionadas ->
            mostrarArquivosSelecionados(imagensSelecionadas)
        }
        dialog.show(supportFragmentManager, "EscolherArquivosDialog")
    }

    private fun mostrarArquivosSelecionados(lista: List<Imagem>) {
        imagensSelecionadas = lista  // <-- salva para uso posterior

        val layout = findViewById<LinearLayout>(R.id.layoutArquivosSelecionados)
        layout.removeAllViews()

        val inflater = LayoutInflater.from(this)

        lista.forEach { arquivo ->
            val view = inflater.inflate(R.layout.item_arquivo_selecionado, layout, false)

            val img = view.findViewById<ImageView>(R.id.imgPreview)
            val tv = view.findViewById<TextView>(R.id.tvNomeFoto)

            tv.text = arquivo.nome

            Glide.with(this)
                .load(arquivo.url)
                .into(img)

            layout.addView(view)
        }

        // 🔥 Aqui pega quantos itens foram adicionados
        val quantidade = layout.childCount

        Log.d("DEBUG_LISTA", "O layout tem $quantidade itens")
    }

    private fun saveCreatedGame(selectTipoJogoMemoria: String?, selectCategoria: String?)
    {
        if(criarJogoMemoriaViewHolder.etNomeJogoMemoria.text.isEmpty())
        {
            Toast.makeText(this, "O nome do jogo não pode estar vazio!", Toast.LENGTH_LONG).show()
            return
        }

        val nomeRaw = criarJogoMemoriaViewHolder.etNomeJogoMemoria.text.toString()
        val quantidade = criarJogoMemoriaViewHolder.layoutArquivosSelecionados.childCount

        val sanitizer: SanitizeNameInterface = SanitizeNameStrategy()

        val sanitizedName = try {
            val sanitized = sanitizer.sanitizeFileName(nomeRaw)

            sanitized?.lowercase() ?: return
        } catch (e: IllegalArgumentException) {
            Toast.makeText(this, e.message, Toast.LENGTH_SHORT).show()
            return
        }

        if(selectTipoJogoMemoria == "Selecione um:")
        {
            Toast.makeText(this, "Selecione uma tipo de jogo válido", Toast.LENGTH_LONG).show()
            return
        }

        if(selectCategoria == "Selecione uma categoria")
        {
            Toast.makeText(this, "Selecione uma categoria válida", Toast.LENGTH_LONG).show()
            return
        }

        if(quantidade < 5 || quantidade > 7)
        {
            when {
                quantidade < 5 ->
                    Toast.makeText(this, "Selecione pelo menos 5 itens!", Toast.LENGTH_LONG).show()

                quantidade > 7 ->
                    Toast.makeText(this, "O máximo permitido é 7 itens!", Toast.LENGTH_LONG).show()

                else -> {
                    // Pode continuar
                }
            }
        }

        if(selectTipoJogoMemoria == "Par_EN")
        {
            val nomesSelecionados = imagensSelecionadas.map { it.nome }

            nomesSelecionados.forEach { nome ->
                val partes = nome.split("_")

                val nomeES = if(partes.size >= 2) partes[1] else null

                ItemJogoMemoria(
                    imagemURL = imagem.url,
                    pt = null,

                )
            }
        }
    }

    private fun cancelCreateGame()
    {
        finish()
    }
}