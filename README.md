# Tugas 3 - Pengembangan Aplikasi Mobile - My Profile App
- **Nama:** Muhammad Naufal Fikri Akmal
- **NIM:** 123140132
- **Program Studi:** Teknik Informatika
- **Kelas:** Pengembangan Aplikasi Mobile RB Minggu 3

## Tampilan Aplikasi (Screenshot)
<img width="428" height="936" alt="image" src="https://github.com/user-attachments/assets/34179202-04e5-4c56-8482-e4774bd3e086" />

---

# Tugas 4 - Refactoring: MVVM Architecture & State Management

Pada rilis terbaru ini, aplikasi profil statis telah dirombak secara menyeluruh menggunakan pola arsitektur **MVVM (Model-View-ViewModel)**. Aplikasi kini bersifat dinamis, reaktif, dan memiliki struktur kode yang jauh lebih *maintainable*.

## Tampilan Fitur Terbaru

| Mode Terang (Default) | Form Edit (Real-time) | Mode Gelap (Smooth Animasi) |
| :---: | :---: | :---: |
| <img width="250" src="https://github.com/user-attachments/assets/7a466a0a-edf5-4c52-86c4-1e69671fd608" /> | <img width="250" src="https://github.com/user-attachments/assets/17c22e2a-2cfc-4b7c-ad21-9b1bb5f1de70" /> | <img width="250" src="https://github.com/user-attachments/assets/f232e568-f050-4758-a17f-cbb5da5aeb6c" /> |

---

## Fitur & Implementasi Teknis

Sebagai pengembang, saya menerapkan beberapa standar pengembangan modern pada aplikasi Compose Multiplatform ini:

### 1. Manajemen State Terpusat (UI State Pattern)
Seluruh data yang dapat berubah di layar tidak lagi dideklarasikan secara acak. Semua *state* dibungkus ke dalam sebuah `data class` tunggal bernama `ProfileUiState`. Hal ini mencakup teks (Nama, Bio, Email, Telepon, Lokasi) maupun status visibilitas komponen (sedang diedit, mode gelap, dll).

### 2. Arsitektur Berbasis ViewModel
Logika bisnis dan penyimpanan data UI telah dipisahkan dari tampilan menggunakan kelas `ProfileViewModel`. Pengelolaan *state* menggunakan **StateFlow** yang bersifat reaktif. Semua tindakan pengguna (*user intent*) diproses melalui fungsi-fungsi *event handler* secara aman menggunakan metode `.update { }`.

### 3. Komponen Reusable & State Hoisting
Aplikasi ini secara ketat menerapkan prinsip **State Hoisting**.
- Layar utama (`ProfileScreen`) dibangun sebagai komponen *Stateless*, yang hanya menerima aliran data dan melempar *callback* ke ViewModel.
- Untuk mencegah penulisan kode berulang pada form input, saya membuat komponen *reusable* bernama `StatelessEditField` yang menangani semua input teks secara seragam.

### 4. Edit Profil Secara Real-Time
Pengguna dapat menekan tombol **Edit Profile** untuk membuka form interaktif (`AnimatedVisibility`). Tidak hanya sekadar nama, pengguna bisa mengubah seluruh data kontak (Email, Telepon, Lokasi). Perubahan ini tervalidasi dan ter-update secara *real-time* tanpa ada jeda pemuatan ulang halaman.

### 5. Smooth Dark Mode Transition
Terdapat fitur *Switch* untuk mengganti tema aplikasi dari mode Terang ke mode Gelap. Untuk meningkatkan pengalaman pengguna (UX), perpindahan tema ini tidak terjadi secara kaku/berkedip, melainkan diiringi dengan **animasi transisi warna yang halus** (*smooth*) menggunakan implementasi `animateColorAsState` dengan durasi 500ms.

---

## Struktur Direktori
Kode telah dikelompokkan ke dalam struktur paket yang terpisah berdasarkan tanggung jawabnya (Separation of Concerns):

```text
com.example.myfirstkmpapp
│
├── data/
│   └── ProfileUiState.kt      # Model data untuk State UI
├── viewmodel/
│   └── ProfileViewModel.kt    # Logic bisnis dan pengelolaan StateFlow
├── ui/
│   └── ProfileScreen.kt       # Komponen visual (Stateless UI)
└── App.kt                     # Entry point & Pengaturan Tema Aplikasi
```

# Tugas 5 - Multi-Screen Navigation & Integrated Notes App

