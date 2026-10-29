package com.example.shopsey.ui.profile

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCode2
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsey.R

/**
 * Halaman Profile ShopSey sesuai referensi desain lampiran (Screenshot 2 & 3).
 *
 * Komponen:
 * 1. Header Profile: Avatar bulat besar, nama "R A", lokasi "Yogyakarta", subtitle "Lifestyle", tombol "Edit Profil"
 * 2. Bar Aksi 4 Kolom: Pesanan, Akses, Favorit, Pengaturan (dengan garis pembatas vertikal di antaranya)
 * 3. Menu List: Kotak Masuk, Manfaat Anggota Shopsey Anda, Acara (dengan chevron > dan garis pembatas)
 * 4. Section "Mengikuti (3)": Header dengan tombol Edit dan 3 thumbnail foto profil atlet
 * 5. Footer: "Anggota Sejak Agustus 2026"
 */
@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .statusBarsPadding()
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ----------------------------------------------------
            // 1. HEADER PROFILE (Avatar, Nama, Lokasi, Tombol Edit)
            // ----------------------------------------------------
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar Profil Bulat
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE2E2E6)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Avatar",
                        tint = Color.White,
                        modifier = Modifier.size(56.dp)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Nama Pengguna
                Text(
                    text = "R A",
                    color = Color(0xFF111111),
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 2.sp
                )

                Spacer(modifier = Modifier.height(3.dp))

                // Lokasi
                Text(
                    text = "Yogyakarta",
                    color = Color(0xFF111111),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(3.dp))

                // Subtitle Kategori
                Text(
                    text = "Lifestyle",
                    color = Color(0xFF777777),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Tombol "Edit Profil"
                Surface(
                    onClick = { /* Aksi Edit Profil */ },
                    shape = RoundedCornerShape(50),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFD1D1D6))
                ) {
                    Text(
                        text = "Edit Profil",
                        color = Color(0xFF111111),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 24.dp, vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            // ----------------------------------------------------
            // 2. BAR AKSI 4 KOLOM (Pesanan | Akses | Favorit | Pengaturan)
            // ----------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ProfileActionButton(
                    icon = Icons.Default.Inventory2,
                    label = "Pesanan",
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )

                VerticalDivider(
                    color = Color(0xFFE5E5EA),
                    thickness = 1.dp,
                    modifier = Modifier.height(36.dp)
                )

                ProfileActionButton(
                    icon = Icons.Default.QrCode2,
                    label = "Akses",
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )

                VerticalDivider(
                    color = Color(0xFFE5E5EA),
                    thickness = 1.dp,
                    modifier = Modifier.height(36.dp)
                )

                ProfileActionButton(
                    icon = Icons.Default.FavoriteBorder,
                    label = "Favorit",
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )

                VerticalDivider(
                    color = Color(0xFFE5E5EA),
                    thickness = 1.dp,
                    modifier = Modifier.height(36.dp)
                )

                ProfileActionButton(
                    icon = Icons.Default.Settings,
                    label = "Pengaturan",
                    onClick = { },
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // ----------------------------------------------------
            // 3. MENU LIST NAVIGASI (Kotak Masuk, Manfaat Anggota, Acara)
            // ----------------------------------------------------
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                // Item 1: Kotak Masuk
                ProfileNavRow(
                    title = "Kotak Masuk",
                    subtitle = "Lihat pesan",
                    onClick = { }
                )

                HorizontalDivider(
                    color = Color(0xFFEFEFF2),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                // Item 2: Manfaat Anggota Shopsey Anda
                ProfileNavRow(
                    title = "Manfaat Anggota Shopsey Anda",
                    subtitle = "Tersedia 2",
                    onClick = { }
                )

                HorizontalDivider(
                    color = Color(0xFFEFEFF2),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )

                // Item 3: Acara
                ProfileNavRow(
                    title = "Acara",
                    subtitle = "Lihat Semua Acara",
                    onClick = { }
                )

                HorizontalDivider(
                    color = Color(0xFFEFEFF2),
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            // ----------------------------------------------------
            // 4. SECTION: "Mengikuti (3)"
            // ----------------------------------------------------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mengikuti (3)",
                        color = Color(0xFF111111),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Edit",
                        color = Color(0xFF777777),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.clickable { }
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 3 Foto Tile Atlet / Model yang Diikuti
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(115.dp)
                            .background(Color(0xFFEEEEF2))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.shop11),
                            contentDescription = "Atlet 1",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(115.dp)
                            .background(Color(0xFFEEEEF2))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.shop10),
                            contentDescription = "Atlet 2",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .height(115.dp)
                            .background(Color(0xFFEEEEF2))
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.shop12),
                            contentDescription = "Atlet 3",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // ----------------------------------------------------
            // 5. FOOTER: Informasi Keanggotaan
            // ----------------------------------------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF7F7F9))
                    .padding(vertical = 22.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Anggota Sejak Agustus 2026",
                    color = Color(0xFF8E8E93),
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                )
            }

            // Bottom Space agar tidak tertutup floating navbar
            Spacer(modifier = Modifier.height(130.dp))
        }
    }
}

/**
 * Item Tombol Aksi Cepat pada Bar 4 Kolom
 */
@Composable
private fun ProfileActionButton(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color(0xFF111111),
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = label,
            color = Color(0xFF111111),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

/**
 * Baris Item Navigasi Menu Profil dengan Chevron >
 */
@Composable
private fun ProfileNavRow(
    title: String,
    subtitle: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 18.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = Color(0xFF111111),
                fontSize = 17.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = subtitle,
                color = Color(0xFF8E8E93),
                fontSize = 14.sp
            )
        }

        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Buka",
            tint = Color(0xFF111111),
            modifier = Modifier.size(20.dp)
        )
    }
}
