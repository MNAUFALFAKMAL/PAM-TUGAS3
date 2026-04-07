package com.example.myfirstkmpapp.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.myfirstkmpapp.components.BottomNavigationBar
import com.example.myfirstkmpapp.components.DrawerContent
import com.example.myfirstkmpapp.screens.*
import com.example.myfirstkmpapp.ui.ProfileScreen
import com.example.myfirstkmpapp.viewmodel.NotesViewModel
import com.example.myfirstkmpapp.viewmodel.ProfileViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppNavigation(
    profileViewModel: ProfileViewModel = viewModel { ProfileViewModel() },
    notesViewModel: NotesViewModel = viewModel { NotesViewModel() }
) {
    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Kontrol Visibilitas BottomBar dan TopBar
    val showBottomAndFab = currentRoute in listOf(Screen.Notes.route, Screen.Favorites.route, Screen.Profile.route)

    // Sembunyikan TopAppBar AppNavigation saat di layar Profile (karena ProfileScreen Tugas 4 sudah punya TopAppBar sendiri)
    val showTopBar = currentRoute != Screen.Profile.route

    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = {
            DrawerContent(onMenuClick = { route ->
                scope.launch { drawerState.close() }
                navController.navigate(route) {
                    popUpTo(Screen.Notes.route)
                    launchSingleTop = true
                }
            })
        }
    ) {
        Scaffold(
            topBar = {
                if (showTopBar) {
                    TopAppBar(
                        title = { Text("My Notes App") },
                        colors = TopAppBarDefaults.topAppBarColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                    )
                }
            },
            bottomBar = {
                if (showBottomAndFab) {
                    BottomNavigationBar(navController = navController, currentRoute = currentRoute)
                }
            },
            floatingActionButton = {
                if (currentRoute == Screen.Notes.route) {
                    FloatingActionButton(
                        onClick = { navController.navigate(Screen.AddNote.route) },
                        containerColor = MaterialTheme.colorScheme.primary
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Add Note")
                    }
                }
            }
        ) { paddingValues ->
            NavHost(
                navController = navController,
                startDestination = Screen.Notes.route,
                modifier = Modifier.padding(paddingValues)
            ) {
                // TABS
                composable(Screen.Notes.route) {
                    NoteListScreen(
                        notesViewModel = notesViewModel,
                        onNoteClick = { noteId -> navController.navigate(Screen.NoteDetail.createRoute(noteId)) }
                    )
                }
                composable(Screen.Favorites.route) {
                    FavoritesScreen(
                        notesViewModel = notesViewModel,
                        onNoteClick = { noteId -> navController.navigate(Screen.NoteDetail.createRoute(noteId)) }
                    )
                }

                // INTEGRASI TUGAS 4 (Profile)
                composable(Screen.Profile.route) {
                    val uiState by profileViewModel.uiState.collectAsState()
                    ProfileScreen(
                        uiState = uiState,
                        onNameChange = { profileViewModel.updateName(it) },
                        onBioChange = { profileViewModel.updateBio(it) },
                        onEmailChange = { profileViewModel.updateEmail(it) },
                        onPhoneChange = { profileViewModel.updatePhone(it) },
                        onLocationChange = { profileViewModel.updateLocation(it) },
                        onToggleBio = { profileViewModel.toggleBioVisibility() },
                        onToggleEdit = { profileViewModel.toggleEditMode() },
                        onToggleDarkMode = { profileViewModel.toggleDarkMode() }
                    )
                }

                // SUB-SCREENS NOTES
                composable(Screen.AddNote.route) {
                    AddNoteScreen(notesViewModel = notesViewModel, onBack = { navController.popBackStack() })
                }
                composable(route = Screen.NoteDetail.route, arguments = listOf(navArgument("noteId") { type = NavType.IntType })) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
                    NoteDetailScreen(
                        noteId = noteId,
                        notesViewModel = notesViewModel,
                        onBack = { navController.popBackStack() },
                        onEditClick = { id -> navController.navigate(Screen.EditNote.createRoute(id)) }
                    )
                }
                composable(route = Screen.EditNote.route, arguments = listOf(navArgument("noteId") { type = NavType.IntType })) { backStackEntry ->
                    val noteId = backStackEntry.arguments?.getInt("noteId") ?: 0
                    EditNoteScreen(noteId = noteId, notesViewModel = notesViewModel, onBack = { navController.popBackStack() })
                }
            }
        }
    }
}