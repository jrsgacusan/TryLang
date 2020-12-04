package com.example.trylang.models

import android.os.Parcelable

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
class User(var uid: String? = "",
           var firstName: String? = "",
           var lastName: String? ="",
           var emailAddress: String? = "",
           var mobileNumber: String? = "",
           var age: Int? = 18,
           var profileImageUrl: String? = "",
           var bio: String? = ""
)  {


}