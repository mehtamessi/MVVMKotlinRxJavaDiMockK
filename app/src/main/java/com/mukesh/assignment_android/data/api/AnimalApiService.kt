package com.mukesh.assignment_android.data.api

import com.mukesh.assignment_android.data.model.Animal
import com.mukesh.assignment_android.data.model.ApiKey
import com.mukesh.assignment_android.di.DaggerApiComponent
import io.reactivex.Single
import javax.inject.Inject


class AnimalApiService {
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