package com.mukesh.assignment_android.mvvm.model.api

import com.mukesh.assignment_android.mvvm.model.model_parse_data.Animal
import com.mukesh.assignment_android.mvvm.model.model_parse_data.ApiKey
import io.reactivex.Single
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.POST

interface AnimalApi {

    @GET("getKey")
    fun getApiKey(): Single<ApiKey>

    @FormUrlEncoded
    @POST("getAnimals")
    fun getAnimal(@Field("key")key :String):Single<List<Animal>>
}