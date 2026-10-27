package com.example.shopsey.ui.auth

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.example.shopsey.R
import kotlinx.coroutines.delay

/**
 * Halaman Formulir Pendaftaran Akun ShopSey (Register Form Screen).
 *
 * Sesuai Spesifikasi:
 * 1. Diakses setelah pengguna mengeklik tombol "Lanjutkan" pada alur Sign Up.
 * 2. Posisi awal semua input field BERSIH & NORMAL (tanpa border merah atau warning "Wajib diisi").
 * 3. Pesan warning "Wajib diisi" dan border merah BARU MUNCUL setelah tombol "Buat Akun" diklik.
 * 4. Notifikasi kode OTP 6 digit dinamis langsung dikirimkan ke bar notifikasi sistem Android
 *    (jendela mock in-app versi lama telah dihapus agar tidak bertabrakan).
 * 5. Validasi Tanggal Lahir:
 *    - Hari: 1 s/d 31 (menyesuaikan jumlah hari bulan terkait & tahun kabisat).
 *    - Bulan: 1 s/d 12 (Januari s/d Desember).
 *    - Tahun: Rentang 18 tahun dari tahun 2026 saat ini (maksimal kelahiran 2008, rentang 1900 s/d 2008).
 * 6. Ukuran form field seukuran 1 baris teks (height 50dp) dengan placeholder berada di dalam box secara vertikal
 *    sehingga tag text/placeholder terlihat seluruhnya tanpa pernah terpotong atau ter-enter ke baris baru.
 */
