package com.example.docsolution

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView

class AdapterView(val context : Context, val list : ArrayList<ModelUser>) : BaseAdapter() {
    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): ModelUser {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getView(position: Int, view: View?, parent: ViewGroup?): View {
        var currentView = view
        if (currentView == null ) {
            currentView = LayoutInflater.from(context).inflate(R.layout.row, null)
        }

        val model = getItem(position)

        val textUser = currentView?.findViewById<TextView>(R.id.textUser)
        val textName = currentView?.findViewById<TextView>(R.id.textName)
        val textFatherLastName = currentView?.findViewById<TextView>(R.id.textFatherLastName)
        val textCreationDate = currentView?.findViewById<TextView>(R.id.textCreationDate)
        val textEmail = currentView?.findViewById<TextView>(R.id.textEmail)
        val textPhoneNumber = currentView?.findViewById<TextView>(R.id.textPhoneNumber)
        textUser?.text = model.username
        textName?.text = model.name
        textFatherLastName?.text = model.fatherLastName
        textCreationDate?.text = model.creationDate
        textEmail?.text = model.email
        textPhoneNumber?.text = model.phoneNumber
        return currentView!!

    }
}