Pada iterasi terbaru ini, aplikasi telah berevolusi dari *single-screen* menjadi arsitektur **Multi-Screen** yang kompleks menggunakan **Compose Navigation**. Selain itu, fitur *Notes* (Catatan) telah dikembangkan menjadi fitur fungsional penuh (CRUD) yang terintegrasi secara mulus dengan *Profile App* dari Tugas 4.

## 📸 Bukti Layar (Screenshots)

Tampilan di bawah ini mendemonstrasikan hasil dari perpindahan antar layar (routing) beserta status pengiriman argumen data:

| Daftar Catatan (Notes) | Detail & Hapus Catatan | Form Tambah/Edit |
| :---: | :---: | :---: |
| <img width="430" height="943" alt="Image" src="https://github.com/user-attachments/assets/479bcd37-7c33-4f2b-8b4d-df341779058c" />
| <img width="430" height="943" alt="Image" src="https://github.com/user-attachments/assets/46dab231-dff0-4d01-9966-9d99d382ab2f" /> | <img width="430" height="943" alt="Image" src="https://github.com/user-attachments/assets/02d0473d-3304-478c-b04f-9b2d62f140dc" /> |

*(Catatan: Layar di atas saling terhubung menggunakan NavController dengan mem-passing parameter `noteId`)*

---

## 🚀 Implementasi Teknis & Arsitektur Navigasi

Aplikasi ini menerapkan standar industri untuk navigasi *Compose Multiplatform*, meliputi:

### 1. Sistem Navigasi Deklaratif (Navigation Component)
Pengelolaan perpindahan antar layar diatur secara terpusat menggunakan `NavHost` dan `rememberNavController`. Rute navigasi (*routes*) dibungkus menggunakan tipe `sealed class` untuk memastikan *type-safety* dan meminimalisir kesalahan penulisan (*typo*) saat rute dipanggil.

### 2. Nested Navigation (Bottom Bar & Drawer)
Aplikasi mengimplementasikan hierarki navigasi bersarang (*nested*):
- **Bottom Navigation:** Memiliki 3 tab utama, yaitu `Notes`, `Favorites`, dan `Profile` (mengintegrasikan profil dari iterasi sebelumnya). Pengaturan *state* dilakukan dengan hati-hati menggunakan `popUpTo` dan `launchSingleTop` untuk mencegah penumpukan *back-stack* memori saat berpindah tab.
- **🌟 Bonus Fitur (Navigation Drawer):** Sistem navigasi diperkuat dengan implementasi `ModalNavigationDrawer` (Menu Hamburger) yang terhubung langsung ke sistem *routing* utama.

### 3. Pengiriman Data Antar Layar (Passing Arguments)
Transisi dari Daftar Catatan ke Detail Catatan atau Form Edit mengimplementasikan pengiriman argumen dinamis. Parameter ID (`noteId`) dikirim melalui URL navigasi menggunakan spesifikasi `NavType.IntType`, memastikan layar tujuan menerima referensi data yang tepat.

### 4. Full CRUD Notes dengan MVVM
Fitur Catatan tidak lagi menggunakan data statis sementara, melainkan dikelola secara reaktif oleh `NotesViewModel`. Melalui UI yang *stateless*, pengguna dapat:
- Menambahkan catatan baru melalui *Floating Action Button*.
- Mengedit judul dan isi catatan (*Real-time binding*).
- Menghapus catatan secara permanen.
- Menyematkan bintang (*Favorite*), yang secara otomatis akan memfilter dan menampilkannya khusus di tab `Favorites`.

### 5. Proper Back-Stack Management
Tombol kembali (*Back Button*) di setiap *sub-screen* (Detail, Add, Edit) telah diprogram secara native menggunakan metode `navController.popBackStack()`. Hal ini memberikan alur pengalaman pengguna (UX) yang logis, aman, dan mencegah *memory leak*.

---

## 📂 Pembaruan Struktur Direktori (Clean Code)
Seiring dengan bertambahnya layar dan fitur, *source code* telah direstrukturisasi ulang ke dalam folder-folder spesifik berdasarkan pola *Separation of Concerns*:

```text
com.example.myfirstkmpapp
│
├── components/          # Reusable UI widget (BottomNavigationBar, DrawerContent)
├── data/                # Definisi Model Data (ProfileUiState, Note)
├── navigation/          # Pengaturan Routing Navigasi (AppNavigation, Screen Routes)
├── screens/             # Kumpulan Layar Navigasi (NoteList, Detail, Add/Edit, Favorites)
├── ui/                  # Layar Profile (Bawaan dari Tugas 4)
├── viewmodel/           # Logic bisnis & pengelolaan StateFlow (NotesVM, ProfileVM)
└── App.kt               # Root composition, Injection, & Status Bar Controller
```