package com.mukesh.assignment_android

import com.mukesh.assignment_android.data.api.AnimalApiService
import com.mukesh.assignment_android.di.ApiModule

class ApiModuleTest(val mockService: AnimalApiService): ApiModule() {

    override fun provideAnimalApiService(): AnimalApiService {
        return mockService
    }
}