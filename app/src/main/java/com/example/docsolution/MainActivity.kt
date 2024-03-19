package com.example.docsolution

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class MainActivity : AppCompatActivity() {

    lateinit var preferences : SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        preferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val isLogged = preferences.getBoolean("isLogged", false)
        var intent = Intent(this, ActivityList::class.java)
        if (!isLogged) {
             intent = Intent(this, ActivityLogin::class.java)
        }
        startActivity(intent)
    }

}