package com.example.shopsey.ui.auth

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
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsey.R

/**
 * Halaman Masuk Menggunakan Kata Sandi ("Apa kata sandi Anda?")
 *
 * Diakses saat pengguna menekan "Gunakan Kata Sandi" pada halaman OtpVerificationScreen.
 * Fitur:
 * 1. Subtitle email dengan tombol Edit.
 * 2. Input Kata Sandi dengan tombol toggle visibilitas (ikon mata).
 * 3. Link "Lupa kata sandi?" yang menuju ke halaman reset kata sandi baru.
 * 4. Tombol statis "Masuk" (Hitam) dan "Gunakan Kode Satu Kali" (Outlined Putih).
 */
@Composable
fun PasswordInputScreen(
    userEmail: String,
    onBack: () -> Unit,
    onEditEmail: () -> Unit,
    onLoginSuccess: () -> Unit,
    onNavigateToOtp: () -> Unit,
    onForgotPassword: () -> Unit,
    modifier: Modifier = Modifier
) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var hasAttemptedSubmit by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val redErrorColor = Color(0xFFBA1A1A)
    val isPasswordEmpty = password.trim().isEmpty()
    val isPasswordError = hasAttemptedSubmit && isPasswordEmpty

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
                text = "Apa kata sandi Anda?",
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

            // 5. OutlinedTextField Input Kata Sandi dengan Toggle Ikon Mata
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = {
                    Text(
                        text = "Kata Sandi*",
                        color = if (isPasswordError) redErrorColor else Color(0xFF757575)
                    )
                },
                singleLine = true,
                isError = isPasswordError,
                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Password,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        hasAttemptedSubmit = true
                        if (password.trim().isNotEmpty()) {
                            onLoginSuccess()
                        }
                    }
                ),
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

            if (isPasswordError) {
                Text(
                    text = "Wajib diisi",
                    color = redErrorColor,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 6.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 6. Link "Lupa kata sandi?"
            Text(
                text = "Lupa kata sandi?",
                color = Color(0xFF616161),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                textDecoration = TextDecoration.Underline,
                modifier = Modifier.clickable { onForgotPassword() }
            )
        }

        // 7. Tombol Statis di Bagian Paling Bawah (Tidak Bergerak / Resizing Mengikuti Keyboard)
        Column(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp)
                .fillMaxWidth()
        ) {
            // Tombol "Masuk" (Hitam)
            Button(
                onClick = {
                    hasAttemptedSubmit = true
                    if (password.trim().isNotEmpty()) {
                        focusManager.clearFocus()
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

            // Tombol "Gunakan Kode Satu Kali" (Outlined Putih Batas Abu-abu)
            OutlinedButton(
                onClick = onNavigateToOtp,
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
                    text = "Gunakan Kode Satu Kali",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }
    }
}
