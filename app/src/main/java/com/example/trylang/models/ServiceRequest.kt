package com.example.trylang.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class ServiceRequest(var uid: String? = "", var title: String? =  "", var userUid: String? = "", var price: Int? = 0, var description: String? = "", var mobileNumber: String? = "") {
    override fun toString(): String {
        return "Service Request Title: $title\n" +
                "Request Description: $description\n" +
                "Target Price: Php$price"
    }
}