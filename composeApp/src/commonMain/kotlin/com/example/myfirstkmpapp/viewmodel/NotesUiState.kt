package com.example.myfirstkmpapp.viewmodel

import com.example.myfirstkmpapp.db.Note

sealed interface NotesUiState {
    data object Loading : NotesUiState
    data object Empty : NotesUiState
    data class Content(val notes: List<Note>) : NotesUiState
}