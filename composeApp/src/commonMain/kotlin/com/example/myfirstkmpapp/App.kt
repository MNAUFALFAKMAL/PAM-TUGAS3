package com.example.myfirstkmpapp

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myfirstkmpapp.data.local.DatabaseDriverFactory
import com.example.myfirstkmpapp.data.repository.NoteRepository
import com.example.myfirstkmpapp.data.settings.SettingsManager
import com.example.myfirstkmpapp.db.NotesDatabase
import com.example.myfirstkmpapp.screens.NoteEditorScreen
import com.example.myfirstkmpapp.screens.NotesScreen
import com.example.myfirstkmpapp.screens.SettingsScreen
import com.example.myfirstkmpapp.viewmodel.NotesViewModel
import com.russhwolf.settings.Settings
import kotlinx.coroutines.launch

// Deklarasi Warna Pink Sesuai Keinginanmu
val PinkPrimary = Color(0xFFD81B60)
val PinkSecondary = Color(0xFFF06292)

val LightColorScheme = lightColorScheme(
    primary = PinkPrimary,
    background = Color(0xFFF3F2EF)
)
val DarkColorScheme = darkColorScheme(
    primary = PinkSecondary,
    background = Color(0xFF121212)
)

@Composable
fun App(driverFactory: DatabaseDriverFactory) {
    // 1. Inisialisasi Database & Repository (Tetap dipertahankan)
    val database = remember { NotesDatabase(driverFactory.createDriver()) }
    val settingsManager = remember { SettingsManager(settings = Settings()) }
    val repository = remember { NoteRepository(database, settingsManager) }
    val viewModel = remember { NotesViewModel(repository) }

    // 2. State Tema & Navigasi
    val theme by viewModel.theme.collectAsState()
    val colorScheme = if (theme == "dark") DarkColorScheme else LightColorScheme
    val animatedBackground by animateColorAsState(targetValue = colorScheme.background, tween(500))

    val drawerState = rememberDrawerState(DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    var screen by remember { mutableStateOf("notes") }
    var editNoteId by remember { mutableStateOf<Long?>(null) }

    MaterialTheme(colorScheme = colorScheme) {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    Spacer(Modifier.height(16.dp))
                    Text(
                        "My Notes App",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp)
                    )
                    HorizontalDivider()
                    NavigationDrawerItem(
                        label = { Text("About App") },
                        selected = false,
                        onClick = { scope.launch { drawerState.close() } },
                        icon = { Icon(Icons.Default.Info, contentDescription = null) },
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        ) {
            // Box untuk background yang bisa animasi warna
            Box(modifier = Modifier.fillMaxSize().background(animatedBackground)) {
                Scaffold(
                    bottomBar = {
                        // Tampilkan BottomBar hanya jika tidak sedang di layar Editor
                        if (screen != "editor") {
                            NavigationBar {
                                NavigationBarItem(
                                    selected = screen == "notes",
                                    onClick = { screen = "notes" },
                                    label = { Text("Notes") },
                                    icon = { Text("📝") }
                                )
                                NavigationBarItem(
                                    selected = screen == "settings",
                                    onClick = { screen = "settings" },
                                    label = { Text("Settings") },
                                    icon = { Text("⚙") }
                                )
                            }
                        }
                    }
                ) { padding ->
                    when (screen) {
                        "notes" -> NotesScreen(
                            modifier = Modifier.padding(padding),
                            viewModel = viewModel,
                            onAddClick = {
                                editNoteId = null
                                screen = "editor"
                            },
                            onEditClick = { id ->
                                editNoteId = id
                                screen = "editor"
                            }
                            // Jika NotesScreen butuh tombol buka drawer, tambahkan parameter di sini
                        )

                        "editor" -> NoteEditorScreen(
                            modifier = Modifier.padding(padding),
                            noteId = editNoteId,
                            viewModel = viewModel,
                            onBack = {
                                viewModel.clearSelectedNote()
                                screen = "notes"
                            }
                        )

                        "settings" -> SettingsScreen(
                            modifier = Modifier.padding(padding),
                            viewModel = viewModel
                        )
                    }
                }
            }
        }
    }
}