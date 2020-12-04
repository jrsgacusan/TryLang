package com.example.trylang.models

import android.icu.text.CaseMap
import android.os.health.UidHealthStats
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class Service(var uid: String? = "", var title: String? = "", var description: String? = "",
              var price: Int? = 0, var category: String? = "", var userUid: String? ="", var userImageUrl: String? = "") {
    override fun toString(): String {
        return "Category: $category\nService Title: $title \nDescription: $description\nPrice: Php$price"
    }
}