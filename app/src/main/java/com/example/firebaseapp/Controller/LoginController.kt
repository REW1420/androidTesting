package com.example.firebaseapp.Controller

import android.content.Intent
import androidx.core.content.ContextCompat.startActivity
import com.example.firebaseapp.MainActivity
import com.example.firebaseapp.View.ILoginView
import com.google.firebase.auth.FirebaseAuth

class LoginController(
    private val LoginView : ILoginView,


): ILoginController {

     lateinit var firebaseAuth: FirebaseAuth

    override fun OnLogin(email: String?, password: String?) {

        firebaseAuth = FirebaseAuth.getInstance()

        firebaseAuth.signInWithEmailAndPassword(email!!,password!!).addOnCompleteListener{
            task ->
            if (task.isSuccessful){
               LoginView.OnLoginSuccees(true)
            } else {
                LoginView.OnLoginError(task.exception.toString())
            }
        }

    }


}