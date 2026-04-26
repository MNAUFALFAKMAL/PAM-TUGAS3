package com.example.myfirstkmpapp.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.myfirstkmpapp.viewmodel.NotesViewModel

@Composable
fun SettingsScreen(
    modifier: Modifier = Modifier,
    viewModel: NotesViewModel
) {
    val theme by viewModel.theme.collectAsState()
    val sortOrder by viewModel.sortOrder.collectAsState()

    Column(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize()
    ) {
        Text(
            text = "Settings",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Theme",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = theme == "light",
                onClick = { viewModel.changeTheme("light") },
                label = { Text("Light") }
            )

            FilterChip(
                selected = theme == "dark",
                onClick = { viewModel.changeTheme("dark") },
                label = { Text("Dark") }
            )

            FilterChip(
                selected = theme == "system",
                onClick = { viewModel.changeTheme("system") },
                label = { Text("System") }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Sort Order",
            style = MaterialTheme.typography.titleMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(
                selected = sortOrder == "newest",
                onClick = { viewModel.changeSortOrder("newest") },
                label = { Text("Newest") }
            )

            FilterChip(
                selected = sortOrder == "oldest",
                onClick = { viewModel.changeSortOrder("oldest") },
                label = { Text("Oldest") }
            )
        }
    }
}