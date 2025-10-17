package com.example.lessons.ui.formDatas

data class BasicFormData(
    var name: String = "",
    var surname: String = "",
    var email: String = "",
    var phoneNumber: String = "",
    var yourselfDescription: String = "",
    val scenario: String = "updateBasicData"
)
