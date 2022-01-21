package com.mukesh.assignment_android.di

import android.app.Activity
import android.app.Application
import com.mukesh.assignment_android.mvvm.model.api.AnimalApi
import com.mukesh.assignment_android.mvvm.model.api.AnimalApiService
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
open class ApiModule {

    private val BASE_URL = "https://us-central1-apis-4674e.cloudfunctions.net"

    private var client = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS).build()

    @Provides
     fun provideApiService(): AnimalApi {
       return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(AnimalApi::class.java)
    }

    @Provides
   open fun provideAnimalApiService(application: Application): AnimalApiService {
        return AnimalApiService(application)
    }

    @Singleton
    @TypeOfContext(CONTEXT_APP)
    @Provides
    open  fun contextReferenceHelper(application: Application): AnimalApiService {
        return AnimalApiService(application)
    }

    @Singleton
    @TypeOfContext(CONTEXT_ACTIVITY)
    @Provides
    fun activityReferenceHelper(activity: Activity):AnimalApiService{
        return AnimalApiService(activity)
    }
}

const val CONTEXT_APP ="application context"
const val CONTEXT_ACTIVITY ="activity context"
@Qualifier
annotation class TypeOfContext(val type:String)