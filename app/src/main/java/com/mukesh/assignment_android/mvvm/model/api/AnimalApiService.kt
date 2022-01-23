package com.mukesh.assignment_android.mvvm.model.api

import android.content.Context
import com.mukesh.assignment_android.mvvm.model.model_parse_data.Animal
import com.mukesh.assignment_android.mvvm.model.model_parse_data.ApiKey
import com.mukesh.assignment_android.di.DaggerApiComponent
import io.reactivex.Single
import javax.inject.Inject


class AnimalApiService() {
    @Inject
    lateinit var api: AnimalApi

    init {
        DaggerApiComponent.create().injectApiService(this)
    }

    fun getApiAKey(): Single<ApiKey> {
        return api.getApiKey()
    }

    fun getAnimals(key: String): Single<List<Animal>> {
        return api.getAnimal(key)
    }
}