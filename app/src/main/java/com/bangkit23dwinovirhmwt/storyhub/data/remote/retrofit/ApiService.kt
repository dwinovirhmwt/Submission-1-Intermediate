package com.bangkit23dwinovirhmwt.storyhub.data.remote.retrofit

import com.bangkit23dwinovirhmwt.storyhub.data.remote.response.DetailResponse
import com.bangkit23dwinovirhmwt.storyhub.data.remote.response.FileUploadResponse
import com.bangkit23dwinovirhmwt.storyhub.data.remote.response.LoginResponse
import com.bangkit23dwinovirhmwt.storyhub.data.remote.response.RegisterResponse
import com.bangkit23dwinovirhmwt.storyhub.data.remote.response.StoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path


interface ApiService {

    @FormUrlEncoded
    @POST("register")
    fun registerUser(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    @FormUrlEncoded
    @POST("login")
    fun loginUser(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    @GET("stories")
    fun getAllStories(
        @Header("Authorization") token: String
    ): Call<StoryResponse>

    @GET("stories/{id}")
    fun getDetailStories(
        @Header("Authorization") token: String,
        @Path("id") id: String,
    ): Call<DetailResponse>

    @Multipart
    @POST("stories")
    fun uploadImage(
        @Header("Authorization") token: String,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
    ): Call<FileUploadResponse>
}