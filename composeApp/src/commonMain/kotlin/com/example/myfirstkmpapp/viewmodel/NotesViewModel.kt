package com.example.myfirstkmpapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myfirstkmpapp.data.repository.NoteRepository
import com.example.myfirstkmpapp.db.Note
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class NotesViewModel(
    private val repository: NoteRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<NotesUiState>(NotesUiState.Loading)
    val uiState: StateFlow<NotesUiState> = _uiState.asStateFlow()

    private val _selectedNote = MutableStateFlow<Note?>(null)
    val selectedNote: StateFlow<Note?> = _selectedNote.asStateFlow()

    private val _query = MutableStateFlow("")
    val query: StateFlow<String> = _query.asStateFlow()

    private val _theme = MutableStateFlow(repository.getTheme())
    val theme: StateFlow<String> = _theme.asStateFlow()

    private val _sortOrder = MutableStateFlow(repository.getSortOrder())
    val sortOrder: StateFlow<String> = _sortOrder.asStateFlow()

    private var notesJob: Job? = null

    init {
        loadNotes()
    }

    fun loadNotes() {
        notesJob?.cancel()
        notesJob = viewModelScope.launch {
            repository.getNotes().collect { notes ->
                _uiState.value = if (notes.isEmpty()) {
                    NotesUiState.Empty
                } else {
                    NotesUiState.Content(notes)
                }
            }
        }
    }

    fun search(keyword: String) {
        _query.value = keyword
        notesJob?.cancel()

        notesJob = viewModelScope.launch {
            val source = if (keyword.isBlank()) {
                repository.getNotes()
            } else {
                repository.searchNotes(keyword)
            }

            source.collect { notes ->
                _uiState.value = if (notes.isEmpty()) {
                    NotesUiState.Empty
                } else {
                    NotesUiState.Content(notes)
                }
            }
        }
    }

    fun selectNote(id: Long) {
        viewModelScope.launch {
            _selectedNote.value = repository.getNoteById(id)
        }
    }

    fun clearSelectedNote() {
        _selectedNote.value = null
    }

    fun addNote(title: String, content: String) {
        viewModelScope.launch {
            repository.addNote(title, content)
        }
    }

    fun updateNote(id: Long, title: String, content: String) {
        viewModelScope.launch {
            repository.updateNote(id, title, content)
        }
    }

    fun deleteNote(id: Long) {
        viewModelScope.launch {
            repository.deleteNote(id)
        }
    }

    fun changeTheme(theme: String) {
        repository.setTheme(theme)
        _theme.value = theme
    }

    fun changeSortOrder(sortOrder: String) {
        repository.setSortOrder(sortOrder)
        _sortOrder.value = sortOrder

        if (_query.value.isBlank()) {
            loadNotes()
        } else {
            search(_query.value)
        }
    }
}