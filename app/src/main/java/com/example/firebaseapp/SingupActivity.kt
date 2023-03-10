package com.example.firebaseapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseapp.Controller.ISingupController
import com.example.firebaseapp.Controller.SingupController
import com.example.firebaseapp.View.ISingupView
import com.example.firebaseapp.databinding.ActivitySingupBinding
import com.example.firebaseapp.enumClass.ProviderType
import com.google.firebase.firestore.FirebaseFirestore

class SingupActivity : AppCompatActivity(), ISingupView {

    private lateinit var binding: ActivitySingupBinding

    var db = FirebaseFirestore.getInstance()
    var singupPresenter: ISingupController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //instancia del controller
        singupPresenter = SingupController(this)
        //instancia de db


        binding.btnSingUp.setOnClickListener {
            (singupPresenter as SingupController).OnSingup(
                binding.etxtEmail.text.toString(),
                binding.etxtPassword.text.toString(),
                binding.etxtPasswordConfirm.text.toString()
            )

        }



        binding.txtLogIn.setOnClickListener {
            val loginIntent = Intent(this, LoginActivity::class.java)
            startActivity(loginIntent)
        }
    }

    override fun OnSingupSuccees(boolean: Boolean?) {
        if (boolean == true) {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            Toast.makeText(this, "Inicie sesion", Toast.LENGTH_SHORT).show()
            OnDBSaveData(
                binding.etxtUsuario.text.toString(),
                binding.etxtEmail.text.toString(),
                binding.etxtPhone.text.toString(),
               provider = ProviderType.BASIC
            )
        }
    }

    override fun OnSingupError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun OnDBSaveData(
        user: String,
        email: String,
        phoneNumber: String,
        provider: ProviderType
    ) {
        db.collection("user").document(email).set(
            hashMapOf(
                "provider" to provider,
                "email" to email,
                "user" to user,
                "phone number" to phoneNumber,

            )
        )
    }
}