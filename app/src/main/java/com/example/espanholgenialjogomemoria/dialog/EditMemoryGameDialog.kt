package com.example.espanholgenialjogomemoria.dialog

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import java.net.URLDecoder
import java.nio.charset.StandardCharsets
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Spinner
import android.widget.TextView
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import com.example.espanholgenialjogomemoria.R
import com.example.espanholgenialjogomemoria.model.Imagem
import com.example.espanholgenialjogomemoria.strategy.Categoria
import com.example.espanholgenialjogomemoria.strategy.TipoJogoMemoria
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage

class EditMemoryGameDialog : DialogFragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var storage: FirebaseStorage
    private lateinit var firestore: FirebaseFirestore
    private var imagensSelecionadas: List<Imagem> = emptyList()
    private lateinit var nomeJogo: String
    private lateinit var etNome: EditText
    private lateinit var spinnerTipo: Spinner
    private lateinit var spinnerCategoria: Spinner
    private lateinit var layoutArquivos: LinearLayout
    private lateinit var btnEscolherArquivos: Button
    private lateinit var btnSalvar: Button
    private lateinit var btnCancelar: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // 🔥 Agora a variável é inicializada no momento correto
        nomeJogo = arguments?.getString("nomeJogo") ?: ""
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())

        val view = layoutInflater.inflate(R.layout.dialog_editar_dados_jogo_memoria, null)
        dialog.setContentView(view)

        dialog.window?.setBackgroundDrawableResource(android.R.color.white)

        //Inicializa o Auth do Firebase
        FirebaseApp.initializeApp(requireContext())
        auth = FirebaseAuth.getInstance()
        storage = FirebaseStorage.getInstance()
        firestore = FirebaseFirestore.getInstance()

        iniciarViews(view)

        // Agora nomeJogo JÁ está inicializado
        carregarDadosFirestore()

        btnCancelar.setOnClickListener { dismiss() }
        btnEscolherArquivos.setOnClickListener { choiceFiles() }


        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }

    private fun iniciarViews(view: View) {
        etNome = view.findViewById(R.id.etNomeJogoMemoria)
        spinnerTipo = view.findViewById(R.id.spinnerJogoMemoriaOpcoes)
        spinnerCategoria = view.findViewById(R.id.spinnerCategoriaOpcoes)
        layoutArquivos = view.findViewById(R.id.layoutArquivosSelecionados)
        btnEscolherArquivos = view.findViewById(R.id.btnEscolherArquivos)
        btnSalvar = view.findViewById(R.id.btnSalvar)
        btnCancelar = view.findViewById(R.id.btnCancelar)
    }

    private fun carregarDadosFirestore()
    {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return

        val firestore = FirebaseFirestore.getInstance()

        firestore.collection("users")
            .document(userId)
            .collection("jogoMemoria")
            .document(nomeJogo)
            .get()
            .addOnSuccessListener { doc ->
                if (!doc.exists()) return@addOnSuccessListener

                val nome = doc.getString("nome") ?: ""
                val tipo = doc.getString("tipoJogoMemoria") ?: ""
                val categoria = doc.getString("categoria") ?: ""
                val arquivosFirestore = doc.get("itens") as? List<Map<String, Any>> ?: emptyList()

                val arquivos = arquivosFirestore.map { mapa ->
                    Imagem(
                        nome = mapa["es"] as? String ?: "",
                        url = mapa["imagemURL"] as? String ?: "",
                        selecionado = true
                    )
                }

                etNome.setText(nome)

                preencherSpinnerTipo(tipo)
                preencherSpinnerCategoria(categoria)

                imagensSelecionadas = arquivos  // salva dentro da classe

                mostrarArquivosSelecionados(arquivos)

                Log.d("DEBUG_FIRESTORE", arquivosFirestore.toString())

            }
    }

    private fun selecionarItemSpinner(spinner: Spinner, valor: String) {
        val adapter = spinner.adapter ?: return
        for (i in 0 until adapter.count) {
            if (adapter.getItem(i).toString() == valor) {
                spinner.setSelection(i)
                break
            }
        }
    }

    private fun preencherSpinnerTipo(tipoSelecionado: String) {

        val lista = TipoJogoMemoria().addTipoJogoMemoria()

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, lista)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerTipo.adapter = adapter

        // só seleciona se tiver vindo do Firestore
        if (tipoSelecionado != null) {
            spinnerTipo.post {
                val pos = lista.indexOf(tipoSelecionado)
                if (pos >= 0) spinnerTipo.setSelection(pos)
            }
        }
    }

    private fun preencherSpinnerCategoria(categoriaSelecionada: String)
    {
        val lista = Categoria().addCategoria()

        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, lista)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        spinnerCategoria.adapter = adapter

        // só seleciona se tiver vindo do Firestore
        if (categoriaSelecionada != null) {
            spinnerCategoria.post {
                val pos = lista.indexOf(categoriaSelecionada)
                if (pos >= 0) spinnerCategoria.setSelection(pos)
            }
        }
    }

    private fun mostrarArquivosSelecionados(lista: List<Imagem>) {
        layoutArquivos.removeAllViews()
        val inflater = LayoutInflater.from(requireContext())

        lista.forEach { arquivo ->
            val item = inflater.inflate(R.layout.item_arquivo_selecionado, layoutArquivos, false)

            val img = item.findViewById<ImageView>(R.id.imgPreview)
            val tv = item.findViewById<TextView>(R.id.tvNomeFoto)

            // 🔥 pega o nome real do arquivo a partir da URL
            val nomeExtraido = extrairNomeDaUrl(arquivo.url.orEmpty())
            tv.text = nomeExtraido

            Glide.with(requireContext())
                .load(arquivo.url)
                .into(img)

            layoutArquivos.addView(item)
        }
    }

    private fun extrairNomeDaUrl(url: String): String {
        val decoded = URLDecoder.decode(url, StandardCharsets.UTF_8.toString())
        return decoded.substringAfterLast("/").substringBefore("?")
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
        dialog.show(parentFragmentManager, "EscolherArquivosDialog")
    }
}