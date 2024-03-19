package com.example.docsolution

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class ActivityRegister : AppCompatActivity() {

    lateinit var sharedPreferences: SharedPreferences

    lateinit var editName : EditText
    lateinit var editFatherLastName : EditText
    lateinit var editMotherLastName : EditText
    lateinit var editEmail : EditText
    lateinit var editPhoneNumber : EditText
    lateinit var editUsername : EditText
    lateinit var editPassword : EditText
    lateinit var editConfirmPassword : EditText

    lateinit var name : String
    lateinit var fatherLastName : String
    lateinit var motherLastName : String
    lateinit var email : String
    lateinit var phoneNumber : String
    lateinit var username : String
    lateinit var password : String
    lateinit var confirmPassword : String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        editName = findViewById(R.id.editName)
        editFatherLastName = findViewById(R.id.editFatherLastName)
        editMotherLastName = findViewById(R.id.editMotherLastName)
        editEmail = findViewById(R.id.editEmail)
        editPhoneNumber = findViewById(R.id.editPhoneNumber)
        editUsername = findViewById(R.id.editUsername)
        editPassword = findViewById(R.id.editPassword)
        editConfirmPassword = findViewById(R.id.editConfirmPassword)
        findViewById<Button>(R.id.buttonSave).setOnClickListener {
            save()
        }

    }


    private fun save() {
        name = editName.text.toString().trim()
        fatherLastName = editFatherLastName.text.toString().trim()
        motherLastName = editMotherLastName.text.toString().trim()
        email = editEmail.text.toString().trim()
        phoneNumber = editPhoneNumber.text.toString().trim()
        username = editUsername.text.toString().trim()
        password = editPassword.text.toString().trim()
        confirmPassword = editConfirmPassword.text.toString().trim()
        if (name == "" && fatherLastName == "" && motherLastName == "" && email == "" && phoneNumber == "" && username == "" && password == "" && confirmPassword == "") {
            Toast.makeText(this, "Verifica tus datos ", Toast.LENGTH_SHORT).show()
        } else if (password != confirmPassword){
            Toast.makeText(this, "Las contraseñas no coinciden verifica", Toast.LENGTH_SHORT).show()
        } else {
            sendRequest()
        }
    }

    private fun sendRequest() {
        val params = JSONObject()
        val body  = JSONObject()
        val roles = JSONArray()
        val rol = JSONObject()
        rol.put("Id", 2)
        rol.put("Name", "Usuario Tradicional")
        body.put("Tenant", null)
        body.put("Name", name)
        body.put("FatherLastName", fatherLastName)
        body.put("MotherLastName", motherLastName)
        body.put("Email", email)
        body.put("PhoneNumber", phoneNumber)
        body.put("Metadata", null)
        body.put("UserName", username)
        body.put("Password", password)
        roles.put(rol)
        body.put("Roles", roles)
        params.put("Body", body)

        val sendRequestVolley = Volley.newRequestQueue(this)
        val request : JsonObjectRequest = object :  JsonObjectRequest( Method.POST, "https://techhub.docsolutions.com/OnBoardingPre/WebApi/api/user/RegisterUserRole", params, {
                response ->
            Log.i("RESPONSE", "requestGetInformation: ${response}")
            val isOk = response.getBoolean("IsOK")
            if (!isOk) {
                Toast.makeText(this, "Verifica tus datos, probablemente halla un usuario con esos datos", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Registro guardado con exito", Toast.LENGTH_SHORT).show()
                startActivity(Intent(this, ActivityList::class.java))
                finish()
            }
        }, { error ->
            Log.i("RESPONSE_LOGIN_ERROR", "Algo a ido mal: ${error}")
        }){
            override fun getHeaders() : MutableMap<String, String> {
                val params = HashMap<String, String>()
                val token = sharedPreferences.getString("token", "")
                params["Authorization"] = "Bearer ${token}"
                params["Content-Type"] = "application/json"
                params["Accept"] = "application/json"
                return params
            }
        }
        sendRequestVolley.add(request)
    }
}