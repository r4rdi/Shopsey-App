package com.example.shopsey.ui.auth

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsey.R
import kotlinx.coroutines.delay

@Composable
fun LandingScreen(
    onSignUpClick: () -> Unit,
    onSignInClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 1. Koleksi gambar landing: landing1.png hingga landing6.png
    val landingImages = remember {
        listOf(
            R.drawable.landing1,
            R.drawable.landing2,
            R.drawable.landing3,
            R.drawable.landing4,
            R.drawable.landing5,
            R.drawable.landing6
        )
    }

    var currentImageIndex by remember { mutableIntStateOf(0) }

    // 2. Coroutine Loop: Berpindah gambar setiap 3 detik secara bergantian
    LaunchedEffect(key1 = true) {
        while (true) {
            delay(3000L) // Delay 3 detik antar perpindahan gambar
            currentImageIndex = (currentImageIndex + 1) % landingImages.size
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // 3. Background Full-Screen dengan Efek Transisi Crossfade yang Halus (1000ms)
        Crossfade(
            targetState = currentImageIndex,
            animationSpec = tween(durationMillis = 1000),
            label = "LandingBackgroundCrossfade",
            modifier = Modifier.fillMaxSize()
        ) { targetIndex ->
            Image(
                painter = painterResource(id = landingImages[targetIndex]),
                contentDescription = "Landing Background",
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        // 4. Efek Layer Shadow Vignette Gelap di Seluruh Pinggiran Halaman (Keliling & Sudut)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .drawBehind {
                    val canvasWidth = size.width
                    val canvasHeight = size.height
                    val radius = maxOf(canvasWidth, canvasHeight) * 0.72f

                    drawRect(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.40f),
                                Color.Black.copy(alpha = 0.85f)
                            ),
                            center = Offset(canvasWidth / 2f, canvasHeight / 2f),
                            radius = radius
                        )
                    )
                }
        )

        // 5. Gradient Overlay Gelap di Bagian Bawah agar Teks Selalu Terbaca Jelas & Tajam
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.35f),
                            Color.Black.copy(alpha = 0.85f),
                            Color.Black
                        ),
                        startY = 450f
                    )
                )
        )

        // 6. Konten Teks & Tombol di Bagian Bawah
        Column(
            modifier = Modifier
                .fillMaxSize()
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 28.dp),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.Start
        ) {
            // Ikon Logo ShopSey (Putih) di Kiri Bawah
            Image(
                painter = painterResource(id = R.drawable.logo3),
                contentDescription = "ShopSey Logo",
                modifier = Modifier.size(44.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Teks Heading Inspiratif
            Text(
                text = "Bringing ShopSey Members the best products, inspiration and stories in sport.",
                color = Color.White,
                fontSize = 25.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Row Tombol Sign Up & Sign In (~1.3x Scale)
            Row(
                horizontalArrangement = Arrangement.spacedBy(14.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Tombol Sign Up (Latar Putih, Teks Hitam, Bentuk Capsule)
                Button(
                    onClick = onSignUpClick,
                    modifier = Modifier
                        .height(48.dp)
                        .defaultMinSize(minWidth = 130.dp),
                    shape = RoundedCornerShape(percent = 50),
                    contentPadding = PaddingValues(horizontal = 28.dp, vertical = 0.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "Sign Up",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                // Tombol Sign In (Latar Transparan/Hitam, Border Putih Tipis, Teks Putih)
                OutlinedButton(
                    onClick = onSignInClick,
                    modifier = Modifier
                        .height(48.dp)
                        .defaultMinSize(minWidth = 130.dp),
                    shape = RoundedCornerShape(percent = 50),
                    contentPadding = PaddingValues(horizontal = 28.dp, vertical = 0.dp),
                    border = BorderStroke(1.dp, Color.White),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = Color.Black.copy(alpha = 0.4f),
                        contentColor = Color.White
                    )
                ) {
                    Text(
                        text = "Sign In",
                        fontSize = 15.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
