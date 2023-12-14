package com.example.mvptutorial.model

import android.text.TextUtils
import android.util.Patterns

data class User(private val email: String, private val password: String) {
    fun isSuccessCode(): Int {
        if (TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
            return 0
        }
        else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return 2
        }
        else if (password.length <= 6){
            return 3
        }
        return 1 // success
    }
}
