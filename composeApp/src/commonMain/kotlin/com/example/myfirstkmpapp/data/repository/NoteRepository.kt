package com.example.myfirstkmpapp.data.repository

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.myfirstkmpapp.data.settings.SettingsManager
import com.example.myfirstkmpapp.db.Note
import com.example.myfirstkmpapp.db.NotesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import kotlinx.datetime.Clock

class NoteRepository(
    private val database: NotesDatabase,
    private val settingsManager: SettingsManager
) {
    private val queries = database.noteQueries

    fun getNotes(): Flow<List<Note>> {
        return if (settingsManager.sortOrder == "oldest") {
            queries.selectAllOldest().asFlow().mapToList(Dispatchers.IO)
        } else {
            queries.selectAllNewest().asFlow().mapToList(Dispatchers.IO)
        }
    }

    fun searchNotes(keyword: String): Flow<List<Note>> {
        val searchQuery = "%$keyword%"
        return if (settingsManager.sortOrder == "oldest") {
            queries.searchOldest(searchQuery, searchQuery).asFlow().mapToList(Dispatchers.IO)
        } else {
            queries.searchNewest(searchQuery, searchQuery).asFlow().mapToList(Dispatchers.IO)
        }
    }

    suspend fun getNoteById(id: Long): Note? {
        return withContext(Dispatchers.IO) {
            queries.selectById(id).executeAsOneOrNull()
        }
    }

    suspend fun addNote(title: String, content: String) {
        val now = Clock.System.now().toEpochMilliseconds()
        withContext(Dispatchers.IO) {
            queries.insert(title, content, now, now)
        }
    }

    suspend fun updateNote(id: Long, title: String, content: String) {
        val now = Clock.System.now().toEpochMilliseconds()
        withContext(Dispatchers.IO) {
            queries.update(title, content, now, id)
        }
    }

    suspend fun deleteNote(id: Long) {
        withContext(Dispatchers.IO) {
            queries.deleteById(id)
        }
    }

    fun getTheme(): String = settingsManager.theme

    fun setTheme(theme: String) {
        settingsManager.theme = theme
    }

    fun getSortOrder(): String = settingsManager.sortOrder

    fun setSortOrder(sortOrder: String) {
        settingsManager.sortOrder = sortOrder
    }
}