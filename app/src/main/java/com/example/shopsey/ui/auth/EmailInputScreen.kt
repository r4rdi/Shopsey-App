package com.example.shopsey.ui.auth

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
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsey.R

// Helper validasi email standar
fun isValidEmail(email: String): Boolean {
    val trimmed = email.trim()
    return trimmed.isNotEmpty() && android.util.Patterns.EMAIL_ADDRESS.matcher(trimmed).matches()
}

@Composable
fun EmailInputScreen(
    isSignUp: Boolean = false,
    onBack: () -> Unit,
    onContinue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var email by remember { mutableStateOf("") }
    var hasAttemptedSubmit by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val isEmailError = hasAttemptedSubmit && !isValidEmail(email)
    val redErrorColor = Color(0xFFBA1A1A)

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Konten Form Bagian Atas
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

            // 3. Teks Heading Disesuaikan Antara Sign Up dan Sign In / Log In
            Text(
                text = if (isSignUp) {
                    "Masukkan email Anda untuk mendaftar."
                } else {
                    "Masukkan email Anda untuk masuk."
                },
                color = Color.Black,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Fitur Bahasa: "Indonesia" dan "Ubah"
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Text(
                    text = "Indonesia",
                    color = Color.Black,
                    fontSize = 14.sp
                )
                Spacer(modifier = Modifier.size(8.dp))
                Text(
                    text = "Ubah",
                    color = Color(0xFF757575),
                    fontSize = 14.sp,
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { /* Trigger ganti bahasa */ }
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            // 5. OutlinedTextField Input Email dengan Validasi Format Email
            OutlinedTextField(
                value = email,
                onValueChange = {
                    email = it
                },
                label = {
                    Text(
                        text = "Email*",
                        color = if (isEmailError) redErrorColor else Color(0xFF757575)
                    )
                },
                singleLine = true,
                isError = isEmailError,
                shape = RoundedCornerShape(8.dp),
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Email,
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                        hasAttemptedSubmit = true
                        if (isValidEmail(email)) {
                            onContinue(email.trim())
                        }
                    }
                ),
                trailingIcon = {
                    if (isEmailError) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Format email tidak valid",
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

            // Pesan Error "Format email tidak valid"
            if (isEmailError) {
                Text(
                    text = "Format email tidak valid",
                    color = redErrorColor,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 6.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 6. Disclaimer Privasi
            Text(
                text = "Dengan melanjutkan, saya menyetujui Kebijakan Privasi dan Ketentuan Penggunaan ShopSey.",
                color = Color(0xFF757575),
                fontSize = 12.sp,
                lineHeight = 17.sp
            )
        }

        // 7. Tombol Statis di Bagian Bawah (Tidak Bergerak / Resizing Mengikuti Keyboard)
        Button(
            onClick = {
                hasAttemptedSubmit = true
                if (isValidEmail(email)) {
                    focusManager.clearFocus()
                    onContinue(email.trim())
                }
            },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(horizontal = 24.dp, vertical = 20.dp)
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(percent = 50),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Black,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "Lanjutkan",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }
    }
}
