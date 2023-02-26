package com.example.firebaseapp

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseapp.Controller.ISingupController
import com.example.firebaseapp.Controller.SingupController
import com.example.firebaseapp.View.ISingupView
import com.example.firebaseapp.databinding.ActivitySingupBinding

class SingupActivity : AppCompatActivity(), ISingupView {

    private lateinit var binding: ActivitySingupBinding

    var singupPresenter: ISingupController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySingupBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //instancia del controller
        singupPresenter = SingupController(this)


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
        }
    }

    override fun OnSingupError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}