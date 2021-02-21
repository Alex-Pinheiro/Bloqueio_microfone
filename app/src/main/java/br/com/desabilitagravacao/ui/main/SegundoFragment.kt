package br.com.desabilitagravacao.ui.main

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.navigation.findNavController
import br.com.desabilitagravacao.ForegroundService
import br.com.desabilitagravacao.R


class SegundoFragment : Fragment() {

    lateinit var sharedPreferences: SharedPreferences
    var block: Boolean = false

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_segundo, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        sharedPreferences = requireContext().getSharedPreferences("preferencias", Context.MODE_PRIVATE)

        val button: Button? = view?.findViewById(R.id.button2)
        button?.setOnClickListener {
            Log.e("MediaRecorder", "Botao ok")
            ForegroundService.stopService(requireContext())
            view?.findNavController()?.navigate(R.id.action_segundoFragment_to_mainFragment)
        }
    }

    override fun onResume() {
        super.onResume()

        block = sharedPreferences.getBoolean("block", false)
        if(!block){
            view?.findNavController()?.navigate(R.id.action_segundoFragment_to_mainFragment)
        }
    }
}