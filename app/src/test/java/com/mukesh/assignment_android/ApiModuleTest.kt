package com.mukesh.assignment_android

import com.mukesh.assignment_android.di.ApiModule
import com.mukesh.assignment_android.mvvm.model.api.AnimalApiService


class ApiModuleTest(val mockService:AnimalApiService): ApiModule() {

    override fun provideAnimalApiService(): AnimalApiService {
        return mockService
    }
}