package com.example.myfirstkmpapp

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myfirstkmpapp.navigation.AppNavigation
import com.example.myfirstkmpapp.viewmodel.NotesViewModel
import com.example.myfirstkmpapp.viewmodel.ProfileViewModel

val LightColors = lightColorScheme(
    primary = Color(0xFFE65100),
    primaryContainer = Color(0xFFFFCC80),
    onPrimaryContainer = Color(0xFF4E342E),
    background = Color(0xFFFAFAFA),
    surface = Color.White,
    onSurface = Color(0xFF212121),
    surfaceVariant = Color(0xFFEEEEEE)
)

val DarkColors = darkColorScheme(
    primary = Color(0xFFFF9800),
    primaryContainer = Color(0xFFE65100),
    onPrimaryContainer = Color.White,
    background = Color(0xFF121212),
    surface = Color(0xFF1E1E1E),
    onSurface = Color(0xFFE0E0E0),
    surfaceVariant = Color(0xFF2C2C2C)
)

@Composable
fun App(
    profileViewModel: ProfileViewModel = viewModel { ProfileViewModel() },
    notesViewModel: NotesViewModel = viewModel { NotesViewModel() }
) {
    val uiState by profileViewModel.uiState.collectAsState()
    val colorScheme = if (uiState.isDarkMode) DarkColors else LightColors

    val animatedBackground by animateColorAsState(
        targetValue = colorScheme.background,
        animationSpec = tween(durationMillis = 500)
    )
    MaterialTheme(colorScheme = colorScheme) {
        StatusBarColors(
            isDarkMode = uiState.isDarkMode,
            color = colorScheme.primaryContainer
        )
        Box(modifier = Modifier.fillMaxSize().background(animatedBackground)) {
            AppNavigation(profileViewModel, notesViewModel)
        }
    }
}

@Composable
expect fun StatusBarColors(isDarkMode: Boolean, color: Color)