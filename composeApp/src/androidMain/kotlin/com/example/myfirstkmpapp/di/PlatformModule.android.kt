package com.example.myfirstkmpapp.di

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import com.example.myfirstkmpapp.NotesDatabase
import com.example.myfirstkmpapp.data.local.DatabaseDriverFactory
import com.example.myfirstkmpapp.data.local.PreferenceManager
import com.example.myfirstkmpapp.platform.DeviceInfo
import com.example.myfirstkmpapp.platform.NetworkMonitor
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import java.io.File

actual fun platformModule() = module {
    single { DatabaseDriverFactory(androidContext()) }
    single { NotesDatabase(get<DatabaseDriverFactory>().createDriver()) }

    single<DataStore<Preferences>> {
        PreferenceDataStoreFactory.create(
            produceFile = { File(androidContext().filesDir, "settings.preferences_pb") }
        )
    }

    single { PreferenceManager(get()) }
    single { DeviceInfo() }
    single { NetworkMonitor(androidContext()) }
}