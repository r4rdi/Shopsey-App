package com.example.shopsey.ui.home

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationEndReason
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.pager.VerticalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsey.R

/**
 * Model data untuk setiap Section pada Home Page.
 */
data class HomeSectionData(
    val id: String,
    val brandName: String,
    val title: String,
    val subtitle: String,
    val bgRes: Int,
    val iconRes: Int
)

/**
 * Halaman Utama (Home Page) ShopSey.
 *
 * Fitur Utama:
 * 1. Section Scrolling (VerticalPager): Halaman secara otomatis berpindah dan mengunci
 *    (snap) secara halus ke section penuh berikutnya saat digulirkan.
 * 2. 6 Kontainer Section Produk:
 *    - 1. Produk Nike (homebg1.png & icon_nike.png)
 *    - 2. Produk Sheath Kobe (homebg2.png & icon_sheathkobe.png)
 *    - 3. Produk Adidas (homebg3.png & icon_adidas.png)
 *    - 4. Produk Puma (homebg4.png & icon_puma.png)
 *    - 5. Produk Under Armour (homebg5.png & icon_underarmour.png)
 *    - 6. Produk Jordan (homebg6.png & icon_jordan.png)
 * 3. Floating Navbar: Fixed position di bagian bawah (stay in place), overflow no scrolling,
 *    berisi 5 item: Home, Shop, Search, Bag, dan Profile.
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    onExploreClick: (HomeSectionData) -> Unit = {}
) {
    // 6 Kontainer / Section sesuai spesifikasi
    val sections = remember {
        listOf(
            HomeSectionData(
                id = "nike",
                brandName = "Nike",
                title = "Nike United",
                subtitle = "Created by athletes.",
                bgRes = R.drawable.homebg1,
                iconRes = R.drawable.icon_nike
            ),
            HomeSectionData(
                id = "sheathkobe",
                brandName = "Sheath Kobe",
                title = "Kobe Protro",
                subtitle = "Passion, focus, and relentless work ethic.",
                bgRes = R.drawable.homebg2,
                iconRes = R.drawable.icon_sheathkobe
            ),
            HomeSectionData(
                id = "adidas",
                brandName = "Adidas",
                title = "Predator Elite",
                subtitle = "Speed, precision, and surgical touch.",
                bgRes = R.drawable.homebg3,
                iconRes = R.drawable.icon_adidas
            ),
            HomeSectionData(
                id = "puma",
                brandName = "Puma",
                title = "Puma Future 7",
                subtitle = "Play without boundaries. Agility and pure energy.",
                bgRes = R.drawable.homebg4,
                iconRes = R.drawable.icon_puma
            ),
            HomeSectionData(
                id = "underarmour",
                brandName = "Under Armour",
                title = "Curry Flow 11",
                subtitle = "Change the game for good. Unstoppable grip.",
                bgRes = R.drawable.homebg5,
                iconRes = R.drawable.icon_underarmour
            ),
            HomeSectionData(
                id = "jordan",
                brandName = "Jordan",
                title = "Jordan 6 Sneaker",
                subtitle = "Defy gravity. An iconic silhouette born for flight.",
                bgRes = R.drawable.homebg6,
                iconRes = R.drawable.icon_jordan
            )
        )
    }

    // Konfigurasi Infinite Scrolling (Seamless Carousel):
    // Menggunakan virtual page count agar pengguna maupun mekanisme auto-scroll
    // dapat melakukan scroll ke bawah secara terus menerus (infinite loop) dari Section 6 (Jordan) kembali ke Section 1 (Nike).
    val virtualCycleCount = 20_000
    val virtualPageCount = sections.size * virtualCycleCount // 120.000 halaman
    val initialPage = (virtualPageCount / 2) - ((virtualPageCount / 2) % sections.size) // Mulai tepat di Section 1 (Nike)

    // Pager state untuk Section Scrolling (mengunci ke section berikutnya dengan snap halus)
    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { virtualPageCount }
    )

    // Tab navigasi aktif pada floating navbar (0 = Home)
    var selectedNavTab by remember { mutableIntStateOf(0) }

    // Progress animasi untuk Scroll-Timer Indicator (0f -> 1f)
    val timerProgress = remember { Animatable(0f) }
    val autoScrollDurationMs = 4500 // Durasi delay tiap section (4.5 detik)

    // Efek Auto-Scrolling & Timer Bar:
    // Menggunakan coroutine loop terisolasi yang stabil (tidak ter-cancel di tengah jalan saat melewati 50% scroll)
    // sehingga transisi scroll berjalan penuh (Full One Page Section Scroll) tanpa macet/stuck di tengah.
    LaunchedEffect(pagerState) {
        while (isActive) {
            // Tunggu hingga pager benar-benar diam (settled) dan tidak sedang digeser manual oleh pengguna
            if (pagerState.isScrollInProgress) {
                snapshotFlow { pagerState.isScrollInProgress }
                    .filter { !it }
                    .first()
            }

            // Reset indikator timer ke 0 untuk section saat ini
            timerProgress.snapTo(0f)

            // Jalankan animasi pengisian bar timer (0f -> 1f)
            // Sekaligus memantau apakah pengguna mulai menggeser layar secara manual
            var userInterrupted = false
            coroutineScope {
                val timerJob = launch {
                    timerProgress.animateTo(
                        targetValue = 1f,
                        animationSpec = tween(durationMillis = autoScrollDurationMs, easing = LinearEasing)
                    )
                }

                val touchWatcherJob = launch {
                    snapshotFlow { pagerState.isScrollInProgress }
                        .filter { it }
                        .first()
                    userInterrupted = true
                    timerJob.cancel()
                }

                timerJob.join()
                touchWatcherJob.cancel()
            }

            // Jika durasi timer penuh tanpa disentuh pengguna, lakukan Full Section Scroll ke section berikutnya
            if (!userInterrupted) {
                try {
                    pagerState.animateScrollToPage(
                        page = pagerState.currentPage + 1,
                        animationSpec = tween(durationMillis = 650, easing = FastOutSlowInEasing)
                    )
                } catch (e: Exception) {
                    if (e is CancellationException && !isActive) {
                        throw e
                    }
                }
            } else {
                // Pengguna menggeser layar manual, kosongkan timer bar
                timerProgress.snapTo(0f)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.Black)
    ) {
        // 1. Mekanisme Section Scrolling menggunakan VerticalPager dengan Infinite Looping
        VerticalPager(
            state = pagerState,
            key = { pageIndex -> pageIndex },
            modifier = Modifier.fillMaxSize()
        ) { pageIndex ->
            val actualIndex = (pageIndex % sections.size + sections.size) % sections.size
            val section = sections[actualIndex]
            HomeSectionItem(
                section = section,
                onExploreClick = { onExploreClick(section) }
            )
        }

        // 2. Scroll-Timer Indicator (Batang progress penanda waktu jalan auto-scrolling)
        // Diletakkan di ujung kanan bawah (sejajar dengan area tombol Explore)
        val currentSectionIndex = (pagerState.currentPage % sections.size + sections.size) % sections.size
        val currentDisplayNumber = (currentSectionIndex + 1).toString().padStart(2, '0')
        val totalSectionsNumber = sections.size.toString().padStart(2, '0')

        Column(
            horizontalAlignment = Alignment.End,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .navigationBarsPadding()
                .padding(end = 26.dp, bottom = 152.dp)
        ) {
            // Label penanda nomor section (misal: 01 / 06)
            Text(
                text = "$currentDisplayNumber / $totalSectionsNumber",
                color = Color.White.copy(alpha = 0.85f),
                fontSize = 11.sp,
                fontWeight = FontWeight.SemiBold,
                letterSpacing = 0.5.sp
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Batang Timer Progress Bar (Track & Fill)
            Box(
                modifier = Modifier
                    .width(62.dp)
                    .height(3.5.dp)
                    .clip(RoundedCornerShape(50))
                    .background(Color.White.copy(alpha = 0.25f))
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .fillMaxWidth(fraction = timerProgress.value.coerceIn(0f, 1f))
                        .clip(RoundedCornerShape(50))
                        .background(Color.White)
                )
            }
        }

        // 3. Floating Navbar dengan position fixed (stay in place) & overflow no scrolling
        FloatingNavigationBar(
            selectedTab = selectedNavTab,
            onTabSelected = { selectedNavTab = it },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 26.dp, start = 20.dp, end = 20.dp)
        )
    }
}

/**
 * Komponen Kontainer Section Tunggal (Full Screen)
 */
