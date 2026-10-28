# 🛍️ ShopSey — Elegant E-Commerce Mobile App for Sports and Lifestyle Gear

![License](https://img.shields.io/badge/License-MIT-yellow.svg)
![Android](https://img.shields.io/badge/Android-3DDC84?logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-7F52FF?logo=kotlin&logoColor=white)
![Jetpack Compose](https://img.shields.io/badge/Jetpack_Compose-4285F4?logo=jetpackcompose&logoColor=white)
![JavaScript](https://img.shields.io/badge/JavaScript-F7DF1E?logo=javascript&logoColor=black)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-blue.svg)
![Status](https://img.shields.io/badge/Status-Mock_Data_(UI_Only)-orange.svg)

**ShopSey** adalah aplikasi mobile *e-commerce* fesyen dan *lifestyle* bernuansa elegan (Hitam & Putih). Proyek ini dikembangkan sebagai **Tugas Akhir** untuk mendemonstrasikan implementasi UI/UX yang modern, navigasi yang mulus, dan arsitektur kode yang bersih pada platform Android. 

*Catatan: Aplikasi ini saat ini berjalan menggunakan **Mock Data** (data tiruan JSON lokal) untuk menyimulasikan respons sistem tanpa memerlukan server backend.*

---

## 📸 Tampilan Aplikasi (Screenshots)

| Splash & Home | Kategori Produk | Detail Produk | Keranjang (Cart) |
| :---: | :---: | :---: | :---: |
| <img src="app/src/main/res/drawable/splash_screen_page.png" width="200"/> | <img src="app/src/main/res/drawable/kategori_page.png" width="200"/> | <img src="app/src/main/res/drawable/detail_page.png" width="200"/> | <img src="app/src/main/res/drawable/cart_page.png" width="200"/> |

---

## ✨ Fitur Utama

### 🎨 UI/UX & Desain
*   **Tema Minimalis & Elegan:** Menggunakan *Primary Color* hitam solid dan *Background* putih bersih untuk menonjolkan produk.
*   **Splash Screen:** Animasi logo minimalis saat aplikasi pertama kali dibuka.
*   **Dynamic Bottom Navigation:** Navigasi bawah yang responsif untuk berpindah halaman (Home, Kategori, Cart, Wishlist, Profile).

### 🛒 Fungsionalitas E-Commerce (Simulasi)
*   **Katalog Berbasis Kategori:** Filter produk berdasarkan kategori (Wanita, Pria, Sports, Anak).
*   **Banner Carousel:** Slider promosi otomatis (contoh: *Super Brand Day*, *Payday Promo*).
*   **Flash Sale Timer:** *Countdown timer* interaktif untuk penawaran *Speed Deals*.
*   **Top Brands Showcase:** Navigasi cepat melalui logo merek teratas (Zara, H&M, Nike, dll).
*   **Wishlist & Cart State:** Simulasi menambahkan barang ke keranjang dan daftar keinginan (disimpan secara lokal menggunakan `ViewModel`).

---

## 🛠️ Teknologi & Tech Stack

Aplikasi ini dibangun murni secara *Native* untuk Android dengan spesifikasi berikut:

*   **Bahasa Pemrograman:** [Kotlin](https://kotlinlang.org/)
*   **UI Toolkit:** [Jetpack Compose](https://developer.android.com/jetpack/compose) & Material Design 3
*   **Arsitektur:** **MVVM (Model-View-ViewModel)** untuk pemisahan logika dan tampilan.
*   **Animasi & Auto-Scroll:** Kotlin Coroutines & JavaScript/Delay timing mechanism untuk loop infinite scrolling dan progress-bar indicator.
*   **Navigation:** Android Jetpack Navigation Compose.

---

## 🚀 Cara Menjalankan Proyek Secara Lokal

Ikuti langkah-langkah berikut untuk menjalankan aplikasi ini di komputer Anda:

### Prasyarat
*   **Android Studio** (Versi *Flamingo* atau terbaru direkomendasikan).
*   Emulator Android atau Perangkat Fisik (Minimum Android 8.0 - API 26).

### Instalasi
1.  **Clone repositori ini:**
    ```bash
    git clone [https://github.com/r4rdi/ShopSey-App.git](https://github.com/r4rdi/ShopSey-App.git)
    ```
2.  Buka **Android Studio**.
3.  Pilih **Open** dan cari folder tempat Anda melakukan proses *clone* proyek ini.
4.  Tunggu hingga proses **Gradle Build / Sync** selesai sepenuhnya.
5.  Pilih perangkat target (Emulator atau Device yang terhubung).
6.  Klik tombol ▶️ **Run** (atau tekan `Shift + F10`).

---

## 📂 Struktur Folder Proyek

```text
ShopSey/app/src/main/
├── assets/             # Berisi file JSON lokal (simulasi database backend)
│   ├── products.json
│   └── banners.json
├── java/com/example/shopsey/
│   ├── data/           # Layer Data (Repository, Models/Data Classes)
│   ├── ui/             # Layer Tampilan (Activity, Fragment, Adapters)
│   ├── viewmodel/      # Layer Logika UI (MVVM)
│   └── utils/          # Konstanta, Helper, dan Extension functions
└── res/                # Resource UI (Layout XML, Drawable, Colors, Fonts)

```

---

## 🔮 Rencana Pengembangan (Roadmap)

Untuk saat ini aplikasi menggunakan Mock Data, namun ke depannya arsitektur ini sudah disiapkan untuk:

* [x] Integrasi dengan REST API sungguhan menggunakan **Retrofit**.
* [x] Autentikasi user nyata (Login/Register) menggunakan Firebase Auth.
* [x] Penyimpanan lokal persisten menggunakan Room Database (untuk *Cart*).

---

## 👨‍💻 Penulis

**Rafi Ardiansyah**

* Mahasiswa Jurusan SIJA di SMK Negeri 2 Yogyakarta
* GitHub: [@r4rdi](https://github.com/r4rdi/ShopSey-App.git)
* LinkedIn: [Rafi Ardiansyah](https://www.linkedin.com/in/rafi-ardiansyah-3935b03b2?utm_source=share_via&utm_content=profile&utm_medium=member_android)
