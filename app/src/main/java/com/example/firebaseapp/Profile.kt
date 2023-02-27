package com.example.firebaseapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.firebaseapp.databinding.FragmentProfileBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Profile.newInstance] factory method to
 * create an instance of this fragment.
 */
class Profile : Fragment() {

    private lateinit var binding: FragmentProfileBinding

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

        binding.txtEmailShow.text = email
        binding.txtProvider.text = provider
    }
}