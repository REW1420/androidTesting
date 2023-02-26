package com.example.firebaseapp.Controller

import com.example.firebaseapp.View.ISingupView
import com.google.firebase.auth.FirebaseAuth

class SingupController(
    private var SingupView: ISingupView
): ISingupController {

    lateinit var firebaseAuth: FirebaseAuth

    override fun OnSingup(email: String?, password: String?, confirmPassword: String?) {
        firebaseAuth = FirebaseAuth.getInstance()


        if (email!!.isNotEmpty() && password!!.isNotEmpty() && confirmPassword!!.isNotEmpty()){
           if (password == confirmPassword){
               firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener{
                   task ->
                   if (task.isSuccessful) SingupView.OnSingupSuccees(true)
                   else SingupView.OnSingupError(task.exception.toString())
               }
            } else {
                SingupView.OnSingupError("Las contraseñas no coinciden")
            }
        } else {
            SingupView.OnSingupError("Campos en blanco")
        }
    }
}