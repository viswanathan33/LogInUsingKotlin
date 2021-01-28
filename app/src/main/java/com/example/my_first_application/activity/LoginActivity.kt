package com.example.my_first_application.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.my_first_application.R
import com.example.my_first_application.constant.Constants
import com.example.my_first_application.data.UserLogIn
import com.example.my_first_application.data.UserViewModel
import com.example.my_first_application.model.UserInfo
import com.example.my_first_application.util.ToCallActivity
import com.google.gson.Gson

class LoginActivity : AppCompatActivity() {
    private lateinit var mUserViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mUserViewModel = ViewModelProvider(this).get(UserViewModel::class.java)
        val actionBar = supportActionBar
        val mail = findViewById<EditText>(R.id.editTextTextEmailAddress)
        val password = findViewById<EditText>(R.id.editTextTextPassword)
        val sigup = findViewById<TextView>(R.id.signup)
        val login = findViewById<Button>(R.id.log_button)
        val sharedPreferences = this.getSharedPreferences(Constants.share_pref, Context.MODE_PRIVATE)
        actionBar!!.hide()
        login.setOnClickListener {
            val gson = Gson()
            val userInfo = UserInfo()
            userInfo.setEmail(mail.text.toString())
            userInfo.setpassword(password.text.toString())
            Log.d("TAG", "data===>$userInfo")
            val json: String = gson.toJson(userInfo)
            val editor: SharedPreferences.Editor = sharedPreferences.edit()
             if (userInfo.getEmail()?.isEmpty()!!) {
                mail.error = getString(R.string.mailEmpty)
            } else if (userInfo.getEmail()!!.trim().matches(Constants.mailPattern.toRegex())) {
                if (userInfo.getPassword()?.isEmpty()!!) {
                    password.error = getString(R.string.passwordEmpty)
                } else if (userInfo.getPassword()!!.length < 8) {
                    password.error = getString(R.string.passwordLength)
                } else if (!userInfo.getPassword()!!.trim().matches(Constants.passwordPatternSplChar.toRegex())) {
                    password.error = getString(R.string.passwordSplChar)
                } else if (!userInfo.getPassword()!!.trim().matches(Constants.passwordPatternUpperCase.toRegex())) {
                    password.error = getString(R.string.passwordUpperCase)
                } else if (!userInfo.getPassword()!!.trim().matches(Constants.passwordPatternLowerCase.toRegex())) {
                    password.error = getString(R.string.passwordLowerCase)
                } else if (!userInfo.getPassword()!!.trim().matches(Constants.passwordPatternDigit.toRegex())) {
                    password.error = getString(R.string.passwordDigit)
                } else {
                    val lMail = mail.text.toString()
                    val lPpassword = password.text.toString()
                    val userLogIn: UserLogIn = mUserViewModel.logInCheck(lMail, lPpassword)
                    if (userLogIn == null) {
                        Toast.makeText(applicationContext, getString(R.string.invalidMailOrPassword), Toast.LENGTH_SHORT).show()
                    } else {
                        //Log.d("TAG", "data===>" + userLogIn)
                        Toast.makeText(applicationContext, getString(R.string.loginSuccesfully), Toast.LENGTH_SHORT).show()
                        editor.putString(Constants.USER_INFO, json)
                        editor.apply()
                        editor.commit()
                        ToCallActivity.callActivity(this, HomeActivity::class.java)
                        finish()
                    }
                }
            } else {
                mail.error = getString(R.string.invalidMail)
            }

        }
        sigup.setOnClickListener {
            ToCallActivity.callActivity(this, SigupActivity::class.java)
            finish()
        }

    }
}