package com.example.shopsey.ui.auth

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredWidth
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsey.R

/**
 * Halaman Welcome Page
 *
 * Ditampilkan setelah pengguna menekan tombol "Lanjutkan" pada LoginSuccessScreen.
 * Menampilkan:
 * 1. Background kolase gambar olahraga (welcome1.png sampai welcome15.png)
 *    dengan rotasi kemiringan diagonal 30 derajat.
 * 2. Lapisan bayangan (vignette/shadow layer) sinematik ditingkatkan ~1.3x agar teks terbaca kontras.
 * 3. Logo Shopsey putih elegan transparan.
 * 4. Teks sambutan:
 *    "Hi [Nama Pengguna, misal R],
 *     Welcome to Shopsey
 *     Thanks for being with us!" (dikecilkan 0.88x)
 *    dengan posisi ditengahkan sedikit turun secara vertikal dan align left horizontal.
 * 5. Footer: "Member since 2026"
 * 6. Efek sentuhan/touch/onclick untuk langsung berpindah ke HomeScreen dengan ripple feedback.
 */
@Composable
fun WelcomeScreen(
    userEmail: String,
    onContinue: () -> Unit,
    modifier: Modifier = Modifier
) {
    // Ekstraksi inisial nama atau huruf pertama dari email (contoh: rardiansyah3421@gmail.com -> R)
    val displayInitial = remember(userEmail) {
        userEmail.substringBefore("@")
            .trim()
            .firstOrNull()?.uppercase() ?: "R"
    }

    // Animasi fade in untuk teks dan logo
    val contentAlpha = remember { Animatable(0f) }

    LaunchedEffect(Unit) {
        contentAlpha.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 800)
        )
    }

    // 15 gambar latar belakang dibagi merata ke dalam 5 kolom diagonal agar ukuran gambar lebih kecil
    val col1Images = remember {
        listOf(
            R.drawable.welcome1,
            R.drawable.welcome6,
            R.drawable.welcome11,
            R.drawable.welcome1,
            R.drawable.welcome6
        )
    }
    val col2Images = remember {
        listOf(
            R.drawable.welcome2,
            R.drawable.welcome7,
            R.drawable.welcome12,
            R.drawable.welcome2,
            R.drawable.welcome7
        )
    }
    val col3Images = remember {
        listOf(
            R.drawable.welcome3,
            R.drawable.welcome8,
            R.drawable.welcome13,
            R.drawable.welcome3,
            R.drawable.welcome8
        )
    }
    val col4Images = remember {
        listOf(
            R.drawable.welcome4,
            R.drawable.welcome9,
            R.drawable.welcome14,
            R.drawable.welcome4,
            R.drawable.welcome9
        )
    }
    val col5Images = remember {
        listOf(
            R.drawable.welcome5,
            R.drawable.welcome10,
            R.drawable.welcome15,
            R.drawable.welcome5,
            R.drawable.welcome10
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFF141414))
            .clipToBounds()
    ) {
        // 1. Kolase Gambar Miring Diagonal dengan Rotasi Kemiringan 30 Derajat, Ukuran Lebih Kecil & Padding Rapat
        Row(
            modifier = Modifier
                .align(Alignment.Center)
                .requiredWidth(840.dp)
                .graphicsLayer {
                    rotationZ = -30f
                },
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // Kolom 1
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 10.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                col1Images.forEach { resId ->
                    WelcomeImageCard(resId = resId, height = 175.dp)
                }
            }

            // Kolom 2 (Staggered Offset)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 40.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                col2Images.forEach { resId ->
                    WelcomeImageCard(resId = resId, height = 160.dp)
                }
            }

            // Kolom 3 (Staggered Offset)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 15.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                col3Images.forEach { resId ->
                    WelcomeImageCard(resId = resId, height = 180.dp)
                }
            }

            // Kolom 4 (Staggered Offset)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 45.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                col4Images.forEach { resId ->
                    WelcomeImageCard(resId = resId, height = 165.dp)
                }
            }

            // Kolom 5 (Staggered Offset)
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 20.dp),
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                col5Images.forEach { resId ->
                    WelcomeImageCard(resId = resId, height = 170.dp)
                }
            }
        }

        // 2. Lapisan Bayangan / Shadow Layer (Ditingkatkan ~1.3x)
        // Lapisan 2a: Dark base tint transparan merata
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.26f))
        )

        // Lapisan 2b: Gradien lembut vertikal agar teks putih tetap terbaca jelas
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.11f),
                            Color.Black.copy(alpha = 0.33f),
                            Color.Black.copy(alpha = 0.55f),
                            Color.Black.copy(alpha = 0.78f)
                        )
                    )
                )
        )

        // 3. Konten Teks & Logo di Bagian Depan
        Box(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .graphicsLayer {
                    alpha = contentAlpha.value
                }
        ) {
            // Konten Tengah-Kiri: Ditengahkan dan sedikit turun secara vertikal (+30.dp), tetap align left horizontal
            Column(
                modifier = Modifier
                    .align(Alignment.CenterStart)
                    .offset(y = 30.dp)
                    .padding(horizontal = 28.dp)
            ) {
                // Logo ShopSey (Ikon Oranye + Teks ShopSey Putih Transparan tanpa background hitam)
                Image(
                    painter = painterResource(id = R.drawable.logo_shopsey_white),
                    contentDescription = "ShopSey Logo",
                    modifier = Modifier.height(28.dp)
                )

                Spacer(modifier = Modifier.height(18.dp))

                // Teks "Hi R,"
                Text(
                    text = "Hi $displayInitial,",
                    color = Color.White,
                    fontSize = 24.sp,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                // Teks "Welcome to Shopsey" (tanpa tanda koma)
                Text(
                    text = "Welcome to Shopsey",
                    color = Color.White,
                    fontSize = 24.sp,
                    lineHeight = 30.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                // Teks "Thanks for being with us!"
                Text(
                    text = "Thanks for being\nwith us!",
                    color = Color.White,
                    fontSize = 21.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            // Footer: "Member since 2026"
            Text(
                text = "Member since 2026",
                color = Color.White.copy(alpha = 0.70f),
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 28.dp, bottom = 28.dp)
            )
        }

        // 4. Lapisan Touch / OnClick Interaktif dengan Efek Ripple Putih Elegan
        // Menggantikan sistem delay otomatis: pengguna dapat mengetuk di mana saja pada layar
        // untuk langsung berpindah ke halaman utama (Homepage).
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(color = Color.White)
                ) {
                    onContinue()
                }
        )
    }
}

/**
 * Kartu individual gambar olahraga dengan sudut rounded
 */
@Composable
private fun WelcomeImageCard(
    resId: Int,
    height: Dp,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(height)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF222222))
    ) {
        Image(
            painter = painterResource(id = resId),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
    }
}
