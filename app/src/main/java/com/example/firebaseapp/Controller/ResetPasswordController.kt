package com.example.firebaseapp.Controller

import android.util.Patterns
import com.example.firebaseapp.View.IResetpasswordView
import com.google.firebase.auth.FirebaseAuth

class ResetPasswordController(
    private var ResetView: IResetpasswordView
): IResetPasswordController {

    lateinit var firebaseAuth: FirebaseAuth



    override fun OnReset(email: String?) {

        firebaseAuth = FirebaseAuth.getInstance()

        if (email!!.isEmpty()){
            ResetView.OnResetError("Campo vacio")
            return
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email.toString()).matches()) {
            ResetView.OnResetError("Formato de correo invalido")
            return
        }

        firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener{
            task ->
            if (task.isSuccessful) ResetView.OnResetSucces("Revisa tu correo")
            else ResetView.OnResetError(task.exception.toString())
        }


    }

}