package com.example.shopsey.ui.home

import android.app.Activity
import androidx.annotation.DrawableRes
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
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
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowInsetsControllerCompat
import com.example.shopsey.R
import com.example.shopsey.ui.bag.BagScreen
import com.example.shopsey.ui.profile.ProfileScreen
import com.example.shopsey.ui.search.SearchScreen
import com.example.shopsey.ui.shop.ShopScreen
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

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
 * Halaman Utama (Main Screen) ShopSey.
 *
 * Fitur Utama:
 * 1. Manajemen 5 Tab Navigasi: Home (0), Shop (1), Search (2), Bag (3), Profile (4).
 * 2. Transisi Tema Halus (Dark <-> Light):
 *    - Home menggunakan tema gelap sinematik (Dark Mode)
 *    - Shop, Search, Bag, Profile menggunakan tema terang elegan (Light Mode)
 *    - Saat berganti tab, warna background, navbar 3D, icon, dan status bar bermutasi secara mulus.
 * 3. Section Scrolling (VerticalPager) dengan Seamless Infinite Looping pada tab Home.
 * 4. Floating Navbar: Fixed position di bagian bawah (stay in place) & overflow no scrolling.
 */
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    initialTab: Int = 0,
    onExploreClick: (HomeSectionData) -> Unit = {}
) {
    var selectedNavTab by remember { mutableIntStateOf(initialTab) }
    val isDarkTheme = selectedNavTab == 0

    // Mengontrol warna icon status bar (putih saat tema gelap, hitam saat tema terang)
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as? Activity)?.window ?: return@SideEffect
            WindowInsetsControllerCompat(window, view).apply {
                isAppearanceLightStatusBars = !isDarkTheme
                isAppearanceLightNavigationBars = !isDarkTheme
            }
        }
    }

    // Animasi warna latar belakang saat transisi Dark Mode <-> Light Mode
    val animatedBgColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color.Black else Color(0xFFF7F7F9),
        animationSpec = tween(durationMillis = 350),
        label = "mainBgColor"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(animatedBgColor)
    ) {
        // Transisi halus (Smooth Crossfade) saat berpindah antar halaman/tab
        AnimatedContent(
            targetState = selectedNavTab,
            transitionSpec = {
                fadeIn(animationSpec = tween(350)) togetherWith fadeOut(animationSpec = tween(350))
            },
            label = "tabContentTransition",
            modifier = Modifier.fillMaxSize()
        ) { tab ->
            when (tab) {
                0 -> HomeScreenContent(
                    isActive = selectedNavTab == 0,
                    onExploreClick = onExploreClick
                )
                1 -> ShopScreen(
                    onProductClick = { /* Detail produk */ }
                )
                2 -> SearchScreen()
                3 -> BagScreen()
                4 -> ProfileScreen()
            }
        }

        // Floating Navbar dengan position fixed (stay in place) & overflow no scrolling
        // Memiliki efek transisi tema secara halus (Dark 3D Glass <-> Light White Elevated)
        FloatingNavigationBar(
            selectedTab = selectedNavTab,
            isDarkTheme = isDarkTheme,
            onTabSelected = { selectedNavTab = it },
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .navigationBarsPadding()
                .padding(bottom = 26.dp, start = 20.dp, end = 20.dp)
        )
    }
}

/**
 * Konten Khusus Halaman Home:
 * - 6 Kontainer Section Produk (Nike, Kobe, Adidas, Puma, UA, Jordan)
 * - Section Scrolling (VerticalPager) dengan Infinite Looping
 * - Auto-scrolling & Timer-bar indicator
 */
