package com.example.myfirstkmpapp.di

import com.example.myfirstkmpapp.data.NoteRepository
import com.example.myfirstkmpapp.data.SettingsRepository
import com.example.myfirstkmpapp.viewmodel.NotesViewModel
import org.koin.core.context.startKoin
import org.koin.core.module.Module
import org.koin.dsl.module

val appModule = module {
    single { NoteRepository(get()) }
    single { SettingsRepository(get()) }
    factory { NotesViewModel(get(), get()) }
}

expect fun platformModule(): Module

fun initKoin(config: org.koin.core.KoinApplication.() -> Unit = {}) {
    startKoin {
        config()
        modules(appModule, platformModule())
    }
}