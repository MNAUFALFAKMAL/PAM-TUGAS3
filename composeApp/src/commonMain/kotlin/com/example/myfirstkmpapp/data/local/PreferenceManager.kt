package com.example.myfirstkmpapp.data.local

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PreferenceManager(private val dataStore: DataStore<Preferences>) {
    private val THEME_KEY = booleanPreferencesKey("is_dark_mode")
    val isDarkMode: Flow<Boolean> = dataStore.data.map { it[THEME_KEY] ?: false }
    suspend fun toggleTheme() {
        dataStore.edit { it[THEME_KEY] = !(it[THEME_KEY] ?: false) }
    }
}