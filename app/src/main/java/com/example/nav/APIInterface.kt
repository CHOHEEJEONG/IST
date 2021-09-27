package com.example.nav

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

data class ResponseBody(var result:String? = null)

interface APIInterface {
    @GET("/")
    fun getRequest(@Query("name") name: String): Call<ResponseBody>

    @FormUrlEncoded
    @POST("/")
    fun postRequest(
        @Field("userInfo")userInfo: String,
        @Field("origin")origin: String,
        @Field("convert")convert: String
    ):Call<ResponseBody>

    @Multipart
    @POST("/transform")
    fun userEdit(
        //@Header("token") token: String?,
        @Part image: MultipartBody.Part?,
        @PartMap data: HashMap<String?, RequestBody?>?
    ): Call<ResponseBody?>?

    @FormUrlEncoded
    @PUT("/{id}")
    fun putRequest(@Path("id")id: String,
                   @Field("content")content: String): Call<ResponseBody>

    @DELETE("/{id}")
    fun deleteRequest(@Path("id")id: String): Call<ResponseBody>
}