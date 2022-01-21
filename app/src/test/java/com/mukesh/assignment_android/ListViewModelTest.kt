package com.mukesh.assignment_android

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.mukesh.assignment_android.mvvm.model.model_parse_data.Animal
import com.mukesh.assignment_android.mvvm.model.api.AnimalApiService
import com.mukesh.assignment_android.mvvm.model.model_parse_data.ApiKey
import com.mukesh.assignment_android.di.ApplicationModule
import com.mukesh.assignment_android.di.DaggerViewModelServiceComponent
import com.mukesh.assignment_android.mvvm.viewmodel.ListViewModel
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

    val application = Mockito.mock(Application::class.java)

    var listViewModel = ListViewModel(application,true)
    private val testKey = "test key"

    @Before
    fun setUp(){
        MockitoAnnotations.initMocks(this)
        DaggerViewModelServiceComponent.builder()
            .applicationModule(ApplicationModule(application))
            .apiModule(ApiModuleTest(animalApiService))
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
        val  animal = Animal("cow",null,null,null,null,null,null)
        val  mockAnimalList = listOf(animal)

        val testSingle = Single.just(mockAnimalList)
        Mockito.`when`(animalApiService.getAnimals(testKey)).thenReturn(testSingle)
        listViewModel.everyTimeReferesh()

        Assert.assertEquals(1,listViewModel.animals.value?.size)
        Assert.assertEquals(false,listViewModel.loadError.value)
        Assert.assertEquals(false,listViewModel.loading.value)
    }

    @Test
    fun getAnimalFailure(){
       val testSingle = Single.error<List<Animal>>(Throwable())
        val keySingle = Single.just(ApiKey("ok",testKey))
        Mockito.`when`(animalApiService.getAnimals(testKey)).thenReturn(testSingle)
        Mockito.`when`(animalApiService.getApiAKey()).thenReturn(keySingle)
        listViewModel.everyTimeReferesh()

        Assert.assertEquals(true,listViewModel.loadError.value)
        Assert.assertEquals(false,listViewModel.loading.value)
        Assert.assertEquals(null,listViewModel.animals.value?.size)
    }

    @Test
    fun getApiKeySuccess(){
        val keySingle = Single.just(ApiKey("ok",testKey))

        val  animal = Animal("cow",null,null,null,null,null,null)
        val  mockAnimalList = listOf(animal)

        val testSingle = Single.just(mockAnimalList)
        Mockito.`when`(animalApiService.getApiAKey()).thenReturn(keySingle)
        Mockito.`when`(animalApiService.getAnimals(testKey)).thenReturn(testSingle)
        listViewModel.everyTimeReferesh()
        Assert.assertEquals(1,listViewModel.animals.value?.size)
        Assert.assertEquals(false,listViewModel.loadError.value)
        Assert.assertEquals(false,listViewModel.loading.value)
    }

    @Test
    fun getApiKeyFailure(){
        val testSingle = Single.error<ApiKey>(Throwable())
        Mockito.`when`(animalApiService.getApiAKey()).thenReturn(testSingle)
        listViewModel.everyTimeReferesh()
        Assert.assertEquals(true,listViewModel.loadError.value)
        Assert.assertEquals(false,listViewModel.loading.value)
    }
}