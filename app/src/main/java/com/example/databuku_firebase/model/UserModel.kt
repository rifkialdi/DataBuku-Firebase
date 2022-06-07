package com.example.databuku_firebase.model

data class UserModel(
    val name: String,
    val email: String,
    val password: String,
    val userUid: String?
)
