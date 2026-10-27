package com.example.shopsey.ui.auth

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
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
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
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsey.R

/**
 * Halaman Pembuatan Kata Sandi Baru pada alur Sign Up.
 *
 * Diakses setelah pengguna memasukkan email dan mengeklik tombol "Lanjutkan" pada Sign Up
 * (sebelum mengisi formulir data diri seperti nama dan tanggal lahir).
 *
 * Fitur:
 * 1. Subtitle email dengan tombol Edit.
 * 2. Input Kata Sandi Baru dengan toggle mata (Show/Hide Password).
 * 3. Indikator checklist kriteria kata sandi interaktif:
 *    - Minimum 8 karakter
 *    - Huruf besar, huruf kecil, dan satu angka
 * 4. Tombol statis "Lanjutkan" (Hitam) di bagian paling bawah.
 */
@Composable
fun CreatePasswordScreen(
    userEmail: String,
    onBack: () -> Unit,
    onEditEmail: () -> Unit,
    onContinue: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var hasAttemptedSubmit by remember { mutableStateOf(false) }
    val focusManager = LocalFocusManager.current

    val redErrorColor = Color(0xFFBA1A1A)
    val successGreenColor = Color(0xFF16A34A)

    // Validasi Kriteria Kata Sandi
    val hasMin8Chars = password.length >= 8
    val hasUpperLowerDigit = password.any { it.isUpperCase() } &&
            password.any { it.isLowerCase() } &&
            password.any { it.isDigit() }
    val isPasswordValid = hasMin8Chars && hasUpperLowerDigit
    val isPasswordError = hasAttemptedSubmit && !isPasswordValid

    val passwordErrorMsg = when {
        password.isBlank() -> "Wajib diisi"
        !hasMin8Chars -> "Kata sandi minimal 8 karakter"
        else -> "Gunakan huruf besar, huruf kecil, dan angka"
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
                text = "Sekarang, buat kata sandi untuk akun Anda.",
                color = Color.Black,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(14.dp))

            // 4. Subtitle Email & Tombol Edit
            Column {
                Text(
                    text = "Kami telah menggunakan email",
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
                        if (isPasswordValid) {
                            onContinue(password)
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
                    text = passwordErrorMsg,
                    color = redErrorColor,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(start = 6.dp, top = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // 6. Checklist Kriteria Kata Sandi Interaktif
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

        // 7. Tombol Statis di Bagian Paling Bawah (Bebas dari imePadding / Animasi Tersendat)
        Button(
            onClick = {
                hasAttemptedSubmit = true
                if (isPasswordValid) {
                    focusManager.clearFocus()
                    onContinue(password)
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
