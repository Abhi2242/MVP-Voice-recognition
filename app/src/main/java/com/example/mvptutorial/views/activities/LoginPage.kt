package com.example.mvptutorial.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mvptutorial.R
import com.example.mvptutorial.presenter.LoginPresenter

class LoginPage : AppCompatActivity() {

    private lateinit var loginPresenter: LoginPresenter
    private lateinit var btnLogin: Button
    private lateinit var email: EditText
    private lateinit var pass: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_page)

        btnLogin = findViewById(R.id.btn_login)
        email = findViewById(R.id.et_user_name)
        pass = findViewById(R.id.et_password)
        loginPresenter = LoginPresenter(this)

        btnLogin.setOnClickListener {
            // check if email / password is linked and has correct format
            loginPresenter.onLogin(email.text.toString(), pass.text.toString())
        }
    }

    // get and check for success code from LoginPresenter
    fun onLoginResult(message: String, code: Int){
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
        if (code == 1){
            // on success code start activity and apply intent flags
            startActivity(
                Intent(this, DogBreedActivity::class.java)
                .addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK)
            )
        }
    }

}