package demo.vicwang.mvvm.modul

import demo.vicwang.mvvm.mvvm.model.ApiRepository
import demo.vicwang.mvvm.mvvm.rxprovider.AppSchedulerProvider
import demo.vicwang.mvvm.mvvm.rxprovider.SchedulerProvider
import demo.vicwang.mvvm.mvvm.viewmodel.DataLoadViewModel
import org.koin.android.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ApplicationModule = module(override = true)
{

    single { ApiRepository() }

    single<SchedulerProvider> { AppSchedulerProvider() }

    viewModel { DataLoadViewModel(get(), get()) }

}
