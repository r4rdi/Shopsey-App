package com.example.shopsey.ui.bag

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsey.R

/**
 * Halaman Bag (Shopping Bag) ShopSey sesuai referensi desain lampiran (Screenshot 4).
 *
 * Menggunakan gambar produk Air Jordan 4 'Toro' dari R.drawable.bag1.
 *
 * Komponen:
 * 1. Header: Judul "Bag" rata kiri
 * 2. Kartu Produk: Gambar produk Air Jordan 4 'Toro', nama, kategori, colorway, ukuran
 * 3. Selector Jumlah (Qty 1 ⌄) & Harga Satuan (Rp3.329.000)
 * 4. Rincian Biaya: Subtotal, Shipping, Estimated Total (+ Tax)
 * 5. Tombol Checkout: Kapsul hitam pekat full-width
 */
@Composable
fun BagScreen(
    modifier: Modifier = Modifier,
    onCheckoutClick: () -> Unit = {},
    onProductClick: () -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var quantity by remember { mutableIntStateOf(1) }

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
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // ----------------------------------------------------
            // 1. HEADER: Judul Halaman "Bag"
            // ----------------------------------------------------
            Text(
                text = "Bag",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111111)
            )

            Spacer(modifier = Modifier.height(28.dp))

            // ----------------------------------------------------
            // 2. KARTU PRODUK (Air Jordan 4 'Toro')
            // ----------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .clickable(
                        interactionSource = remember { MutableInteractionSource() },
                        indication = ripple(bounded = true),
                        onClick = onProductClick
                    )
                    .padding(4.dp),
                verticalAlignment = Alignment.Top
            ) {
                // Wadah Gambar Sepatu dengan background abu-abu terang
                Box(
                    modifier = Modifier
                        .size(135.dp)
                        .background(Color(0xFFEEEEF0))
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.bag1),
                        contentDescription = "Air Jordan 4 'Toro'",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                // Detail Informasi Produk
                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    Text(
                        text = "Air Jordan 4 'Toro'",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111111)
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Men's Shoes",
                        fontSize = 14.sp,
                        color = Color(0xFF555555)
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "Fire Red/Black/Cement Grey/White",
                        fontSize = 13.sp,
                        color = Color(0xFF777777),
                        lineHeight = 18.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Size: 42",
                        fontSize = 14.sp,
                        color = Color(0xFF555555)
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Baris Pilihan Quantity (Qty 1 ⌄) & Harga Produk
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Selector Qty
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clickable { /* Ubah qty jika diperlukan */ }
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "Qty $quantity",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF111111)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Pilih Jumlah",
                        tint = Color(0xFF111111),
                        modifier = Modifier.size(18.dp)
                    )
                }

                // Harga Satuan
                Text(
                    text = "Rp3.329.000",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111111)
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Garis Pembatas
            HorizontalDivider(
                color = Color(0xFFE5E5EA),
                thickness = 1.dp
            )

            Spacer(modifier = Modifier.height(22.dp))

            // ----------------------------------------------------
            // 3. RINCIAN BIAYA (Subtotal, Shipping, Estimated Total)
            // ----------------------------------------------------
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Subtotal
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Subtotal",
                        fontSize = 14.sp,
                        color = Color(0xFF555555)
                    )
                    Text(
                        text = "Rp3.329.000,00",
                        fontSize = 14.sp,
                        color = Color(0xFF555555)
                    )
                }

                // Shipping
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Shipping",
                        fontSize = 14.sp,
                        color = Color(0xFF555555)
                    )
                    Text(
                        text = "Standard - Free",
                        fontSize = 14.sp,
                        color = Color(0xFF555555)
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Estimated Total
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Estimated Total",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111111)
                    )
                    Text(
                        text = "Rp3.329.000,00 + Tax",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111111)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // ----------------------------------------------------
            // 4. TOMBOL CHECKOUT (Kapsul Hitam Pekat)
            // ----------------------------------------------------
            Surface(
                onClick = onCheckoutClick,
                shape = RoundedCornerShape(50),
                color = Color.Black,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Text(
                        text = "Checkout",
                        color = Color.White,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Bottom Space agar konten tidak tertutup Floating Navbar
            Spacer(modifier = Modifier.height(130.dp))
        }
    }
}
