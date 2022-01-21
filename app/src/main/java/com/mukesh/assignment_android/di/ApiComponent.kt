package com.mukesh.assignment_android.di

import com.mukesh.assignment_android.mvvm.model.api.AnimalApiService
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class])
interface ApiComponent {
   fun injectApiService(service: AnimalApiService)
}