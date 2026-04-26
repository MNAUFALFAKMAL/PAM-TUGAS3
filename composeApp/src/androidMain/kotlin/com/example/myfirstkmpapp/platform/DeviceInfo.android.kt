package com.example.myfirstkmpapp.platform

import android.os.Build

actual class DeviceInfo actual constructor() {
    actual val osName: String = "Android"
    actual val osVersion: String = Build.VERSION.RELEASE ?: "Unknown"
    actual val deviceModel: String = Build.MODEL ?: "Unknown"
    actual fun getSummary(): String {
        return "$deviceModel berjalan pada $osName versi $osVersion"
    }
}