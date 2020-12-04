package com.example.trylang.handlers

import com.example.trylang.models.Service
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ServiceHandler {


    var database: FirebaseDatabase
    var serviceRef: DatabaseReference

    init {
        database = FirebaseDatabase.getInstance()
        serviceRef = database.getReference("services")
    }

    fun createService(service: Service): Boolean{
        val id = serviceRef.push().key
        service.uid = id
        serviceRef.child(id!!).setValue(service)
        return true
    }

    fun updateService(service: Service):Boolean {
        serviceRef.child(service.uid!!).setValue(service)
        return true
    }

    fun deleteService(service: Service): Boolean {
        serviceRef.child(service.uid!!).removeValue()
        return true
    }
}