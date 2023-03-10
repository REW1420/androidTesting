package com.example.firebaseapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.example.firebaseapp.databinding.FragmentHomeBinding


class Home : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño utilizando viewBinding
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Obtener una referencia al botón y configurar su comportamiento

        // Devolver la vista raíz del diseño
        return binding.root
    }


}
