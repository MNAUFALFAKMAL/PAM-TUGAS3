package com.example.myfirstkmpapp.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import com.example.myfirstkmpapp.NotesDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.IO

class NoteRepository(database: NotesDatabase) {
    private val queries = database.notesDatabaseQueries

    fun getNotes() = queries.getAllNotes().asFlow().mapToList(Dispatchers.IO)

    fun searchNotes(query: String) = queries.searchNotes(query).asFlow().mapToList(Dispatchers.IO)

    suspend fun insert(title: String, content: String) {
        queries.insertNote(null, title, content, System.currentTimeMillis())
    }

    suspend fun update(id: Long, title: String, content: String) {
        queries.updateNote(title, content, id)
    }
    suspend fun delete(id: Long) {
        queries.deleteNote(id)
    }
}