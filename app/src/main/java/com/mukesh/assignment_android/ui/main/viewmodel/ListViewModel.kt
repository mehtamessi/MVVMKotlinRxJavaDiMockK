package com.mukesh.assignment_android.ui.main.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.mukesh.assignment_android.BuildConfig
import com.mukesh.assignment_android.data.model.Animal
import com.mukesh.assignment_android.data.api.AnimalApiService
import com.mukesh.assignment_android.data.model.ApiKey
import com.mukesh.assignment_android.di.ApplicationModule
import com.mukesh.assignment_android.di.CONTEXT_APP
import com.mukesh.assignment_android.di.DaggerViewModelServiceComponent
import com.mukesh.assignment_android.di.TypeOfContext
import com.mukesh.assignment_android.utils.SharedPreferenceHelper
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.observers.DisposableSingleObserver
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class ListViewModel(application: Application) : AndroidViewModel(application) {

    constructor(application: Application, test: Boolean) : this(application){
        injected = true
    }

    //lazy - only going to be create when object is need
    val animals by lazy { MutableLiveData<List<Animal>>() }

    val loadError by lazy { MutableLiveData<Boolean>() }
    val loading by lazy { MutableLiveData<Boolean>() }
    private val disposable = CompositeDisposable()
    @Inject
    lateinit var animalApiService: AnimalApiService

    @Inject
    @field:TypeOfContext(CONTEXT_APP)
    lateinit var pref: SharedPreferenceHelper

    private var iSInvalidKey = false
    private var injected: Boolean = false
    fun Inject() {
        if (!injected) {
            DaggerViewModelServiceComponent.builder()
                .applicationModule(ApplicationModule(getApplication()))
                .build().injectInViewModel(this)
        }
    }

    fun refresh() {
        Inject()
        iSInvalidKey = false
        val storeKey = pref.getStoredApiKey()
        if (storeKey.isNullOrEmpty()) {
            loading.value = true
            getApiKey()
        } else {
            loading.value = true
            getAnimals(storeKey)
        }
    }

    fun hardRefresh() {
        Inject()
        loading.value = true
        getApiKey()
    }

    private fun getApiKey() {
        disposable.add(
            animalApiService.getApiAKey()
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(object : DisposableSingleObserver<ApiKey>() {
                    override fun onSuccess(apikey: ApiKey) {
                        if (apikey.key.isNullOrEmpty()) {
                            loadError.value = true
                            loading.value = false
                        } else {
                            pref.saveAPIKey(apikey.key)

                            getAnimals(apikey.key)
                        }
                    }

                    override fun onError(e: Throwable) {
                        if (BuildConfig.DEBUG) {
                            e.printStackTrace()
                        }
                        loadError.value = true
                        loading.value = false
                    }

                })

        )

    }

    private fun getAnimals(key: String?) {
        key?.let {
            disposable.add(
                animalApiService.getAnimals(key)
                    .subscribeOn(Schedulers.newThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribeWith(object : DisposableSingleObserver<List<Animal>>() {
                        override fun onSuccess(animalList: List<Animal>) {
                                animals.value = animalList
                                loadError.value = false
                                loading.value = false
                        }

                        override fun onError(e: Throwable) {
                            if (BuildConfig.DEBUG) {
                                e.printStackTrace()
                            }
                            if (!iSInvalidKey) {
                                iSInvalidKey = true
                                loading.value = true
                                getApiKey()
                            } else {
                                loadError.value = true
                                loading.value = false
                                animals.value = null
                            }
                        }

                    })
            )
        }
    }

    override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }
}