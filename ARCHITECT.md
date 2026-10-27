# ARCHITECT.md

## Arsitektur Sistem Mobile (ShopSey)
Karena aplikasi ini difokuskan untuk demonstrasi UI/UX dan simulasi aliran data tanpa *backend* nyata, aplikasi dibangun menggunakan pola arsitektur **MVVM (Model-View-ViewModel)**. Pola ini memisahkan logika antarmuka pengguna dari logika bisnis dan pengelolaan data.

### 🏗️ Pola Arsitektur (MVVM)
*   **View (UI Layer):** Berisi Activity dan Fragment. Bertanggung jawab hanya untuk merender UI (mengatur warna hitam-putih, menampilkan daftar produk) dan mendengarkan interaksi pengguna (*click listener*). View mengamati perubahan data dari ViewModel.
*   **ViewModel:** Menyimpan dan mengelola data terkait UI dalam siklus hidup (*lifecycle*) yang sadar. ViewModel akan meminta *mock data* dari layer data dan meneruskannya ke View menggunakan `LiveData` atau `StateFlow`.
*   **Model (Data Layer):** Direpresentasikan oleh kelas *Repository*. Bertugas mengambil, menyaring, dan menyortir *mock data*.

### 📂 Strategi Mock Data
Karena tidak ada server *backend*, data disimulasikan menggunakan pendekatan **Local JSON Asset**:
1.  **File JSON:** Terdapat file seperti `products.json`, `banners.json`, dan `categories.json` yang disimpan di dalam direktori `app/src/main/assets/`.
2.  **Parser:** Saat ViewModel meminta data, Repository akan membaca file JSON tersebut dan mem-parsing-nya menjadi objek *data class* Kotlin menggunakan *library* seperti **Gson** atau **Moshi**.
3.  **Delay Simulasi (Opsional):** Menggunakan Kotlin Coroutines dengan fungsi `delay()` selama 1-2 detik di dalam Repository untuk mensimulasikan latensi jaringan, sehingga layar *loading/shimmer effect* dapat didemonstrasikan.

### 📦 Struktur Folder (Package Structure)
Direktori kode disusun berdasarkan *feature* (Fitur) untuk memudahkan pengelolaan skala proyek:
```text
com.example.shopsey
│
├── data/
│   ├── model/         (Data classes e.g., Product, Category, Banner)
│   ├── repository/    (MockDataRepository)
│   └── source/        (JsonAssetReader)
│
├── ui/
│   ├── home/          (HomeFragment, HomeViewModel, HomeAdapter)
│   ├── category/      (CategoryFragment, CategoryViewModel)
│   ├── cart/          (CartFragment, CartAdapter)
│   └── detail/        (ProductDetailActivity)
│
└── utils/             (Constants, Extensions, Helper functions)