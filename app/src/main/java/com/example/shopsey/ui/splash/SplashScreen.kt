package com.example.shopsey.ui.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.shopsey.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    // Menahan splash screen selama 2.5 detik lalu navigasi ke home
    LaunchedEffect(key1 = true) {
        delay(2500L)
        navController.navigate("landing_screen") {
            // Hapus rute splash dari backstack agar tidak bisa kembali saat tombol Back ditekan
            popUpTo("splash") {
                inclusive = true
            }
        }
    }

    // Tampilan Splash Screen: Layar hitam pekat dengan Logo ShopSey (logo1.png) di tengah
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo1),
            contentDescription = "ShopSey Logo",
            contentScale = ContentScale.Fit,
            modifier = Modifier
                .width(260.dp)
                .padding(16.dp)
        )
    }
}