@Composable
private fun HomeSectionItem(
    section: HomeSectionData,
    onExploreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize()
    ) {
        // Gambar Latar Belakang Penuh
        Image(
            painter = painterResource(id = section.bgRes),
            contentDescription = section.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Lapisan Gradien Atas (Soft vignette untuk keterbacaan area status bar)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
                .align(Alignment.TopCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.45f),
                            Color.Transparent
                        )
                    )
                )
        )

        // Lapisan Gradien Bawah Sinematik (Agar teks, icon dan tombol Explore terbaca sangat jelas)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.25f),
                            Color.Black.copy(alpha = 0.75f),
                            Color.Black.copy(alpha = 0.95f)
                        )
                    )
                )
        )

        // Konten Informasi Produk (Brand Icon, Title, Subtitle, Tombol Explore)
        // Diberikan margin bottom yang proporsional (140dp) menyesuaikan posisi navbar yang dinaikkan
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .navigationBarsPadding()
                .padding(start = 24.dp, end = 24.dp, bottom = 140.dp)
        ) {
            // Icon Brand Produk (Di-scale lebih besar dan proporsional untuk seluruh brand)
            Image(
                painter = painterResource(id = section.iconRes),
                contentDescription = "${section.brandName} Logo",
                modifier = Modifier
                    .height(if (section.id == "sheathkobe" || section.id == "jordan") 44.dp else 36.dp)
                    .widthIn(max = 85.dp),
                contentScale = ContentScale.Fit,
                alignment = Alignment.CenterStart
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Judul Produk (Bold & High Contrast)
            Text(
                text = section.title,
                color = Color.White,
                fontSize = 32.sp,
                lineHeight = 38.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Subtitle Deskripsi
            Text(
                text = section.subtitle,
                color = Color.White.copy(alpha = 0.92f),
                fontSize = 15.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Tombol Explore (Kapsul elegan transparan dengan border putih, persis referensi)
            Surface(
                onClick = onExploreClick,
                shape = RoundedCornerShape(50),
                color = Color.Black.copy(alpha = 0.35f),
                border = BorderStroke(1.2.dp, Color.White),
                contentColor = Color.White
            ) {
                Text(
                    text = "Explore",
                    color = Color.White,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 28.dp, vertical = 10.dp)
                )
            }
        }
    }
}