@Composable
fun HomeScreenContent(
    isActive: Boolean,
    onExploreClick: (HomeSectionData) -> Unit,
    modifier: Modifier = Modifier
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
    val virtualCycleCount = 20_000
    val virtualPageCount = sections.size * virtualCycleCount // 120.000 halaman
    val initialPage = (virtualPageCount / 2) - ((virtualPageCount / 2) % sections.size) // Mulai tepat di Section 1 (Nike)

    val pagerState = rememberPagerState(
        initialPage = initialPage,
        pageCount = { virtualPageCount }
    )

    // Progress animasi untuk Scroll-Timer Indicator (0f -> 1f)
    val timerProgress = remember { Animatable(0f) }
    val autoScrollDurationMs = 4500 // Durasi delay tiap section (4.5 detik)

    // Efek Auto-Scrolling & Timer Bar:
    LaunchedEffect(pagerState, isActive) {
        if (!isActive) {
            timerProgress.snapTo(0f)
            return@LaunchedEffect
        }

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

        // Lapisan Gradien Atas (Soft vignette untuk keterbacaan status bar)
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

        // Lapisan Gradien Bawah Sinematik
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

        // Konten Informasi Produk (Brand Logo, Title, Subtitle, Tombol Explore)
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .navigationBarsPadding()
                .padding(start = 24.dp, end = 24.dp, bottom = 140.dp)
        ) {
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

            Text(
                text = section.title,
                color = Color.White,
                fontSize = 32.sp,
                lineHeight = 38.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = section.subtitle,
                color = Color.White.copy(alpha = 0.92f),
                fontSize = 15.sp,
                lineHeight = 21.sp,
                fontWeight = FontWeight.Normal
            )

            Spacer(modifier = Modifier.height(18.dp))

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
 * Memiliki transisi tema yang halus (Smooth Animation) saat berpindah antara:
 * - Dark Mode (Home)
 * - Light Mode (Shop, Search, Bag, Profile)
 */
@Composable
fun FloatingNavigationBar(
    selectedTab: Int,
    isDarkTheme: Boolean,
    onTabSelected: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    // Animasi perubahan warna tema gelap <-> terang untuk Navbar
    val animBgTop by animateColorAsState(
        targetValue = if (isDarkTheme) Color(0xF50B0B0D) else Color(0xFFFFFFFF),
        animationSpec = tween(350),
        label = "navBgTop"
    )
    val animBgMid by animateColorAsState(
        targetValue = if (isDarkTheme) Color(0xF518181B) else Color(0xFFFAFAFC),
        animationSpec = tween(350),
        label = "navBgMid"
    )
    val animBgBottom by animateColorAsState(
        targetValue = if (isDarkTheme) Color(0xF526262A) else Color(0xFFF2F2F5),
        animationSpec = tween(350),
        label = "navBgBottom"
    )
    val animBorderTop by animateColorAsState(
        targetValue = if (isDarkTheme) Color.White.copy(alpha = 0.24f) else Color.Black.copy(alpha = 0.08f),
        animationSpec = tween(350),
        label = "navBorderTop"
    )
    val animBorderBottom by animateColorAsState(
        targetValue = if (isDarkTheme) Color.White.copy(alpha = 0.03f) else Color.Black.copy(alpha = 0.03f),
        animationSpec = tween(350),
        label = "navBorderBottom"
    )
    val animActiveColor by animateColorAsState(
        targetValue = if (isDarkTheme) Color.White else Color(0xFF111111),
        animationSpec = tween(350),
        label = "navActiveColor"
    )
    val animInactiveColor by animateColorAsState(
        targetValue = Color(0xFF8E8E93),
        animationSpec = tween(350),
        label = "navInactiveColor"
    )
    val animShadowSpot by animateColorAsState(
        targetValue = if (isDarkTheme) Color.Black.copy(alpha = 0.85f) else Color.Black.copy(alpha = 0.12f),
        animationSpec = tween(350),
        label = "navShadowSpot"
    )
    val animShadowAmbient by animateColorAsState(
        targetValue = if (isDarkTheme) Color.Black.copy(alpha = 0.60f) else Color.Black.copy(alpha = 0.06f),
        animationSpec = tween(350),
        label = "navShadowAmbient"
    )

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(68.dp)
            .shadow(
                elevation = if (isDarkTheme) 14.dp else 10.dp,
                shape = RoundedCornerShape(34.dp),
                spotColor = animShadowSpot,
                ambientColor = animShadowAmbient
            )
            .clip(RoundedCornerShape(34.dp))
            .background(
                Brush.verticalGradient(
                    colors = listOf(animBgTop, animBgMid, animBgBottom)
                )
            )
            .border(
                width = 1.dp,
                brush = Brush.verticalGradient(
                    colors = listOf(animBorderTop, animBorderBottom)
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
            // Item 1: Home
            NavBarItem(
                iconRes = R.drawable.ic_nav_home,
                label = "Home",
                isSelected = selectedTab == 0,
                activeColor = animActiveColor,
                inactiveColor = animInactiveColor,
                iconWidth = 18.dp,
                iconHeight = 18.dp,
                onClick = { onTabSelected(0) }
            )

            // Item 2: Shop
            NavBarItem(
                iconRes = R.drawable.icon_shop,
                label = "Shop",
                isSelected = selectedTab == 1,
                activeColor = animActiveColor,
                inactiveColor = animInactiveColor,
                iconWidth = 22.dp,
                iconHeight = 18.dp,
                onClick = { onTabSelected(1) }
            )

            // Item 3: Search
            NavBarItem(
                iconRes = R.drawable.ic_nav_search,
                label = "Search",
                isSelected = selectedTab == 2,
                activeColor = animActiveColor,
                inactiveColor = animInactiveColor,
                iconWidth = 21.dp,
                iconHeight = 20.dp,
                onClick = { onTabSelected(2) }
            )

            // Item 4: Bag
            NavBarItem(
                iconRes = R.drawable.bag_icon,
                label = "Bag",
                isSelected = selectedTab == 3,
                activeColor = animActiveColor,
                inactiveColor = animInactiveColor,
                iconWidth = 22.dp,
                iconHeight = 20.5.dp,
                onClick = { onTabSelected(3) }
            )

            // Item 5: Profile
            NavBarItem(
                iconRes = R.drawable.ic_nav_profile,
                label = "Profile",
                isSelected = selectedTab == 4,
                activeColor = animActiveColor,
                inactiveColor = animInactiveColor,
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
    activeColor: Color,
    inactiveColor: Color,
    iconWidth: androidx.compose.ui.unit.Dp,
    iconHeight: androidx.compose.ui.unit.Dp,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val color = if (isSelected) activeColor else inactiveColor

    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(bounded = false, radius = 24.dp, color = activeColor),
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
