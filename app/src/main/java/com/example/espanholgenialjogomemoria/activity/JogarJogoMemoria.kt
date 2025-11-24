package com.example.espanholgenialjogomemoria.activity

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import com.example.espanholgenialjogomemoria.R
import com.example.espanholgenialjogomemoria.databinding.ActivityJogarJogoMemoriaBinding
import com.example.espanholgenialjogomemoria.model.Carta
import com.example.espanholgenialjogomemoria.model.ItemJogoMemoria
import com.example.espanholgenialjogomemoria.model.TipoCarta
import com.example.seuprojeto.adapter.ParEnAdapter
import com.example.seuprojeto.adapter.TriploAdapter
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class JogarJogoMemoria : BaseDrawerActivity()
{
    private lateinit var binding: ActivityJogarJogoMemoriaBinding
    private lateinit var firestore: FirebaseFirestore
    private lateinit var adapterTriplo: TriploAdapter
    private lateinit var adapterParEn: ParEnAdapter
    private lateinit var auth: FirebaseAuth

    private val cartasSelecionadas = mutableListOf<Pair<Carta, Int>>()

    private var nomeJogo = ""
    private var tipoJogo = "Par_ES"  // fallback padrão

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityJogarJogoMemoriaBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // 🔹 Inicializa as views do Drawer e Toolbar
        val toolbar = findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar)
        val drawerLayout = findViewById<androidx.drawerlayout.widget.DrawerLayout>(R.id.drawer_layout)
        val navView = findViewById<com.google.android.material.navigation.NavigationView>(R.id.nav_view)

        firestore = FirebaseFirestore.getInstance()
        auth = FirebaseAuth.getInstance()

        setupDrawer(
            drawerLayout,
            navView,
            toolbar
        )

        loadProfilePhotoInDrawer()

        nomeJogo = intent.getStringExtra("NOME_JOGO") ?: ""

        if (nomeJogo.isEmpty()) {
            Toast.makeText(this, "Erro: Nome do jogo não recebido!", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        binding.recyclerViewJogar.layoutManager = GridLayoutManager(this, 3)

        carregarJogo()

        Log.d("JogoMemoriaDebug", "Nome do jogo: $nomeJogo")
        Log.d("fdsfhsd", "Tipo de Jogo: $tipoJogo")
    }

    private fun carregarJogo() {
        val userId = auth.currentUser?.uid ?: return

        val ref = firestore.collection("users")
            .document(userId)
            .collection("jogoMemoria")
            .document(nomeJogo)

        ref.get().addOnSuccessListener { doc ->

            if (!doc.exists()) {
                Toast.makeText(this, "Jogo não encontrado!", Toast.LENGTH_SHORT).show()
                finish()
                return@addOnSuccessListener
            }

            tipoJogo = doc.getString("tipoJogoMemoria") ?: "Par_ES"

            // Pega a lista de itens diretamente do documento
            val itensList = doc.get("itens") as? List<Map<String, Any>>
            if (itensList.isNullOrEmpty()) {
                Toast.makeText(this, "Jogo vazio! (nenhum item no documento)", Toast.LENGTH_SHORT).show()
                return@addOnSuccessListener
            }

            // Mapeia para ItemJogoMemoria
            val lista = itensList.mapNotNull { map ->
                try {
                    ItemJogoMemoria(
                        imagemURL = map["imagemURL"] as? String ?: "",
                        pt = map["pt"] as? String,
                        es = map["es"] as? String
                    )
                } catch (e: Exception) {
                    Log.w("JogoMemoriaDebug", "Erro ao mapear item: $map")
                    null
                }
            }

            if (lista.isEmpty()) {
                Toast.makeText(this, "Jogo vazio! (nenhum item válido)", Toast.LENGTH_SHORT).show()
                return@addOnSuccessListener
            }

            // Configura o adapter
            when (tipoJogo) {
                "Par_ES" -> {
                    val listaCartas = mutableListOf<Carta>()
                    lista.forEachIndexed { index, item ->
                        // index é o ID único do item
                        listaCartas.add(Carta(index, TipoCarta.IMAGEM, "", item.imagemURL))
                        listaCartas.add(Carta(index, TipoCarta.TEXTO_ES, item.es ?: ""))
                    }

                    listaCartas.shuffle()

                    adapterParEn = ParEnAdapter(listaCartas) { carta, pos ->
                        cartasSelecionadas.add(Pair(carta, pos))

                        if (cartasSelecionadas.size == 2) {
                            val (carta1, pos1) = cartasSelecionadas[0]
                            val (carta2, pos2) = cartasSelecionadas[1]

                            if (carta1.id == carta2.id) {
                                adapterParEn.marcarParCorreto(pos1, pos2)

                                if (adapterParEn.isJogoConcluido()) {
                                    Toast.makeText(this, "Jogo concluído!", Toast.LENGTH_LONG)
                                        .show()
                                    android.os.Handler().postDelayed({ finish() }, 2000)
                                } else {
                                    Toast.makeText(this, "Par correto!", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                adapterParEn.virarCartasDeVolta(pos1, pos2)
                                Toast.makeText(this, "Errado!", Toast.LENGTH_SHORT).show()
                            }

                            cartasSelecionadas.clear()
                        }
                    }
                    binding.recyclerViewJogar.adapter = adapterParEn
                }

                "Triplo" -> {
                    val listaCartas = mutableListOf<Carta>()
                    lista.forEachIndexed { index, item ->
                        // index é o ID único do item
                        listaCartas.add(Carta(index, TipoCarta.IMAGEM, "", item.imagemURL))
                        listaCartas.add(Carta(index, TipoCarta.TEXTO_ES, item.es ?: ""))
                        listaCartas.add(Carta(index, TipoCarta.TEXTO_PT, item.pt ?: ""))
                    }

                    listaCartas.shuffle()

                    adapterTriplo = TriploAdapter(listaCartas) { carta, pos ->
                        cartasSelecionadas.add(Pair(carta, pos))

                        if (cartasSelecionadas.size == 3) {
                            val (carta1, pos1) = cartasSelecionadas[0]
                            val (carta2, pos2) = cartasSelecionadas[1]
                            val (carta3, pos3) = cartasSelecionadas[2]

                            if (carta1.id == carta2.id && carta1.id == carta3.id) {
                                adapterTriplo.marcarTrioCorreto(pos1, pos2, pos3)

                                if (adapterTriplo.isJogoConcluido()) {
                                    Toast.makeText(this, "Jogo concluído!", Toast.LENGTH_LONG)
                                        .show()
                                    Handler().postDelayed({ finish() }, 2000)
                                } else {
                                    Toast.makeText(this, "Trio correto!", Toast.LENGTH_SHORT).show()
                                }
                            } else {
                                adapterTriplo.virarCartasDeVolta(pos1, pos2, pos3)
                                Toast.makeText(this, "Errado!", Toast.LENGTH_SHORT).show()
                            }

                            cartasSelecionadas.clear()
                        }
                    }

                    binding.recyclerViewJogar.adapter = adapterTriplo
                }
            }
        }.addOnFailureListener {
            Toast.makeText(this, "Erro ao acessar o jogo!", Toast.LENGTH_SHORT).show()
        }
    }
}