/**
 * Floating Navigation Bar (Fixed stay in place & overflow no scrolling).
 * Berisi 5 item:
 * 1. Home (Icon dari referensi XML pengguna)
 * 2. Shop
 * 3. Search
 * 4. Bag
 * 5. Profile
 */
@Composable
private fun FloatingNavigationBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Container Navbar Mengambang dengan gradien warna transisi hitam menuju abu-abu (3D effect)
    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(68.dp)
            .shadow(
                elevation = 14.dp,
                shape = RoundedCornerShape(34.dp),
                spotColor = Color.Black.copy(alpha = 0.85f),
                ambientColor = Color.Black.copy(alpha = 0.60f)
            )
            .clip(RoundedCornerShape(34.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xF50B0B0D), // Hitam pekat di bagian atas
                        Color(0xF518181B), // Transisi gelap halus di tengah
                        Color(0xF526262A)  // Abu-abu tipis/sepintas di bagian bawah untuk efek 3D
                    )
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.White.copy(alpha = 0.24f), // Highlight refleksi cahaya di bibir atas (3D rim)
                        Color.White.copy(alpha = 0.08f), // Sisi samping
                        Color.White.copy(alpha = 0.03f)  // Lembut di bagian bawah
                    )
                ),
                shape = RoundedCornerShape(34.dp)
            )
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 10.dp, end = 10.dp, top = 6.dp, bottom = 2.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Item 1: Home (18dp x 18dp sesuai kode pengguna)
            NavBarItem(
                iconRes = R.drawable.ic_nav_home,
                label = "Home",
                isSelected = selectedTab == 0,
                iconWidth = 18.dp,
                iconHeight = 18.dp,
                onClick = { onTabSelected(0) }
            )

            // Item 2: Shop (icon_shop.png sepatu)
            NavBarItem(
                iconRes = R.drawable.icon_shop,
                label = "Shop",
                isSelected = selectedTab == 1,
                iconWidth = 22.dp,
                iconHeight = 18.dp,
                onClick = { onTabSelected(1) }
            )

            // Item 3: Search (magnifying glass)
            NavBarItem(
                iconRes = R.drawable.ic_nav_search,
                label = "Search",
                isSelected = selectedTab == 2,
                iconWidth = 21.dp,
                iconHeight = 20.dp,
                onClick = { onTabSelected(2) }
            )

            // Item 4: Bag (bag_icon.xml, 22dp x 20.5dp)
            NavBarItem(
                iconRes = R.drawable.bag_icon,
                label = "Bag",
                isSelected = selectedTab == 3,
                iconWidth = 22.dp,
                iconHeight = 20.5.dp,
                onClick = { onTabSelected(3) }
            )

            // Item 5: Profile (frame_1 avatar, 20dp x 22dp)
            NavBarItem(
                iconRes = R.drawable.ic_nav_profile,
                label = "Profile",
                isSelected = selectedTab == 4,
                iconWidth = 20.dp,
                iconHeight = 22.dp,
                onClick = { onTabSelected(4) }
            )
        }
    }
}

/**
 * Komponen Item Individu pada Floating Navbar
 */
@Composable
private fun NavBarItem(
    @DrawableRes iconRes: Int,
    label: String,
    isSelected: Boolean,
    iconWidth: androidx.compose.ui.unit.Dp,
    iconHeight: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val activeColor = Color.White
    val inactiveColor = Color(0xFF8E8E93)
    val color = if (isSelected) activeColor else inactiveColor

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, radius = 24.dp, color = Color.White),
                onClick = onClick
            )
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            painter = painterResource(id = iconRes),
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(width = iconWidth, height = iconHeight)
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = label,
            color = color,
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}
