package com.example.trylang.models

import com.google.firebase.database.IgnoreExtraProperties
import java.io.FileDescriptor

@IgnoreExtraProperties

class UserSellerInfo(var uid: String? = "",
                     var description: String? = "" ,
                     var previousSchool: String? = "",
                     var educationalAttainment: String = "") {

}