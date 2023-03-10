package com.example.firebaseapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.firebaseapp.Controller.LoginController
import com.example.firebaseapp.Interfaces.APIService
import com.example.firebaseapp.Model.DogsResponse
import com.example.firebaseapp.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        replaceFragment(Home())

        val bundle: Bundle? = intent.extras
        val email: String? = bundle?.getString("email")
        val provider: String?=bundle?.getString("provider")

        val pref: SharedPreferences.Editor = getSharedPreferences(getString(R.string.prefs_file), Context.MODE_PRIVATE).edit()
        pref.putString("email", email)
        pref.putString("provider", provider)
        pref.apply()


























        binding.bottomNavigationView.setOnItemSelectedListener {




            when (it.itemId) {

                R.id.home -> replaceFragment(Home())
                R.id.profile -> replaceFragment(Profile())
                R.id.search -> replaceFragment(Search())
                R.id.settings -> replaceFragment(SettingsFragment())

                else -> {


                }

            }

            true

        }

    }


    private fun replaceFragment(fragment: Fragment) {

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()

        fragmentTransaction.replace(R.id.frame_layout, fragment)
        fragmentTransaction.commit()
    }


}