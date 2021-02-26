package com.example.composepegel

import com.example.composepegel.network.Client
import com.example.composepegel.network.ClientImpl
import com.example.composepegel.network.HTTPRepository
import com.example.composepegel.network.HTTPRepositoryImpl
import com.example.composepegel.ui.waters.WatersViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

object MainKoinModule {
    val appModule = module {
        single<Client> { ClientImpl() }
        single<HTTPRepository> { HTTPRepositoryImpl(get()) }
    }
}