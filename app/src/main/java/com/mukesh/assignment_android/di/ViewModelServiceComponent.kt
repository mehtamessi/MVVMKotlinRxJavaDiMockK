package com.mukesh.assignment_android.di

import com.mukesh.assignment_android.mvvm.viewmodel.ListViewModel
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [ApiModule::class,PrefModule::class,ApplicationModule::class])
interface ViewModelServiceComponent {
    fun injectInViewModel(serviceForViewModel: ListViewModel)
}