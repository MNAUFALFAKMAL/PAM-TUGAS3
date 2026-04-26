package com.example.myfirstkmpapp

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myfirstkmpapp.platform.NetworkMonitor
import com.example.myfirstkmpapp.screens.NoteDialog
import com.example.myfirstkmpapp.screens.NotesListScreen
import com.example.myfirstkmpapp.screens.SettingsScreen
import com.example.myfirstkmpapp.viewmodel.NotesViewModel
import org.koin.compose.koinInject

val LightColors = lightColors(
    primary = Color(0xFFE65100),
    primaryVariant = Color(0xFFFFCC80),
    secondary = Color(0xFFE65100),
    background = Color(0xFFFAFAFA),
    surface = Color.White,
    onPrimary = Color.White,
    onBackground = Color(0xFF212121),
    onSurface = Color(0xFF212121)
)

val DarkColors = darkColors(
    primary = Color(0xFFFF9800),
    primaryVariant = Color(0xFFE65100),
    secondary = Color(0xFFFF9800),
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onPrimary = Color(0xFF121212),
    onBackground = Color(0xFFE0E0E0),
    onSurface = Color(0xFFE0E0E0)
)

@Composable
fun App() {
    val viewModel = koinInject<NotesViewModel>()
    val networkMonitor = koinInject<NetworkMonitor>()

    val isConnected by networkMonitor.isConnected.collectAsState()
    val isDark by viewModel.isDarkMode.collectAsState()
    val isSortByNewest by viewModel.isSortByNewest.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()

    var showDialog by remember { mutableStateOf(false) }
    var expandedSortMenu by remember { mutableStateOf(false) }
    var editingNote by remember { mutableStateOf<NoteEntity?>(null) }
    var currentScreen by remember { mutableStateOf("home") }

    val colorScheme = if (isDark) DarkColors else LightColors
    val animatedBackground by animateColorAsState(
        targetValue = colorScheme.background,
        animationSpec = tween(durationMillis = 500)
    )

    MaterialTheme(colors = colorScheme) {
        Surface(
            modifier = Modifier.fillMaxSize().background(animatedBackground),
            color = Color.Transparent
        ) {
            Scaffold(
                backgroundColor = animatedBackground,
                topBar = {
                    TopAppBar(
                        backgroundColor = MaterialTheme.colors.primary,
                        title = {
                            if (currentScreen == "home") {
                                TextField(
                                    value = searchQuery,
                                    onValueChange = { viewModel.onSearchQueryChange(it) },
                                    placeholder = {
                                        Text(
                                            "Cari catatan...",
                                            color = MaterialTheme.colors.onPrimary.copy(alpha = 0.7f)
                                        )
                                    },
                                    leadingIcon = {
                                        Icon(Icons.Default.Search, null, tint = MaterialTheme.colors.onPrimary)
                                    },
                                    colors = TextFieldDefaults.textFieldColors(
                                        textColor = MaterialTheme.colors.onPrimary,
                                        backgroundColor = Color.Transparent,
                                        focusedIndicatorColor = Color.Transparent,
                                        unfocusedIndicatorColor = Color.Transparent,
                                        cursorColor = MaterialTheme.colors.onPrimary
                                    ),
                                    singleLine = true
                                )
                            } else {
                                Text("Pengaturan", color = MaterialTheme.colors.onPrimary)
                            }
                        },
                        actions = {
                            if (currentScreen == "home") {
                                Box {
                                    IconButton(onClick = { expandedSortMenu = true }) {
                                        Icon(
                                            Icons.Default.Sort,
                                            contentDescription = "Filter",
                                            tint = MaterialTheme.colors.onPrimary
                                        )
                                    }
                                    DropdownMenu(
                                        expanded = expandedSortMenu,
                                        onDismissRequest = { expandedSortMenu = false }
                                    ) {
                                        DropdownMenuItem(onClick = {
                                            viewModel.setSortOrder(true)
                                            expandedSortMenu = false
                                        }) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text("Terbaru")
                                                if (isSortByNewest) Icon(Icons.Default.Check, null, modifier = Modifier.padding(start = 8.dp))
                                            }
                                        }
                                        DropdownMenuItem(onClick = {
                                            viewModel.setSortOrder(false)
                                            expandedSortMenu = false
                                        }) {
                                            Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text("Terlama")
                                                if (!isSortByNewest) Icon(Icons.Default.Check, null, modifier = Modifier.padding(start = 8.dp))
                                            }
                                        }
                                    }
                                }

                                // Fitur Toggle Tema
                                IconButton(onClick = { viewModel.toggleTheme() }) {
                                    Icon(
                                        if (isDark) Icons.Default.LightMode else Icons.Default.DarkMode,
                                        contentDescription = "Toggle Theme",
                                        tint = MaterialTheme.colors.onPrimary
                                    )
                                }
                            }

                            IconButton(onClick = {
                                currentScreen = if (currentScreen == "home") "settings" else "home"
                            }) {
                                Icon(
                                    if (currentScreen == "home") Icons.Default.Settings else Icons.Default.Home,
                                    contentDescription = "Toggle Screen",
                                    tint = MaterialTheme.colors.onPrimary
                                )
                            }
                        }
                    )
                },
                floatingActionButton = {
                    if (currentScreen == "home") {
                        FloatingActionButton(
                            onClick = {
                                editingNote = null
                                showDialog = true
                            },
                            backgroundColor = MaterialTheme.colors.primary
                        ) {
                            Icon(
                                Icons.Default.Add,
                                contentDescription = "Tambah Catatan",
                                tint = MaterialTheme.colors.onPrimary
                            )
                        }
                    }
                }
            ) { paddingValues ->
                Column(modifier = Modifier.padding(paddingValues).fillMaxSize()) {

                    if (!isConnected) {
                        Row(
                            modifier = Modifier.fillMaxWidth().background(Color.Red).padding(8.dp),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(Icons.Default.Warning, contentDescription = "Offline", tint = Color.White)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Anda sedang Offline", color = Color.White, fontWeight = FontWeight.Bold)
                        }
                    }

                    if (currentScreen == "home") {
                        NotesListScreen(
                            viewModel = viewModel,
                            onEditClick = { note ->
                                editingNote = note
                                showDialog = true
                            }
                        )
                    } else {
                        SettingsScreen()
                    }

                    if (showDialog) {
                        NoteDialog(
                            initialTitle = editingNote?.title ?: "",
                            initialContent = editingNote?.content ?: "",
                            onDismiss = { showDialog = false },
                            onSave = { title, content ->
                                if (title.isNotBlank()) {
                                    viewModel.addOrUpdateNote(editingNote?.id, title, content)
                                    showDialog = false
                                }
                            }
                        )
                    }
                }
            }
        }
    }
}