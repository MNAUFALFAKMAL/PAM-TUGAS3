package com.example.myfirstkmpapp.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.myfirstkmpapp.navigation.Screen

@Composable
fun DrawerContent(onMenuClick: (String) -> Unit) {
    ModalDrawerSheet {
        Text("Menu Aplikasi", modifier = Modifier.padding(16.dp), fontWeight = FontWeight.Bold)
        HorizontalDivider()
        NavigationDrawerItem(
            label = { Text("Daftar Catatan") },
            selected = false,
            onClick = { onMenuClick(Screen.Notes.route) },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
        NavigationDrawerItem(
            label = { Text("Profil Pengguna") },
            selected = false,
            onClick = { onMenuClick(Screen.Profile.route) },
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
        )
    }
}