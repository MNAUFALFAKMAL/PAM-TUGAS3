package com.example.myfirstkmpapp.viewmodel

import androidx.lifecycle.ViewModel
import com.example.myfirstkmpapp.data.Note
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class NotesViewModel : ViewModel() {
    // List catatan awal
    private val _notes = MutableStateFlow(
        listOf(
            Note(1, "Impian", "Kaya raya dan bahagia", true),
            Note(2, "Daftar Belanja", "Eskrim, cemilan, dan minuman.", false)
        )
    )
    val notes: StateFlow<List<Note>> = _notes.asStateFlow()

    // Fungsi Tambah Catatan
    fun addNote(title: String, content: String) {
        _notes.update { currentList ->
            val newId = if (currentList.isEmpty()) 1 else currentList.maxOf { it.id } + 1
            currentList + Note(id = newId, title = title, content = content)
        }
    }

    // Fungsi Edit Catatan
    fun updateNote(id: Int, newTitle: String, newContent: String) {
        _notes.update { currentList ->
            currentList.map { if (it.id == id) it.copy(title = newTitle, content = newContent) else it }
        }
    }

    // Fungsi Hapus Catatan
    fun deleteNote(id: Int) {
        _notes.update { currentList -> currentList.filter { it.id != id } }
    }

    // Fungsi Toggle Favorite
    fun toggleFavorite(id: Int) {
        _notes.update { currentList ->
            currentList.map { if (it.id == id) it.copy(isFavorite = !it.isFavorite) else it }
        }
    }

    // Fungsi Ambil 1 Catatan berdasarkan ID
    fun getNoteById(id: Int): Note? {
        return _notes.value.find { it.id == id }
    }
}