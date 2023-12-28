package com.bangkit23dwinovirhmwt.storyhub.data.preference

data class UserModel(
    val userId: String,
    val name: String,
    val token: String,
    val isLogin: Boolean = false
)