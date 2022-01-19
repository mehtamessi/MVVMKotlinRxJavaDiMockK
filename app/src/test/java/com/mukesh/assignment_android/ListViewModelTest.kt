package com.mukesh.assignment_android

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mukesh.assignment_android.data.model.Animal
import com.mukesh.assignment_android.data.api.AnimalApiService
import com.mukesh.assignment_android.data.model.ApiKey
import com.mukesh.assignment_android.di.ApplicationModule
import com.mukesh.assignment_android.di.DaggerViewModelServiceComponent
import com.mukesh.assignment_android.ui.main.viewmodel.ListViewModel
import com.mukesh.assignment_android.utils.SharedPreferenceHelper
import io.reactivex.Scheduler
import io.reactivex.Single
import io.reactivex.android.plugins.RxAndroidPlugins
import io.reactivex.internal.schedulers.ExecutorScheduler
import io.reactivex.plugins.RxJavaPlugins
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.MockitoAnnotations
import java.util.concurrent.Callable
import java.util.concurrent.Executor

class ListViewModelTest {

    @get:Rule
    var rule = InstantTaskExecutorRule()

    @Mock
    lateinit var animalApiService: AnimalApiService

    @Mock
    lateinit var sPrefs: SharedPreferenceHelper

    val application = Mockito.mock(Application::class.java)

    var listViewModel = ListViewModel(application,true)
    private val testKey = "test ke"

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        DaggerViewModelServiceComponent.builder()
            .applicationModule(ApplicationModule(application))
            .apiModule(ApiModuleTest(animalApiService))
            .prefModule(PrefModuleTest(sPrefs))
            .build()
            .injectInViewModel(listViewModel)
    }
    @Before
    fun setUpRxSchedulers() {
        val immediate = object : Scheduler() {
            override fun createWorker(): Worker {
                return ExecutorScheduler.ExecutorWorker(Executor { it.run() }, true)
            }
        }

        RxJavaPlugins.setInitNewThreadSchedulerHandler { scheduler: Callable<Scheduler> -> immediate }
        RxAndroidPlugins.setInitMainThreadSchedulerHandler { scheduler: Callable<Scheduler> -> immediate }
    }

    @Test
    fun getAnimalSuccess(){
        Mockito.`when`(sPrefs.getStoredApiKey()).thenReturn(testKey)
        val  animal = Animal("cow",null,null,null,null,null,null)
        val  mockAnimalList = listOf(animal)

        val testSingle = Single.just(mockAnimalList)
        Mockito.`when`(animalApiService.getAnimals(testKey)).thenReturn(testSingle)
        listViewModel.refresh()

        Assert.assertEquals(1,listViewModel.animals.value?.size)
        Assert.assertEquals(false,listViewModel.loadError.value)
        Assert.assertEquals(false,listViewModel.loading.value)
    }

    @Test
    fun getAnimalFailure(){
        Mockito.`when`(sPrefs.getStoredApiKey()).thenReturn(testKey)

        val testSingle = Single.error<List<Animal>>(Throwable())
        val keySingle = Single.just(ApiKey("ok",testKey))
        Mockito.`when`(animalApiService.getAnimals(testKey)).thenReturn(testSingle)
        Mockito.`when`(animalApiService.getApiAKey()).thenReturn(keySingle)
        listViewModel.refresh()

        Assert.assertEquals(true,listViewModel.loadError.value)
        Assert.assertEquals(false,listViewModel.loading.value)
        Assert.assertEquals(null,listViewModel.animals.value?.size)
    }

    @Test
    fun getApiKeySuccess(){
        Mockito.`when`(sPrefs.getStoredApiKey()).thenReturn(null)
        val keySingle = Single.just(ApiKey("ok",testKey))

        val  animal = Animal("cow",null,null,null,null,null,null)
        val  mockAnimalList = listOf(animal)

        val testSingle = Single.just(mockAnimalList)
        Mockito.`when`(animalApiService.getApiAKey()).thenReturn(keySingle)
        Mockito.`when`(animalApiService.getAnimals(testKey)).thenReturn(testSingle)
        listViewModel.refresh()
        Assert.assertEquals(1,listViewModel.animals.value?.size)
        Assert.assertEquals(false,listViewModel.loadError.value)
        Assert.assertEquals(false,listViewModel.loading.value)
    }

    @Test
    fun getApiKeyFailure(){
        Mockito.`when`(sPrefs.getStoredApiKey()).thenReturn(null)

        val testSingle = Single.error<ApiKey>(Throwable())

//        val keySingle = Single.just(ApiKey("ok",null))
//        Mockito.`when`(animalApiService.getApiAKey()).thenReturn(keySingle)
        Mockito.`when`(animalApiService.getApiAKey()).thenReturn(testSingle)
        listViewModel.refresh()
        Assert.assertEquals(true,listViewModel.loadError.value)
        Assert.assertEquals(false,listViewModel.loading.value)
    }
}