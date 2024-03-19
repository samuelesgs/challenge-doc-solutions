package com.example.docsolution

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject

class ActivityLogin : AppCompatActivity() {

    val url = "https://techhub.docsolutions.com/OnBoardingPre/WebApi/api/authentication/authentication"

    lateinit var user : String
    lateinit var password : String
    lateinit var editUser : EditText
    lateinit var editPassword : EditText

    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        editUser = findViewById(R.id.editUser)
        editPassword = findViewById(R.id.editPassword)
        sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        findViewById<Button>(R.id.buttonSearch).setOnClickListener {
            login()
        }
    }

    private fun login(){
        this.user = editUser.text.toString()
        this.password = editPassword.text.toString()
        if (password == "" || user == "") {
            Toast.makeText(this, "Introduzca un usuario o contraseña", Toast.LENGTH_SHORT).show()
        } else {
            sendRequest()
        }
    }

    private fun sendRequest() {
        val params = JSONObject()
        val body = JSONObject()
        body.put("Username", user)
        body.put("Password", password)
        params.put("Body",body)
        val sendRequestVolley = Volley.newRequestQueue(this)
        val request = JsonObjectRequest( Request.Method.POST, url, params, {response ->
            val isOk = response.getBoolean("IsOK")
            if (!isOk) {
                Toast.makeText(this, "Sus datos son incorrectos, verifique su información e intente nuevamente", Toast.LENGTH_SHORT).show()
            } else {
                saveDataPreference(response.getJSONObject("Body") )
            }
        }, {
            Log.i("RESPONSE_LOGIN_ERROR", "Algo a ido mal: ${it}")
        })
        sendRequestVolley.add(request)
    }

    private fun saveDataPreference(body : JSONObject) {
        //val userLoginData =  body.getJSONObject("UserLoginData")
        val token = body.getString("Token")
        sharedPreferences.edit().putString("token", token).apply()
        sharedPreferences.edit().putBoolean("isLogged", true).apply()
        startActivity(Intent(this, ActivityList::class.java))
        finish()
    }
}