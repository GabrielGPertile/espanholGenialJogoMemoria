package com.example.espanholgenialjogomemoria.dialog

import android.app.Dialog
import android.os.Bundle
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.example.espanholgenialjogomemoria.R

class EditMemoryGameDialog : DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = Dialog(requireContext())

        val view = layoutInflater.inflate(R.layout.dialog_editar_dados_jogo_memoria, null)
        dialog.setContentView(view)

        // fundo transparente opcional
        dialog.window?.setBackgroundDrawableResource(android.R.color.white)

        return dialog
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
    }
}