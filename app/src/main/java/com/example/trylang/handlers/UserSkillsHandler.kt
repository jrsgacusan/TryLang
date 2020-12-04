package com.example.trylang.handlers

import com.example.trylang.models.Service
import com.example.trylang.models.UserSkills
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class UserSkillsHandler {

    var database: FirebaseDatabase
    var userSkillsRef: DatabaseReference

    init {
        database = FirebaseDatabase.getInstance()
        userSkillsRef = database.getReference("user_skills")
    }

    fun createSkill(userSkills: UserSkills): Boolean{
        val id = userSkillsRef.push().key
        userSkills.uid = id
        userSkillsRef.child(id!!).setValue(userSkills)
        return true
    }

    fun deleteSkill(userSkill: UserSkills): Boolean {
        userSkillsRef.child(userSkill.uid!!).removeValue()
        return true
    }
}