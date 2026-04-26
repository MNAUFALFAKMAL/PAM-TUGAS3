package com.example.myfirstkmpapp.platform

import kotlinx.coroutines.flow.StateFlow

expect class NetworkMonitor {
    val isConnected: StateFlow<Boolean>
}