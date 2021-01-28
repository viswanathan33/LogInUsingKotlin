package com.example.my_first_application.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
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
import com.example.my_first_application.util.extensions.savePreference
import com.google.gson.Gson

class SigupActivity : AppCompatActivity() {
    private lateinit var mUserViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        val actionBar = supportActionBar
        val name=findViewById<EditText>(R.id.editTextTextPersonName)
        val email=findViewById<EditText>(R.id.editTextTextEmailAddress1)
        val password=findViewById<EditText>(R.id.editTextTextPassword1)
        val confirmPassword=findViewById<EditText>(R.id.editTextTextPassword_confirm)
        val sigup=findViewById<Button>(R.id.button_signup)
        val login=findViewById<TextView>(R.id.textView_login)
        val sharedPreferences=getSharedPreferences(Constants.share_pref, Context.MODE_PRIVATE)
        mUserViewModel= ViewModelProvider(this).get(UserViewModel::class.java)
        actionBar!!.hide()
        sigup.setOnClickListener {
            val gson=Gson()

           // name.toString().savePreference("Key")

            val userInfo= UserInfo()
            userInfo.setName(name.text.toString())
            userInfo.setEmail(email.text.toString())
            userInfo.setpassword(password.text.toString())
            userInfo.setConfirmPassword(confirmPassword.text.toString())
            val editor:SharedPreferences.Editor=sharedPreferences.edit()
            val json:String=gson.toJson(userInfo)
            if (userInfo.getName()?.isEmpty()!!)
            {
                name.error = getString(R.string.nameEmpty)
            }
            else if (userInfo.getEmail()?.isEmpty()!!) {
                email.error = getString(R.string.mailEmpty)
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
                }
                else if (userInfo.getPassword()!! != userInfo.getConfirmPassword()) {
                    confirmPassword.error = getString(R.string.passwordMatch)
                }
                else {
                    val dbmail=email.text.toString()
                    val dbpassword=password.text.toString()
                    val userLogIn= UserLogIn(dbmail,dbpassword)
                    mUserViewModel.addLogin(userLogIn)
                    Toast.makeText(applicationContext, getString(R.string.signUpSuccesfully), Toast.LENGTH_SHORT).show()
                    editor.putString(Constants.USER_INFO, json)
                    editor.apply()
                    editor.commit()
                    ToCallActivity.callActivity(this, HomeActivity::class.java)
                    finish()
                }
            } else {
                email.error = getString(R.string.invalidMail)
            }

        }
        login.setOnClickListener {
            ToCallActivity.callActivity(this, LoginActivity::class.java)
            finish()
        }
    }
}
