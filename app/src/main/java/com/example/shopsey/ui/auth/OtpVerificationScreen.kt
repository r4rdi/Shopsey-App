package com.example.shopsey.ui.auth

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.example.shopsey.R
import kotlinx.coroutines.delay
import kotlin.random.Random

// Fungsi Helper untuk menghasilkan kode OTP acak 6 digit dinamis
fun generateRandom6DigitOtp(): String {
    return Random.nextInt(100000, 999999).toString()
}

// Fungsi Helper untuk mengirimkan notifikasi sistem ke Status Bar / Notification Bar Android
fun triggerSystemNotification(context: Context, otpCode: String) {
    try {
        val channelId = "shopsey_otp_channel"
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as? NotificationManager ?: return

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Kode Verifikasi ShopSey",
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = "Notifikasi pengiriman kode verifikasi OTP"
                enableLights(true)
                enableVibration(true)
            }
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, channelId)
            .setSmallIcon(R.drawable.logo3)
            .setContentTitle("ShopSey Security")
            .setContentText("Kode verifikasi Anda adalah $otpCode. Gunakan untuk masuk.")
            .setStyle(NotificationCompat.BigTextStyle().bigText("Kode verifikasi akun ShopSey Anda adalah $otpCode. Jangan berikan kode ini kepada siapa pun."))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1001, notification)
    } catch (_: Exception) {
        // Fallback aman jika pembatasan izin OS berlaku
    }
}

@Composable
fun OtpVerificationScreen(
    userEmail: String,
    onBack: () -> Unit,
    onEditEmail: () -> Unit,
    onLoginSuccess: () -> Unit,
    onNavigateToPassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // State untuk kode OTP 6 digit dinamis (berubah acak setiap sesi/resend)
    var dynamicOtpCode by remember { mutableStateOf(generateRandom6DigitOtp()) }
    var otpInput by remember { mutableStateOf("") }
    var countdown by remember { mutableIntStateOf(28) }
    var hasAttemptedSubmit by remember { mutableStateOf(false) }

    val redErrorColor = Color(0xFFBA1A1A)
    val isOtpMatch = otpInput == dynamicOtpCode
    val isOtpError = hasAttemptedSubmit && !isOtpMatch

    val otpErrorMsg = when {
        otpInput.isBlank() -> "Wajib diisi"
        otpInput.length < 6 -> "Kode harus 6 digit"
        else -> "Kode verifikasi tidak sesuai"
    }

    // Runtime Permission untuk Notification Bar Android 13+ (POST_NOTIFICATIONS)
    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            triggerSystemNotification(context, dynamicOtpCode)
        }
    }

    LaunchedEffect(Unit) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                permissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // 1. Timer Countdown 28 detik
    LaunchedEffect(countdown) {
        if (countdown > 0) {
            delay(1000L)
            countdown--
        }
    }

    // 2. Notifikasi dikirimkan langsung ke Notification Bar sistem Android
    LaunchedEffect(dynamicOtpCode) {
        delay(800L)
        triggerSystemNotification(context, dynamicOtpCode)
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            // Top Bar Transparan dengan Ikon Back
            IconButton(
                onClick = onBack,
                modifier = Modifier.padding(top = 4.dp, bottom = 12.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color.Black,
                    modifier = Modifier.size(26.dp)
                )
            }

            // Ikon Logo ShopSey (Hitam)
            Image(
                painter = painterResource(id = R.drawable.logo3),
                contentDescription = "ShopSey Logo",
                colorFilter = ColorFilter.tint(Color.Black),
                modifier = Modifier.size(38.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Heading Teks
            Text(
                text = "Masukkan kode 6 digit yang dikirim ke email Anda.",
                color = Color.Black,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Subtitle Email Tujuan & Tombol Edit
            Column {
                Text(
                    text = "Kami telah mengirimkan kode ke",
                    color = Color(0xFF616161),
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = userEmail,
                        color = Color.Black,
                        fontSize = 14.sp,
                        lineHeight = 20.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Edit",
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        textDecoration = TextDecoration.Underline,
                        modifier = Modifier.clickable { onEditEmail() }
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // OutlinedTextField Input Kode 6 Digit dengan Validasi Kecocokan OTP
            OutlinedTextField(
                value = otpInput,
                onValueChange = { input ->
                    val filtered = input.filter { it.isDigit() }
                    if (filtered.length <= 6) {
                        otpInput = filtered
                    }
                },
                label = {
                    Text(
                        text = "Kode 6 digit*",
                        color = if (isOtpError) redErrorColor else Color(0xFF757575)
                    )
                },
                singleLine = true,
                isError = isOtpError,
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                trailingIcon = {
                    if (isOtpError) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Error",
                            tint = redErrorColor
                        )
                    }
                },
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.Black,
                    unfocusedBorderColor = Color(0xFFCCCCCC),
                    errorBorderColor = redErrorColor,
                    errorLabelColor = redErrorColor,
                    focusedLabelColor = Color.Black,
                    unfocusedLabelColor = Color(0xFF757575),
                    cursorColor = Color.Black
                ),
                modifier = Modifier.fillMaxWidth()
            )

            if (isOtpError) {
                Text(
                    text = otpErrorMsg,
                    color = redErrorColor,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 6.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Teks Timer Hitung Mundur & Resend Kode Dinamis Baru ke Notification Bar
            Text(
                text = if (countdown > 0) "Kirim ulang kode dalam $countdown detik" else "Kirim ulang kode sekarang",
                color = if (countdown > 0) Color(0xFF757575) else Color.Black,
                fontWeight = if (countdown == 0) FontWeight.SemiBold else FontWeight.Normal,
                fontSize = 13.sp,
                textAlign = TextAlign.End,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(enabled = countdown == 0) {
                        dynamicOtpCode = generateRandom6DigitOtp()
                        countdown = 28
                        hasAttemptedSubmit = false
                        triggerSystemNotification(context, dynamicOtpCode)
                    }
            )
        }

        // Tombol Statis di Bagian Paling Bawah (Bebas dari imePadding / Tidak Bergerak-gerak)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp)
                .fillMaxWidth()
        ) {
            // Tombol "Masuk" (Hitam) - Wajib Mencocokkan Kode OTP Dinamis yang Dikirim
            Button(
                onClick = {
                    hasAttemptedSubmit = true
                    if (isOtpMatch) {
                        onLoginSuccess()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(percent = 50),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Black,
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = "Masuk",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tombol "Gunakan Kata Sandi" (Outlined Putih Batas Abu-abu)
            OutlinedButton(
                onClick = onNavigateToPassword,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                shape = RoundedCornerShape(percent = 50),
                border = BorderStroke(1.dp, Color(0xFFE0E0E0)),
                colors = ButtonDefaults.outlinedButtonColors(
                    containerColor = Color.White,
                    contentColor = Color.Black
                )
            ) {
                Text(
                    text = "Gunakan Kata Sandi",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
