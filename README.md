# 🛍️ ShopSey — Elegant E-Commerce Mobile App for Sports and Lifestyle Gear

![License](https://img.shields.io/badge/License-MIT-yellow.svg)
![Android Studio](https://img.shields.io/badge/Android_Studio-3DDC84?style=flat&logo=android-studio&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-B125EA?style=flat&logo=kotlin&logoColor=white)
![Architecture](https://img.shields.io/badge/Architecture-MVVM-blue.svg)
![Status](https://img.shields.io/badge/Status-Mock_Data_(UI_Only)-orange.svg)

**ShopSey** adalah aplikasi mobile *e-commerce* fesyen dan *lifestyle* bernuansa elegan (Hitam & Putih). Proyek ini dikembangkan sebagai **Tugas Akhir** untuk mendemonstrasikan implementasi UI/UX yang modern, navigasi yang mulus, dan arsitektur kode yang bersih pada platform Android. 

*Catatan: Aplikasi ini saat ini berjalan menggunakan **Mock Data** (data tiruan JSON lokal) untuk menyimulasikan respons sistem tanpa memerlukan server backend.*

---

## 📸 Tampilan Aplikasi (Screenshots)

*(Ganti URL gambar di bawah ini dengan screenshot aplikasi Anda setelah di-upload ke GitHub atau Imgur)*

| Splash & Home | Kategori Produk | Detail Produk | Keranjang (Cart) |
| :---: | :---: | :---: | :---: |
| <img src="URL_GAMBAR_1" width="200"/> | <img src="URL_GAMBAR_2" width="200"/> | <img src="URL_GAMBAR_3" width="200"/> | <img src="URL_GAMBAR_4" width="200"/> |

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
*   **UI Toolkit:** XML Layouts / Material Design 3
*   **Arsitektur:** **MVVM (Model-View-ViewModel)** untuk pemisahan logika dan tampilan.
*   **Asynchronous Programming:** Kotlin Coroutines (digunakan untuk menyimulasikan *delay* jaringan/loading data).
*   **Data Handling:** 
    *   `Moshi` / `Gson` (untuk mem-*parsing* file `products.json` dari folder assets).
    *   `LiveData` / `StateFlow` (untuk mengamati perubahan data UI).
*   **Image Loading:** [Glide](https://github.com/bumptech/glide) / [Coil](https://coil-kt.github.io/coil/) (untuk merender *mock image URL* dengan cepat).
*   **Navigation:** Android Jetpack Navigation Component.

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
>>>>>>> 15e8311 (feat: implement authentication flow, welcome screen, and home page with section scrolling and 3D floating navbar)
