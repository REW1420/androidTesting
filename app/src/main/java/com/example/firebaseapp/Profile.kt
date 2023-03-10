package com.example.firebaseapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.firebaseapp.databinding.FragmentProfileBinding
import com.google.firebase.firestore.FirebaseFirestore

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


class Profile : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    var db = FirebaseFirestore.getInstance()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflar el diseño utilizando viewBinding
        binding = FragmentProfileBinding.inflate(inflater, container, false)

        //context
        val context = activity?.applicationContext
        // Acceder a las preferencias compartidas del contexto
        val sharedPreferences =
            context?.getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE)
        val email = sharedPreferences?.getString("email", null)
        val provider = sharedPreferences?.getString("provider", null)

        //funcion init para llamar a la base de datos


        //body
        setup(email!!, provider!!)

        // log out action


        binding.btnSingout.setOnClickListener {

            val prefs = context.getSharedPreferences(
                getString(R.string.prefs_file),
                Context.MODE_PRIVATE
            )!!
                .edit()
            prefs.clear()
            prefs.apply()

            showLogin()
        }

        // Devolver la vista raíz del diseño
        return binding.root
    }

    private fun showLogin() {
        val intent = Intent(context, LoginActivity::class.java)
        startActivity(intent)

    }


    fun setup(email: String, provider: String) {



        db.collection("user").document(email).get().addOnSuccessListener { res ->
            binding.txtPhone.text = (res.get("phone number") as String?).toString()
            binding.txtUser.text = (res.get("user") as String?).toString()


        }

        binding.txtEmailShow.text = email
        binding.txtProvider.text = provider

    }


}
