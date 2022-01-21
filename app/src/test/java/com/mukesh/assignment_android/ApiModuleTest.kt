package com.mukesh.assignment_android

import android.app.Application
import com.mukesh.assignment_android.mvvm.model.api.AnimalApiService
import com.mukesh.assignment_android.di.ApiModule

class ApiModuleTest(val mockService: AnimalApiService): ApiModule() {

    override fun provideAnimalApiService(application: Application): AnimalApiService {
        return mockService
    }

    override fun contextReferenceHelper(application: Application): AnimalApiService {
        return mockService
    }
   /* override fun activityReferenceHelper(activity: Activity): AnimalApiService {
        return mockService
    }*/
}