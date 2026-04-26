package com.example.myfirstkmpapp.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Info
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myfirstkmpapp.NoteEntity
import com.example.myfirstkmpapp.platform.DeviceInfo
import com.example.myfirstkmpapp.viewmodel.NotesViewModel
import org.koin.compose.koinInject

@Composable
fun NotesListScreen(
    viewModel: NotesViewModel,
    onEditClick: (NoteEntity) -> Unit
) {
    val notes by viewModel.notes.collectAsState()

    if (notes.isEmpty()) {
        Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Tidak ada catatan", style = MaterialTheme.typography.h6)
        }
    } else {
        LazyColumn(contentPadding = PaddingValues(16.dp)) {
            items(notes) { note ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp).clickable { onEditClick(note) },
                    elevation = 4.dp,
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(note.title, fontWeight = FontWeight.Bold, style = MaterialTheme.typography.subtitle1)
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(note.content, style = MaterialTheme.typography.body2, maxLines = 2)
                        }
                        IconButton(onClick = { onEditClick(note) }) {
                            Icon(Icons.Default.Edit, contentDescription = "Edit", tint = MaterialTheme.colors.primary)
                        }
                        IconButton(onClick = { viewModel.deleteNote(note.id) }) {
                            Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun NoteDialog(
    initialTitle: String = "",
    initialContent: String = "",
    onDismiss: () -> Unit,
    onSave: (String, String) -> Unit
) {
    var title by remember { mutableStateOf(initialTitle) }
    var content by remember { mutableStateOf(initialContent) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(if (initialTitle.isEmpty()) "Tambah Catatan" else "Edit Catatan") },
        text = {
            Column {
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Judul") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedTextField(
                    value = content,
                    onValueChange = { content = it },
                    label = { Text("Isi Catatan") },
                    modifier = Modifier.fillMaxWidth().height(120.dp),
                    maxLines = 5
                )
            }
        },
        confirmButton = {
            Button(onClick = { onSave(title, content) }) { Text("Simpan") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Batal") }
        }
    )
}

@Composable
fun SettingsScreen(
    deviceInfo: DeviceInfo = koinInject()
) {
    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        Text("Pengaturan", style = MaterialTheme.typography.h5, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(24.dp))

        Card(
            elevation = 4.dp,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = "Device Info", tint = MaterialTheme.colors.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Informasi Perangkat", style = MaterialTheme.typography.subtitle1, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(16.dp))
                Text("Model: ${deviceInfo.deviceModel}")
                Text("OS: ${deviceInfo.osName}")
                Text("Versi: ${deviceInfo.osVersion}")
                Spacer(modifier = Modifier.height(16.dp))
                Text(
                    text = deviceInfo.getSummary(),
                    style = MaterialTheme.typography.body2,
                    color = Color.Gray
                )
            }
        }
    }
}