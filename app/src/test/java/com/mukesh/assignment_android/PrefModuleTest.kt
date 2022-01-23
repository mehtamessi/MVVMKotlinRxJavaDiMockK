package com.rajesh.stateofartandroid

import android.app.Application
import com.mukesh.assignment_android.di.PrefModule
import com.mukesh.assignment_android.utils.SharedPreferenceHelper

class PrefModuleTest(val mockPrefS: SharedPreferenceHelper): PrefModule() {

    override fun provideSharedPrefHelper(application: Application): SharedPreferenceHelper {
        return mockPrefS
    }
}