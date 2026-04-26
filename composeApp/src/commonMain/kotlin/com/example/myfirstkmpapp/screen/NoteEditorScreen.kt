package com.example.myfirstkmpapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myfirstkmpapp.viewmodel.NotesViewModel

@Composable
fun NoteEditorScreen(
    modifier: Modifier = Modifier,
    noteId: Long?,
    viewModel: NotesViewModel,
    onBack: () -> Unit
) {
    val selectedNote by viewModel.selectedNote.collectAsState()

    var title by remember(noteId) { mutableStateOf("") }
    var content by remember(noteId) { mutableStateOf("") }

    LaunchedEffect(noteId) {
        if (noteId != null) {
            viewModel.selectNote(noteId)
        } else {
            viewModel.clearSelectedNote()
        }
    }

    LaunchedEffect(selectedNote) {
        selectedNote?.let { note ->
            title = note.title
            content = note.content
        }
    }

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = if (noteId == null) "Add Note" else "Edit Note",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = title,
            onValueChange = { title = it },
            label = { Text("Title") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedTextField(
            value = content,
            onValueChange = { content = it },
            label = { Text("Content") },
            modifier = Modifier
                .fillMaxWidth()
                .height(180.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Button(
                onClick = {
                    if (title.isNotBlank() && content.isNotBlank()) {
                        if (noteId == null) {
                            viewModel.addNote(title, content)
                        } else {
                            viewModel.updateNote(noteId, title, content)
                        }

                        viewModel.clearSelectedNote()
                        onBack()
                    }
                }
            ) {
                Text("Save")
            }

            OutlinedButton(
                onClick = {
                    viewModel.clearSelectedNote()
                    onBack()
                }
            ) {
                Text("Back")
            }
        }
    }
}