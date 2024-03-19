package com.example.docsolution

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONArray
import org.json.JSONObject

class ActivityList : AppCompatActivity() {

    private val url = "https://techhub.docsolutions.com/OnBoardingPre/WebApi/api/user/GetUsers"

    lateinit var sharedPreferences: SharedPreferences
    var list = ArrayList<ModelUser>()

    lateinit var listView : ListView
    lateinit var adapter : AdapterView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)
        listView = findViewById(R.id.listView)
        sharedPreferences = getSharedPreferences("myPreferences", Context.MODE_PRIVATE)
        val editSearch = findViewById<EditText>(R.id.editTextText)
        findViewById<Button>(R.id.buttonSearch).setOnClickListener {
            this.requestGetInformation(editSearch.text.toString().trim())
        }
        findViewById<Button>(R.id.buttonAdd).setOnClickListener {
            startActivity(Intent(this, ActivityRegister::class.java))
        }
    }

    override fun onResume() {
        super.onResume()
        requestGetInformation("")
    }

    private fun requestGetInformation(search : String) {
        Log.i("ACTIVITY_LIST", "requestGetInformation: ")
        var searchParam = search
        if (searchParam == "") {
            searchParam = "Luis"
        }
        val params = JSONObject()
        val body = JSONObject()
        body.put("SearchText", searchParam)
        params.put("Body", body)
        Log.i("REQUEST_PARAMS", "requestGetInformation: ${params}")
        val sendRequestVolley = Volley.newRequestQueue(this)
        val request : JsonObjectRequest = object :  JsonObjectRequest( Method.POST, url, params, {
            response ->
            Log.i("RESPONSE", "requestGetInformation: ${response}")
            val isOk = response.getBoolean("IsOK")
            if (!isOk) {
                Toast.makeText(this, "Esta busqueda no tiene datos", Toast.LENGTH_SHORT).show()
            } else {
                setData(response.getJSONArray("Body"))
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

    /*{
    "Id": 29,
    "Username": "jose.moreno1@docsolutions.com",
    "Password": null,
    "Name": "Jose Luis",
    "FatherLastName": "Moreno",
    "MotherLastName": "Rodriguez",
    "Active": true,
    "Locked": false,
    "CreationDate": "31\/07\/2020",
    "Tenant_Id": null,
    "Email": "jose.moreno@docsolutions.com",
    "PhoneNumber": "5545540260",
    "Metadata": [],
    "Roles": [
    {
        "Id": 3,
        "Name": "Usuario Autoconsumo"
    }
    ]
}*/

    private fun setData(body: JSONArray) {
        list.clear()

        for ( i in 0 until body.length()) {
            val row = body.getJSONObject(i)
            Log.i("PRINT", "setData: ${row}")
            val model = ModelUser(
                row.getInt("Id"),
                row.getString("Username"),
                row.getString("Name"),
                row.getString("FatherLastName"),
                row.getString("CreationDate"),
                row.getString("Email"),
                row.getString("PhoneNumber")
            )
            list.add(model)
        }
        setAdapter()
    }

    private fun setAdapter() {
        adapter = AdapterView(this, list)
        listView.adapter = adapter

    }
}