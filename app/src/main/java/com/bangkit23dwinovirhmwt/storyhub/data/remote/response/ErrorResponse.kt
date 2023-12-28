package com.bangkit23dwinovirhmwt.storyhub.data.remote.response

import com.google.gson.annotations.SerializedName

data class ErrorResponse(

    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String? = null
)