@Composable
fun RegisterFormScreen(
    userEmail: String,
    onBack: () -> Unit,
    onEditEmail: () -> Unit,
    onCreateAccountSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    // State untuk nilai input
    var otpCode by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var day by remember { mutableStateOf("") }
    var month by remember { mutableStateOf("") }
    var year by remember { mutableStateOf("") }

    var promoChecked by remember { mutableStateOf(false) }
    var privacyChecked by remember { mutableStateOf(false) }

    // State validasi: HANYA true setelah pengguna menekan tombol "Buat Akun"
    var hasAttemptedSubmit by remember { mutableStateOf(false) }

    // State OTP dinamis (berubah acak 6 digit)
    var dynamicOtpCode by remember { mutableStateOf(generateRandom6DigitOtp()) }

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

    // Trigger notifikasi sistem ke Notification Bar Android saat kode OTP dibuat / diperbarui
    LaunchedEffect(dynamicOtpCode) {
        delay(800L)
        triggerSystemNotification(context, dynamicOtpCode)
    }

    val scrollState = rememberScrollState()
    val redErrorColor = Color(0xFFBA1A1A)

    // Validasi Logika Sistem
    val dayInt = day.toIntOrNull()
    val monthInt = month.toIntOrNull()
    val yearInt = year.toIntOrNull()

    // Batas hari sesuai bulan yang dipilih (termasuk cek kabisat untuk Februari)
    val maxDaysInSelectedMonth = when (monthInt) {
        2 -> {
            val y = yearInt ?: 2024
            if ((y % 4 == 0 && y % 100 != 0) || (y % 400 == 0)) 29 else 28
        }
        4, 6, 9, 11 -> 30
        1, 3, 5, 7, 8, 10, 12 -> 31
        else -> 31
    }

    val isOtpValid = otpCode == dynamicOtpCode
    val isFirstNameValid = firstName.trim().isNotBlank()
    val isLastNameValid = lastName.trim().isNotBlank()
    val isDayValid = dayInt != null && dayInt in 1..maxDaysInSelectedMonth
    val isMonthValid = monthInt != null && monthInt in 1..12
    // Minimal 18 tahun dari tahun 2026 => maksimal kelahiran 2008 (rentang 1900 s/d 2008)
    val isYearValid = yearInt != null && yearInt in 1900..2008

    val isFormValid = isOtpValid && isFirstNameValid && isLastNameValid && isDayValid && isMonthValid && isYearValid && privacyChecked

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .navigationBarsPadding()
                .verticalScroll(scrollState)
                .padding(horizontal = 24.dp, vertical = 12.dp)
        ) {
            // 1. Top Bar Transparan dengan Ikon Back
            IconButton(
                onClick = onBack,
                modifier = Modifier.padding(top = 4.dp, bottom = 10.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Kembali",
                    tint = Color.Black,
                    modifier = Modifier.size(26.dp)
                )
            }

            // 2. Ikon Logo ShopSey (Hitam Elegan Minimalis)
            Image(
                painter = painterResource(id = R.drawable.logo3),
                contentDescription = "ShopSey Logo",
                colorFilter = ColorFilter.tint(Color.Black),
                modifier = Modifier.size(36.dp)
            )

            Spacer(modifier = Modifier.height(22.dp))

            // 3. Heading Teks Sesuai Brand Atletik Kelas Dunia
            Text(
                text = "Sekarang daftarkan diri Anda menjadi Anggota ShopSey.",
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

            Spacer(modifier = Modifier.height(24.dp))

            // 5. Input Field Kode OTP (1 Baris Teks, Placeholder Utuh, Tombol Refresh di Kanan)
            val otpHasError = hasAttemptedSubmit && !isOtpValid
            val otpErrorMsg = when {
                otpCode.isBlank() -> "Wajib diisi"
                otpCode.length < 6 -> "Kode harus 6 digit"
                else -> "Kode verifikasi tidak sesuai"
            }

            ShopSeyInputField(
                value = otpCode,
                onValueChange = { otpCode = it },
                placeholder = "Kode*",
                isError = otpHasError,
                errorMessage = otpErrorMsg,
                keyboardType = KeyboardType.Number,
                maxCharLength = 6,
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
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            // 6. Row Nama Depan & Nama Belakang (2 Kolom Seimbang, Placeholder Utuh 1 Baris)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Nama Depan
                val firstNameHasError = hasAttemptedSubmit && !isFirstNameValid
                ShopSeyInputField(
                    value = firstName,
                    onValueChange = { firstName = it },
                    placeholder = "Nama Depan*",
                    isError = firstNameHasError,
                    errorMessage = "Wajib diisi",
                    modifier = Modifier.weight(1f)
                )

                // Nama Belakang
                val lastNameHasError = hasAttemptedSubmit && !isLastNameValid
                ShopSeyInputField(
                    value = lastName,
                    onValueChange = { lastName = it },
                    placeholder = "Nama Belakang*",
                    isError = lastNameHasError,
                    errorMessage = "Wajib diisi",
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // 7. Bagian Tanggal Lahir (Label + 3 Kotak 1 Baris Teks: Hari*, Bulan*, Tahun*)
            Text(
                text = "Tanggal Lahir",
                color = Color(0xFF616161),
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Row Hari, Bulan, Tahun (3 Kolom Berimbang, Label & Placeholder Dijamin Tidak Terpotong)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Hari (1 - 31 disesuaikan bulan & kabisat)
                val dayHasError = hasAttemptedSubmit && !isDayValid
                val dayErrorMsg = if (day.isBlank()) "Wajib diisi" else "1-$maxDaysInSelectedMonth"
                ShopSeyInputField(
                    value = day,
                    onValueChange = { day = it },
                    placeholder = "Hari*",
                    isError = dayHasError,
                    errorMessage = dayErrorMsg,
                    keyboardType = KeyboardType.Number,
                    maxCharLength = 2,
                    modifier = Modifier.weight(1f)
                )

                // Bulan (1 - 12)
                val monthHasError = hasAttemptedSubmit && !isMonthValid
                val monthErrorMsg = if (month.isBlank()) "Wajib diisi" else "1-12"
                ShopSeyInputField(
                    value = month,
                    onValueChange = { month = it },
                    placeholder = "Bulan*",
                    isError = monthHasError,
                    errorMessage = monthErrorMsg,
                    keyboardType = KeyboardType.Number,
                    maxCharLength = 2,
                    modifier = Modifier.weight(1f)
                )

                // Tahun (Maksimal kelahiran 2008 / usia minimal 18 tahun dari 2026)
                val yearHasError = hasAttemptedSubmit && !isYearValid
                val yearErrorMsg = when {
                    year.isBlank() -> "Wajib diisi"
                    yearInt != null && yearInt > 2008 -> "Maks 2008"
                    else -> "Tdk valid"
                }
                ShopSeyInputField(
                    value = year,
                    onValueChange = { year = it },
                    placeholder = "Tahun*",
                    isError = yearHasError,
                    errorMessage = yearErrorMsg,
                    keyboardType = KeyboardType.Number,
                    maxCharLength = 4,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Teks Hadiah Hari Ulang Tahun
            Text(
                text = "Dapatkan Hadiah Anggota ShopSey pada hari ulang tahun Anda.",
                color = Color(0xFF757575),
                fontSize = 12.sp,
                lineHeight = 16.sp
            )

            Spacer(modifier = Modifier.height(20.dp))

            // 8. Checkbox 1: Penerimaan Info Promo (Opsional)
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { promoChecked = !promoChecked }
                    .padding(vertical = 4.dp)
            ) {
                Checkbox(
                    checked = promoChecked,
                    onCheckedChange = { promoChecked = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Black,
                        uncheckedColor = Color.Gray
                    )
                )
                Text(
                    text = "Mendaftarlah untuk menerima email berisi info terbaru dari ShopSey tentang produk, penawaran, dan manfaat Anggota.",
                    color = Color.Black,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    modifier = Modifier.padding(start = 4.dp, top = 10.dp)
                )
            }

            Spacer(modifier = Modifier.height(10.dp))

            // 9. Checkbox 2: Persetujuan Kebijakan Privasi (Wajib Disetujui)
            val privacyHasError = hasAttemptedSubmit && !privacyChecked
            Row(
                verticalAlignment = Alignment.Top,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { privacyChecked = !privacyChecked }
                    .padding(vertical = 4.dp)
            ) {
                Checkbox(
                    checked = privacyChecked,
                    onCheckedChange = { privacyChecked = it },
                    colors = CheckboxDefaults.colors(
                        checkedColor = Color.Black,
                        uncheckedColor = if (privacyHasError) redErrorColor else Color.Gray
                    )
                )
                Column(modifier = Modifier.padding(start = 4.dp, top = 10.dp)) {
                    Text(
                        text = "Saya menyetujui Kebijakan Privasi dan Ketentuan Penggunaan ShopSey.",
                        color = if (privacyHasError) redErrorColor else Color.Black,
                        fontSize = 13.sp,
                        lineHeight = 18.sp
                    )
                    if (privacyHasError) {
                        Text(
                            text = "Wajib disetujui",
                            color = redErrorColor,
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            // 10. Tombol Lebar Penuh "Buat Akun" (Pill Hitam Elegan)
            Button(
                onClick = {
                    hasAttemptedSubmit = true
                    if (isFormValid) {
                        onCreateAccountSuccess()
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
                    text = "Buat Akun",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

/**
 * Komponen Input Field Khusus ShopSey.
 *
 * Dirancang khusus agar:
 * 1. Tinggi tepat seukuran 1 baris teks (height 50dp).
 * 2. Teks label/placeholder selalu tampil di dalam kotak secara vertikal (vertically centered).
 * 3. Tag text/placeholder terlihat seluruhnya tanpa pernah terpotong atau ter-enter ke baris baru.
 * 4. Saat posisi awal (normal), border berwarna abu-abu halus tanpa pesan warning.
 * 5. Saat error (setelah klik Buat Akun), border berubah merah dan pesan bantuan muncul di bawah kotak.
 */
@Composable
fun ShopSeyInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    modifier: Modifier = Modifier,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Text,
    maxCharLength: Int? = null,
    trailingIcon: @Composable (() -> Unit)? = null
) {
    var isFocused by remember { mutableStateOf(false) }
    val focusRequester = remember { FocusRequester() }

    val borderColor = when {
        isError -> Color(0xFFBA1A1A)
        isFocused -> Color.Black
        else -> Color(0xFFCCCCCC)
    }

    Column(modifier = modifier) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = null
                ) {
                    focusRequester.requestFocus()
                }
                .border(
                    width = if (isFocused || isError) 1.5.dp else 1.dp,
                    color = borderColor,
                    shape = RoundedCornerShape(8.dp)
                )
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(horizontal = if (trailingIcon != null) 12.dp else 10.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = Color(0xFF757575),
                            fontSize = 13.5.sp,
                            maxLines = 1,
                            softWrap = false,
                            overflow = TextOverflow.Clip
                        )
                    }
                    BasicTextField(
                        value = value,
                        onValueChange = { input ->
                            val filtered = if (keyboardType == KeyboardType.Number) {
                                input.filter { it.isDigit() }
                            } else {
                                input
                            }
                            if (maxCharLength == null || filtered.length <= maxCharLength) {
                                onValueChange(filtered)
                            }
                        },
                        singleLine = true,
                        maxLines = 1,
                        textStyle = TextStyle(
                            color = Color.Black,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Normal
                        ),
                        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
                        cursorBrush = SolidColor(Color.Black),
                        modifier = Modifier
                            .fillMaxWidth()
                            .focusRequester(focusRequester)
                            .onFocusChanged { isFocused = it.isFocused }
                    )
                }
                if (trailingIcon != null) {
                    trailingIcon()
                }
            }
        }
        if (isError && !errorMessage.isNullOrBlank()) {
            Text(
                text = errorMessage,
                color = Color(0xFFBA1A1A),
                fontSize = 11.sp,
                lineHeight = 14.sp,
                maxLines = 1,
                softWrap = false,
                modifier = Modifier.padding(start = 4.dp, top = 3.dp)
            )
        }
    }
}
