package com.example.espanholgenialjogomemoria.activity

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.espanholgenialjogomemoria.R
import com.example.espanholgenialjogomemoria.adapter.ListarJogoMemoriaAdapter
import com.example.espanholgenialjogomemoria.dialog.EditMemoryGameDialog
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ListarMyMemoryGameActivity : BaseDrawerActivity()
{
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ListarJogoMemoriaAdapter
    private val listaJogoMemoria = mutableListOf<String>()
    private lateinit var btnCasoDeUso: FloatingActionButton
    private lateinit var auth: FirebaseAuth
    private lateinit var firestore: FirebaseFirestore

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

        carregarNomesVideos()
    }

    private fun carregarNomesVideos()
    {
        val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val firestoreRef = firestore.collection("users").document(userId).collection("jogoMemoria")

        firestoreRef.get()
            .addOnSuccessListener { result ->
                listaJogoMemoria.clear()
                for (document in result) {
                    listaJogoMemoria.add(document.id)
                }
                adapter.notifyDataSetChanged()
            }
            .addOnFailureListener { e ->
                e.printStackTrace()
            }
    }

    private fun jogarJogoMemoria(nome: String)
    {
        val intent = Intent(this, JogarJogoMemoria::class.java)
        intent.putExtra("NOME_JOGO", nome)
        startActivity(intent)
    }

    private fun editarJogoMemoria(nome: String)
    {
        val dialog = EditMemoryGameDialog()

        val bundle = Bundle()
        bundle.putString("nomeJogo", nome)
        dialog.arguments = bundle

        dialog.show(supportFragmentManager, "dialogEditar")
    }

    private fun excluirJogoMemoria(nome: String)
    {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Excluir vídeo")
        builder.setMessage("Tem certeza que deseja excluir o joga da memória,  \"$nome\" permanentemente?")

        builder.setPositiveButton("Sim") { dialog, _ ->
            dialog.dismiss()

            val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return@setPositiveButton
            val firestoreRef = firestore.collection("users").document(userId).collection("jogoMemoria").document(nome)


            firestoreRef.delete()
                .addOnSuccessListener {

                    // Remove da lista local
                    listaJogoMemoria.remove(nome)
                    adapter.notifyDataSetChanged()

                    Toast.makeText(
                        this,
                        "Jogo \"$nome\" excluído com sucesso!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                .addOnFailureListener {
                    Toast.makeText(
                        this,
                        "Erro ao excluir o jogo. Tente novamente.",
                        Toast.LENGTH_SHORT
                    ).show()
                }
        }

        builder.setNegativeButton("Cancelar") { dialog, _ ->
            dialog.dismiss()
        }

        builder.show()
    }
}