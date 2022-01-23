package com.mukesh.assignment_android.di

import android.app.Activity
import android.app.Application
import com.mukesh.assignment_android.utils.SharedPreferenceHelper

import dagger.Module
import dagger.Provides
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
open class PrefModule {
    @Singleton
    @TypeOfContext(CONTEXT_APP)
    @Provides
   open  fun provideSharedPrefHelper(application: Application): SharedPreferenceHelper {
       return SharedPreferenceHelper(application)
    }

    @Singleton
    @TypeOfContext(CONTEXT_ACTIVITY)
    @Provides
    fun provideSharedPrefHelperByActivity(activity: Activity):SharedPreferenceHelper{
        return SharedPreferenceHelper(activity)
    }
}

const val CONTEXT_APP ="application context"
const val CONTEXT_ACTIVITY ="activity context"
@Qualifier
annotation class TypeOfContext(val type:String)