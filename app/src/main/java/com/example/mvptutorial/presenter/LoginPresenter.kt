package com.example.mvptutorial.presenter

import com.example.mvptutorial.contract.LoginContract
import com.example.mvptutorial.model.User
import com.example.mvptutorial.views.activities.LoginPage

class LoginPresenter(private var view: LoginPage): LoginContract {

    override fun onLogin(email: String, password: String) {
        val user = User(email, password)
        // isSuccessCode() is a data class User function
        when (user.isSuccessCode()) {
            1 -> {
                view.onLoginResult("Login Success", 1)
            }
            0 -> {
                view.onLoginResult("Email or Password is Empty", 0)
            }
            2 -> {
                view.onLoginResult("Wrong Email Format", 0)
            }
            3 -> {
                view.onLoginResult("Password Too Short", 0)
            }
        }
    }

}