package com.example.firebaseapp

import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.example.firebaseapp.Controller.ILoginController
import com.example.firebaseapp.Controller.IResetPasswordController
import com.example.firebaseapp.Controller.LoginController
import com.example.firebaseapp.Controller.ResetPasswordController
import com.example.firebaseapp.View.ILoginView
import com.example.firebaseapp.View.IResetpasswordView
import com.example.firebaseapp.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity(), ILoginView, IResetpasswordView {

    var loginPresenter: ILoginController? = null
    var resetPresenter: IResetPasswordController? = null

    private lateinit var binding: ActivityLoginBinding
    private lateinit var firebaseAuth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)


        //intancia del controller
        loginPresenter = LoginController(this)
        resetPresenter = ResetPasswordController(this)


        firebaseAuth = FirebaseAuth.getInstance()

        //navegacio
        binding.txtSingUp.setOnClickListener {
            val singupIntent = Intent(this, SingupActivity::class.java)
            startActivity(singupIntent)
        }


        //forgot dialog

        binding.txtFotgotPassword.setOnClickListener {
            val biulder = AlertDialog.Builder(this)
            val view = layoutInflater.inflate(R.layout.alert_forgot, null)
            val userEmail = view.findViewById<EditText>(R.id.editBox)

            biulder.setView(view)
            val dialog = biulder.create()

            view.findViewById<Button>(R.id.btnReset).setOnClickListener {
                (resetPresenter as IResetPasswordController).OnReset(
                    userEmail.text.toString()
                )

                dialog.dismiss()
            }
            view.findViewById<Button>(R.id.btnCancel).setOnClickListener {
                dialog.dismiss()
            }

            if (dialog.window != null) {
                dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            }

            dialog.show()
        }

        //log in

        binding.btnLogIn.setOnClickListener {
            (loginPresenter as LoginController).OnLogin(
                binding.etxtEmail.text.toString(),
                binding.etxtPassword.text.toString()
            )


        }
    }


    //fin del onCreate


    override fun OnLoginSuccees(boolean: Boolean?) {
        if (boolean == true) {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        } else {
            Toast.makeText(this, "Ocurrio un error al iniciar sesion", Toast.LENGTH_SHORT).show()
        }
    }

    override fun OnLoginError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun OnResetSucces(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    override fun OnResetError(message: String?) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}

