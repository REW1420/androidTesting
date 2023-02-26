package com.example.firebaseapp.View

interface ILoginView {
    fun OnLoginSuccees(boolean: Boolean? = false)
    fun OnLoginError(message: String?)
}