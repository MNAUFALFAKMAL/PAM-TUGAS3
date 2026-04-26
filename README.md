# 📝 MyFirstKMPApp - Tugas Praktikum Minggu 8

Aplikasi Notes berbasis **Kotlin Multiplatform (KMP)** yang dikembangkan dengan fitur **Platform-Specific** dan **Dependency Injection**. Project ini merupakan pengembangan dari tugas Week-07.

---

## 🏗️ Arsitektur Aplikasi

Aplikasi ini menggunakan pola **MVVM** dengan manajemen dependensi menggunakan **Koin DI**. Struktur aplikasi memisahkan logika antara `commonMain` untuk kode bersama dan `androidMain` untuk implementasi spesifik platform menggunakan pattern `expect/actual`.

---

## 🛠️ Detail Implementasi (Rubrik Penilaian)

### 1. Setup Koin Dependency Injection
Seluruh dependensi di-inject melalui Koin secara terpusat untuk mempermudah manajemen komponen dan skalabilitas aplikasi.
* **Core Module:** Injeksi Repository dan ViewModel di `commonMain`.
* **Platform Module:** Injeksi `DatabaseDriverFactory`, `DeviceInfo`, dan `NetworkMonitor` di `androidMain`.
* **Inisialisasi:** Menggunakan `startKoin` yang dipanggil pada level platform (Android).

### 2. Pattern expect/actual
Implementasi fitur native menggunakan abstraksi `expect` di `commonMain` dan implementasi `actual` di platform Android:
* **`DeviceInfo`**: Mengakses `android.os.Build` untuk mendapatkan informasi model, manufaktur, dan versi OS perangkat secara native.
* **`NetworkMonitor`**: Menggunakan `ConnectivityManager` Android untuk memantau status koneksi internet secara real-time.

### 3. Integrasi UI & Fitur Platform
* **Network Status Indicator:** Implementasi banner peringatan (Offline Banner) yang otomatis muncul di layar utama jika perangkat kehilangan koneksi internet.
* **Settings Screen:** Halaman pengaturan yang menampilkan data teknis perangkat yang diambil melalui API native platform.
* **Dependency Injection UI:** Penggunaan `koinInject()` untuk mengambil instance ViewModel dan Network Monitor langsung di dalam Composable.

---

## 📸 Dokumentasi (Screenshots)

| Fitur | Screenshot |
| :--- | :--- |
| **Main Screen & Offline Indicator** | <img width="428" height="944" alt="Image" src="https://github.com/user-attachments/assets/63ab1713-3b32-49b2-8fd0-f3e5499f115b" /> |
| **Settings Screen (Device Info)** | <img width="428" height="944" alt="Image" src="https://github.com/user-attachments/assets/548c89de-bfef-4b63-a02d-380b611b0ec9" /> |

---

## 🎥 Video Demo
🎥 **Link Video:**https://drive.google.com/file/d/1Uw3Yg_OwkQyfPEFk5EhJ8CsemQOshBva/view?usp=sharing

**Poin yang ditunjukkan dalam video:**
1. Bukti implementasi Koin DI pada kode sumber.
2. Transisi Status Jaringan (On/Off WiFi) dan munculnya indikator di UI secara real-time.
3. Tampilan spesifikasi perangkat pada halaman Settings.

---