package com.example.shopsey.ui.auth

import android.Manifest
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
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.shopsey.R
import kotlinx.coroutines.delay

/**
 * Halaman Reset Kata Sandi ("Verifikasikan email Anda dan masukkan kata sandi baru.")
 *
 * Diakses saat pengguna menekan "Lupa kata sandi?" pada halaman PasswordInputScreen.
 * Fitur:
 * 1. Input field "Kode*" 6 digit dengan tombol refresh yang mengirimkan notifikasi OTP dinamis ke status bar.
 * 2. Input field "Kata Sandi Baru*" dengan toggle mata untuk melihat / menyembunyikan kata sandi.
 * 3. Kriteria kata sandi interaktif:
 *    - Minimum 8 karakter
 *    - Huruf besar, huruf kecil, dan satu angka
 * 4. Tombol statis "Simpan" (Hitam) dan "Batal" (Outlined).
 */
@Composable
fun ForgotPasswordScreen(
    userEmail: String,
    onBack: () -> Unit,
    onEditEmail: () -> Unit,
    onResetSuccess: () -> Unit,
    onCancel: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var otpCode by remember { mutableStateOf("") }
    var newPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var hasAttemptedSubmit by remember { mutableStateOf(false) }

    // State kode OTP 6 digit dinamis
    var dynamicOtpCode by remember { mutableStateOf(generateRandom6DigitOtp()) }

    // Runtime Permission untuk Notification Bar Android 13+
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

    // Trigger notifikasi sistem ke Notification Bar Android
    LaunchedEffect(dynamicOtpCode) {
        delay(800L)
        triggerSystemNotification(context, dynamicOtpCode)
    }

    val redErrorColor = Color(0xFFBA1A1A)
    val successGreenColor = Color(0xFF16A34A)

    // Validasi OTP
    val isOtpMatch = otpCode == dynamicOtpCode
    val isOtpError = hasAttemptedSubmit && !isOtpMatch
    val otpErrorMsg = when {
        otpCode.isBlank() -> "Wajib diisi"
        otpCode.length < 6 -> "Kode harus 6 digit"
        else -> "Kode verifikasi tidak sesuai"
    }

    // Validasi Kriteria Kata Sandi Baru
    val hasMin8Chars = newPassword.length >= 8
    val hasUpperLowerDigit = newPassword.any { it.isUpperCase() } &&
            newPassword.any { it.isLowerCase() } &&
            newPassword.any { it.isDigit() }
    val isPasswordValid = hasMin8Chars && hasUpperLowerDigit
    val isPasswordError = hasAttemptedSubmit && !isPasswordValid

    val isFormValid = isOtpMatch && isPasswordValid

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
            // 1. Top Bar Transparan dengan Ikon Back
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

            // 2. Ikon Logo ShopSey (Hitam)
            Image(
                painter = painterResource(id = R.drawable.logo3),
                contentDescription = "ShopSey Logo",
                colorFilter = ColorFilter.tint(Color.Black),
                modifier = Modifier.size(38.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // 3. Teks Heading
            Text(
                text = "Verifikasikan email Anda dan masukkan kata sandi baru.",
                color = Color.Black,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Subtitle Email & Tombol Edit
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

            // 5. Input Field Kode OTP dengan Tombol Refresh di Sisi Kanan
            OutlinedTextField(
                value = otpCode,
                onValueChange = { input ->
                    val filtered = input.filter { it.isDigit() }
                    if (filtered.length <= 6) {
                        otpCode = filtered
                    }
                },
                label = {
                    Text(
                        text = "Kode*",
                        color = if (isOtpError) redErrorColor else Color(0xFF757575)
                    )
                },
                singleLine = true,
                isError = isOtpError,
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                trailingIcon = {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Refresh Kode OTP",
                        tint = Color.Black,
                        modifier = Modifier
                            .size(24.dp)
                            .clickable {
                                dynamicOtpCode = generateRandom6DigitOtp()
                                triggerSystemNotification(context, dynamicOtpCode)
                            }
                    )
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

            Spacer(modifier = Modifier.height(18.dp))

            // 6. Input Field Kata Sandi Baru dengan Toggle Mata
            OutlinedTextField(
                value = newPassword,
                onValueChange = { newPassword = it },
                label = {
                    Text(
                        text = "Kata Sandi Baru*",
                        color = if (isPasswordError) redErrorColor else Color(0xFF757575)
                    )
                },
                singleLine = true,
                isError = isPasswordError,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                trailingIcon = {
                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                        Icon(
                            imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                            contentDescription = if (passwordVisible) "Sembunyikan kata sandi" else "Tampilkan kata sandi",
                            tint = Color.Black
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

            Spacer(modifier = Modifier.height(12.dp))

            // 7. Indikator Kriteria Kata Sandi (Checklist Interaktif)
            // Kriteria 1: Minimum 8 karakter
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                Text(
                    text = if (hasMin8Chars) "✓" else "✕",
                    color = if (hasMin8Chars) successGreenColor else Color(0xFF757575),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Minimum 8 karakter",
                    color = if (hasMin8Chars) Color.Black else Color(0xFF757575),
                    fontSize = 12.5.sp
                )
            }

            // Kriteria 2: Huruf besar, huruf kecil, dan satu angka
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(vertical = 2.dp)
            ) {
                Text(
                    text = if (hasUpperLowerDigit) "✓" else "✕",
                    color = if (hasUpperLowerDigit) successGreenColor else Color(0xFF757575),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Huruf besar, huruf kecil, dan satu angka",
                    color = if (hasUpperLowerDigit) Color.Black else Color(0xFF757575),
                    fontSize = 12.5.sp
                )
            }
        }

        // 8. Tombol Statis di Bagian Paling Bawah (Bebas dari imePadding / Tidak Bergerak-gerak)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp)
                .fillMaxWidth()
        ) {
            // Tombol "Simpan" (Hitam)
            Button(
                onClick = {
                    hasAttemptedSubmit = true
                    if (isFormValid) {
                        onResetSuccess()
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
                    text = "Simpan",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Tombol "Batal" (Outlined Putih Batas Abu-abu)
            OutlinedButton(
                onClick = onCancel,
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
                    text = "Batal",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
