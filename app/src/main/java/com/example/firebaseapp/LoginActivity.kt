package com.example.firebaseapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
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
import com.example.firebaseapp.enumClass.ProviderType
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

        //session checker
        sessionChecker()

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

    override fun onStart() {
        super.onStart()
        binding.loginLayout.visibility = View.VISIBLE

    }
    //fin del onCreate

    private fun sessionChecker(){
        val prefs : SharedPreferences = getSharedPreferences(getString(R.string.prefs_file),Context.MODE_PRIVATE)
        val email = prefs.getString("email", null)
        val provider = prefs.getString("provider", null)

        if (email != null && provider != null){
            binding.loginLayout.visibility = View.INVISIBLE
            showHome(email, ProviderType.valueOf(provider))
        }

    }

    override fun OnLoginSuccees(boolean: Boolean?) {
        if (boolean == true) {
            showHome(binding.etxtEmail.text.toString(),ProviderType.BASIC)
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

    fun showHome(email: String, provider: ProviderType){
        val intent = Intent(this, MainActivity::class.java).apply {
            putExtra("email",email)
            putExtra("provider",provider.name)
        }


        startActivity(intent)
    }
}

