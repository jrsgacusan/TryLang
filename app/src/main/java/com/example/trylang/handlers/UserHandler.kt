package com.example.trylang.handlers

import com.example.trylang.models.User
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserHandler {

    var database: FirebaseDatabase
    var userRef: DatabaseReference

    init {
        database = FirebaseDatabase.getInstance()
        userRef = database.getReference("users")
    }

    fun update(user: User):Boolean {
        userRef.child(user.uid!!).setValue(user)
        return true
    }


}