package com.example.myfirstkmpapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.myfirstkmpapp.viewmodel.NotesViewModel

// NOTE LIST
@Composable
fun NoteListScreen(notesViewModel: NotesViewModel, onNoteClick: (Int) -> Unit) {
    val notes by notesViewModel.notes.collectAsState()

    if (notes.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { Text("Belum ada catatan.") }
    } else {
        LazyColumn(modifier = Modifier.fillMaxSize().padding(8.dp)) {
            items(notes) { note ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(8.dp).clickable { onNoteClick(note.id) },
                    elevation = CardDefaults.cardElevation(2.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(note.title, fontWeight = FontWeight.Bold, maxLines = 1, overflow = TextOverflow.Ellipsis)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(note.content, style = MaterialTheme.typography.bodyMedium, maxLines = 2, overflow = TextOverflow.Ellipsis)
                        }
                        IconButton(onClick = { notesViewModel.toggleFavorite(note.id) }) {
                            Icon(
                                imageVector = if (note.isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite",
                                tint = if (note.isFavorite) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}

// NOTE DETAIL
@Composable
fun NoteDetailScreen(noteId: Int, notesViewModel: NotesViewModel, onBack: () -> Unit, onEditClick: (Int) -> Unit) {
    val note = notesViewModel.getNoteById(noteId)

    if (note == null) {
        onBack() // Jika catatan sudah dihapus, otomatis kembali
        return
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text(note.title, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(16.dp))
        Text(note.content, style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.weight(1f))

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
            OutlinedButton(onClick = onBack) { Text("Kembali") }
            Button(onClick = { onEditClick(noteId) }) { Icon(Icons.Default.Edit, "Edit"); Spacer(Modifier.width(4.dp)); Text("Edit") }
            Button(
                onClick = { notesViewModel.deleteNote(noteId); onBack() },
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) { Icon(Icons.Default.Delete, "Delete"); Spacer(Modifier.width(4.dp)); Text("Hapus") }
        }
    }
}

// ADD NOTE
@Composable
fun AddNoteScreen(notesViewModel: NotesViewModel, onBack: () -> Unit) {
    var title by remember { mutableStateOf("") }
    var content by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Judul Catatan") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Isi Catatan") }, modifier = Modifier.fillMaxWidth().weight(1f))
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (title.isNotBlank()) {
                    notesViewModel.addNote(title, content)
                    onBack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Simpan Catatan Baru") }
    }
}

// EDIT NOTE
@Composable
fun EditNoteScreen(noteId: Int, notesViewModel: NotesViewModel, onBack: () -> Unit) {
    val note = notesViewModel.getNoteById(noteId)
    var title by remember { mutableStateOf(note?.title ?: "") }
    var content by remember { mutableStateOf(note?.content ?: "") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        OutlinedTextField(value = title, onValueChange = { title = it }, label = { Text("Judul Catatan") }, modifier = Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(value = content, onValueChange = { content = it }, label = { Text("Isi Catatan") }, modifier = Modifier.fillMaxWidth().weight(1f))
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                if (title.isNotBlank()) {
                    notesViewModel.updateNote(noteId, title, content)
                    onBack()
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) { Text("Simpan Perubahan") }
    }
}