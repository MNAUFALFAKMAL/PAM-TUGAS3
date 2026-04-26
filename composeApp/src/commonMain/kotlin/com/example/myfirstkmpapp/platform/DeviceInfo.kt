package com.example.myfirstkmpapp.platform

expect class DeviceInfo() {
    val osName: String
    val osVersion: String
    val deviceModel: String
    fun getSummary(): String
}