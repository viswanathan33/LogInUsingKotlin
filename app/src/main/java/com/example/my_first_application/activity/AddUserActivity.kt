package com.example.my_first_application.activity

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.example.my_first_application.R
import com.example.my_first_application.constant.Constants
import com.example.my_first_application.data.UserDetails
import com.example.my_first_application.data.UserViewModel
import com.example.my_first_application.model.UserInfo
import com.example.my_first_application.util.ToCallActivity
import com.google.gson.Gson
class AddUserActivity : AppCompatActivity() {
    private val gson=Gson()
    private lateinit var mUserViewModel: UserViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adduser)

        val sharedPreferences:SharedPreferences=this.getSharedPreferences(Constants.share_pref, Context.MODE_PRIVATE)
        val json: String? = sharedPreferences.getString(Constants.USER_INFO, "")
        val userInfo:UserInfo=gson.fromJson(json,UserInfo::class.java)
        val usermail=userInfo.getEmail().toString()
        val register=findViewById<Button>(R.id.button_register)
        mUserViewModel=ViewModelProvider(this).get(UserViewModel::class.java)
        val actionBar = supportActionBar
        actionBar!!.hide()
        register.setOnClickListener {
            val name=findViewById<EditText>(R.id.personName)
            val gender=findViewById<EditText>(R.id.gender)
            val age=findViewById<EditText>(R.id.age)
            val address=findViewById<EditText>(R.id.address)
            val username=name.text.toString()
            val usergender=gender.text.toString()
            val userage=age.text.toString()
            val useraddress=address.text.toString()
            val userDetails=UserDetails(0,username,usergender,useraddress,usermail,userage)
            mUserViewModel.addUser(userDetails)
            Toast.makeText(applicationContext, getString(R.string.userAddedSuccesfully), Toast.LENGTH_SHORT).show()
            ToCallActivity.callActivity(this, HomeActivity::class.java)
            //Log.d("TAG","data===>"+json)
            finish()
        }
    }
}