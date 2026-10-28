package com.example.shopsey.ui.shop

import androidx.annotation.DrawableRes
import androidx.compose.animation.core.animateDpAsState
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
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Shuffle
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsey.R

/**
 * Model data Hero Carousel pada Shop Page
 */
data class ShopHeroBanner(
    val id: String,
    val title: String,
    @param:DrawableRes val imageRes: Int
)

/**
 * Model data Kategori Mozaik
 */
data class ShopCategory(
    val id: String,
    val title: String,
    @param:DrawableRes val imageRes: Int
)

/**
 * Model data Produk Lifestyle Picks
 */
data class ShopProduct(
    val id: String,
    val name: String,
    val price: String,
    val originalPrice: String? = null,
    @param:DrawableRes val imageRes: Int
)

/**
 * Halaman Shop (Shop Page) dengan tema Terang (Light Mode).
 * Sesuai referensi desain:
 * 1. Dropdown filter gender/kategori (Men ⌄)
 * 2. Hero Carousel Horizontal Pager (dengan indikator dot dinamis)
 * 3. Mozaik Kategori 2-Kolom (New & Featured, Shoes, Clothing, Accessories, Sport, Sale)
 * 4. Section "Jump Back In" (recently viewed) dengan tombol Clear
 * 5. Section "New for You" berisi Bento Mosaic Cards (Sneakers of the Week, Performance Gear, Skateboarding)
 * 6. Section "Lifestyle Picks" berisi 3-kolom grid produk dengan harga dan diskon
 */
@Composable
fun ShopScreen(
    modifier: Modifier = Modifier,
    onProductClick: (String) -> Unit = {}
) {
    val scrollState = rememberScrollState()

    // Pilihan Gender Filter (Men, Women, Kids)
    var selectedGender by remember { mutableStateOf("Men") }
    var isGenderDropdownExpanded by remember { mutableStateOf(false) }

    // Data Hero Carousel Banner (5 slide)
    val heroBanners = remember {
        listOf(
            ShopHeroBanner(
                id = "alphafly",
                title = "Coming Soon: Alphafly 4 'Prototype'",
                imageRes = R.drawable.shop1
            ),
            ShopHeroBanner(
                id = "phantom",
                title = "New Release: Phantom GX Elite",
                imageRes = R.drawable.shop2
            ),
            ShopHeroBanner(
                id = "mercurial",
                title = "Precision Speed: Mercurial Superfly",
                imageRes = R.drawable.shop3
            ),
            ShopHeroBanner(
                id = "f50",
                title = "Fast Reborn: Adidas F50 Elite",
                imageRes = R.drawable.shop6
            ),
            ShopHeroBanner(
                id = "jordan_cleat",
                title = "Flight on Pitch: Jordan Vapor Edge",
                imageRes = R.drawable.shop8
            )
        )
    }
    val heroPagerState = rememberPagerState(pageCount = { heroBanners.size })

    // Data Mozaik Kategori (6 Kategori, 2 Kolom)
    val categories = remember {
        listOf(
            ShopCategory("new_featured", "New & Featured", R.drawable.shop5),
            ShopCategory("shoes", "Shoes", R.drawable.shop4),
            ShopCategory("clothing", "Clothing", R.drawable.shop11),
            ShopCategory("accessories", "Accessories", R.drawable.shop10),
            ShopCategory("sport", "Sport", R.drawable.shop12),
            ShopCategory("sale", "Sale", R.drawable.shop9)
        )
    }

    // Data Jump Back In
    var jumpBackInList by remember {
        mutableStateOf(
            listOf(
                R.drawable.shop16,
                R.drawable.shop15,
                R.drawable.shop17,
                R.drawable.shop14
            )
        )
    }

    // Data Produk Lifestyle Picks (3 Kolom Grid)
    val lifestylePicks = remember {
        listOf(
            ShopProduct("p1", "Nike Offcourt Adjust", "Rp609.000", null, R.drawable.shop15),
            ShopProduct("p2", "Nike Calm 2.0", "Rp709.000", null, R.drawable.shop16),
            ShopProduct("p3", "Nike Sportswear Club", "Rp919.200", "Rp1.149.000", R.drawable.shop14),
            ShopProduct("p4", "Nike Sportswear", "Rp529.000", null, R.drawable.shop11),
            ShopProduct("p5", "Nike Air Max Plus", "Rp2.389.000", null, R.drawable.shop4),
            ShopProduct("p6", "Nike ReactX Rejuven8", "Rp889.000", null, R.drawable.shop17),
            ShopProduct("p7", "Under Armour HOVR", "Rp1.499.000", null, R.drawable.shop12),
            ShopProduct("p8", "Curry Flow 11 Elite", "Rp2.199.000", null, R.drawable.shop13),
            ShopProduct("p9", "Jordan 11 Retro", "Rp3.199.000", null, R.drawable.shop8)
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F9)) // Background warna terang khas Shop page
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // ----------------------------------------------------
            // 1. TOP BAR: Dropdown Kategori / Gender (Men ⌄)
            // ----------------------------------------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Surface(
                    onClick = { isGenderDropdownExpanded = !isGenderDropdownExpanded },
                    shape = RoundedCornerShape(50),
                    color = Color.White,
                    shadowElevation = 2.dp,
                    border = BorderStroke(1.dp, Color(0xFFE8E8ED))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 18.dp, vertical = 9.dp)
                    ) {
                        Text(
                            text = selectedGender,
                            color = Color(0xFF111111),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (isGenderDropdownExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = "Pilih Kategori",
                            tint = Color(0xFF111111),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                MaterialTheme(
                    shapes = MaterialTheme.shapes.copy(extraSmall = RoundedCornerShape(18.dp))
                ) {
                    DropdownMenu(
                        expanded = isGenderDropdownExpanded,
                        onDismissRequest = { isGenderDropdownExpanded = false },
                        modifier = Modifier
                            .width(225.dp)
                            .background(Color(0xFFEEEEF2))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        // Section 1: Genders (Women, Men, Kids)
                        listOf("Women", "Men", "Kids").forEach { genderOption ->
                            val isSelected = selectedGender == genderOption
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) Color(0xFFD6D6DE) else Color.Transparent)
                                    .clickable {
                                        selectedGender = genderOption
                                        isGenderDropdownExpanded = false
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = genderOption,
                                        color = Color(0xFF111111),
                                        fontSize = 15.sp,
                                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                                    )
                                    if (isSelected) {
                                        Icon(
                                            imageVector = Icons.Default.Check,
                                            contentDescription = "Selected",
                                            tint = Color(0xFF111111),
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }

                        HorizontalDivider(
                            color = Color(0xFFDCDCE0),
                            thickness = 0.8.dp,
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp)
                        )

                        // Section 2: Jordan Brand
                        val isJordanSelected = selectedGender == "Jordan"
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isJordanSelected) Color(0xFFD6D6DE) else Color.Transparent)
                                .clickable {
                                    selectedGender = "Jordan"
                                    isGenderDropdownExpanded = false
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(32.dp)
                                    .clip(CircleShape)
                                    .background(Color.Black),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_jordan_jumpman),
                                    contentDescription = "Jordan",
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = "Jordan",
                                color = Color(0xFF111111),
                                fontSize = 15.sp,
                                fontWeight = if (isJordanSelected) FontWeight.SemiBold else FontWeight.Medium
                            )
                        }

                        HorizontalDivider(
                            color = Color(0xFFDCDCE0),
                            thickness = 0.8.dp,
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp)
                        )

                        // Section 3: Sports & Activities
                        listOf(
                            "Running",
                            "Basketball",
                            "Tennis & Pickleball",
                            "Football",
                            "Gym & Training"
                        ).forEach { sport ->
                            val isSelected = selectedGender == sport
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(if (isSelected) Color(0xFFD6D6DE) else Color.Transparent)
                                    .clickable {
                                        selectedGender = sport
                                        isGenderDropdownExpanded = false
                                    }
                                    .padding(horizontal = 12.dp, vertical = 8.dp)
                            ) {
                                Text(
                                    text = sport,
                                    color = Color(0xFF111111),
                                    fontSize = 15.sp,
                                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                                )
                            }
                        }

                        HorizontalDivider(
                            color = Color(0xFFDCDCE0),
                            thickness = 0.8.dp,
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp)
                        )

                        // Section 4: More Sports
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .clickable {
                                    selectedGender = "More Sports"
                                    isGenderDropdownExpanded = false
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "More Sports",
                                color = Color(0xFF111111),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Medium
                            )
                            Icon(
                                imageVector = Icons.Default.ChevronRight,
                                contentDescription = "More",
                                tint = Color(0xFF555555),
                                modifier = Modifier.size(18.dp)
                            )
                        }

                        HorizontalDivider(
                            color = Color(0xFFDCDCE0),
                            thickness = 0.8.dp,
                            modifier = Modifier.padding(vertical = 4.dp, horizontal = 4.dp)
                        )

                        // Section 5: All Conditions Gear
                        val isAcgSelected = selectedGender == "All Conditions Gear"
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isAcgSelected) Color(0xFFD6D6DE) else Color.Transparent)
                                .clickable {
                                    selectedGender = "All Conditions Gear"
                                    isGenderDropdownExpanded = false
                                }
                                .padding(horizontal = 12.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = "All Conditions Gear",
                                color = Color(0xFF111111),
                                fontSize = 15.sp,
                                fontWeight = if (isAcgSelected) FontWeight.SemiBold else FontWeight.Medium
                            )
                        }
                    }
                }
            }

            // ----------------------------------------------------
            // 2. HERO CAROUSEL: Horizontal Pager Card
            // ----------------------------------------------------
            HorizontalPager(
                state = heroPagerState,
                contentPadding = PaddingValues(horizontal = 16.dp),
                pageSpacing = 12.dp,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(380.dp)
            ) { page ->
                val banner = heroBanners[page]
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(24.dp))
                        .clickable { onProductClick(banner.id) }
                ) {
                    Image(
                        painter = painterResource(id = banner.imageRes),
                        contentDescription = banner.title,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Gradient hitam sinematik di bagian bawah banner
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(180.dp)
                            .align(Alignment.BottomCenter)
                            .background(
                                Brush.verticalGradient(
                                    colors = listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.40f),
                                        Color.Black.copy(alpha = 0.85f)
                                    )
                                )
                            )
                    )

                    // Judul Banner di pojok kiri bawah
                    Text(
                        text = banner.title,
                        color = Color.White,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        lineHeight = 24.sp,
                        modifier = Modifier
                            .align(Alignment.BottomStart)
                            .padding(horizontal = 20.dp, vertical = 22.dp)
                    )
                }
            }

            // Indikator Pager Dots (Active pill + Inactive dots)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp, bottom = 22.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(heroBanners.size) { index ->
                    val isSelected = heroPagerState.currentPage == index
                    val dotWidth by animateDpAsState(
                        targetValue = if (isSelected) 20.dp else 6.dp,
                        animationSpec = tween(250),
                        label = "dotWidth"
                    )
                    val dotColor = if (isSelected) Color(0xFF222222) else Color(0xFFD1D1D6)

                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.dp)
                            .height(6.dp)
                            .width(dotWidth)
                            .clip(RoundedCornerShape(50))
                            .background(dotColor)
                    )
                }
            }

            // ----------------------------------------------------
            // 3. MOZAIK KATEGORI: 2 Kolom x 3 Baris (6 Bento Cards)
            // ----------------------------------------------------
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                for (rowIndex in 0 until categories.size step 2) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val item1 = categories[rowIndex]
                        CategoryMosaicCard(
                            item = item1,
                            onClick = { onProductClick(item1.id) },
                            modifier = Modifier.weight(1f)
                        )

                        if (rowIndex + 1 < categories.size) {
                            val item2 = categories[rowIndex + 1]
                            CategoryMosaicCard(
                                item = item2,
                                onClick = { onProductClick(item2.id) },
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(34.dp))

            // ----------------------------------------------------
            // 4. SECTION: "Jump Back In" (Recently Viewed)
            // ----------------------------------------------------
            if (jumpBackInList.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Jump Back In",
                        color = Color(0xFF111111),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Surface(
                        onClick = { jumpBackInList = emptyList() },
                        shape = RoundedCornerShape(50),
                        color = Color.Transparent,
                        border = BorderStroke(1.dp, Color(0xFFD1D1D6))
                    ) {
                        Text(
                            text = "Clear",
                            color = Color(0xFF222222),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    items(jumpBackInList) { resId ->
                        Box(
                            modifier = Modifier
                                .size(130.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .clickable { onProductClick("recent_$resId") }
                        ) {
                            Image(
                                painter = painterResource(id = resId),
                                contentDescription = "Recently Viewed",
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(34.dp))
            }

            // ----------------------------------------------------
            // 5. SECTION: "New for You" & BENTO MOSAIC CARDS
            // ----------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "New for You",
                    color = Color(0xFF111111),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    onClick = { /* Filter action */ },
                    shape = CircleShape,
                    color = Color.White,
                    shadowElevation = 1.dp,
                    border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
                    modifier = Modifier.size(38.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Filter",
                            tint = Color(0xFF111111),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Card Mozaik 1: "Sneakers of the Week"
            BentoMosaicCard(
                title = "Sneakers of the Week",
                largeImageRes = R.drawable.shop16,
                smallImage1 = R.drawable.shop4,
                smallImage2 = R.drawable.shop15,
                bottomImage1 = R.drawable.shop17,
                bottomImage2 = R.drawable.shop8,
                bottomImage3 = R.drawable.shop13,
                isLargeOnLeft = true,
                onExploreClick = { onProductClick("sneakers_of_the_week") }
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Card Mozaik 2: "Performance Gear" / "NBA Jerseys"
            BentoMosaicCard(
                title = "Performance Gear",
                largeImageRes = R.drawable.shop2,
                smallImage1 = R.drawable.shop5,
                smallImage2 = R.drawable.shop7,
                bottomImage1 = R.drawable.shop3,
                bottomImage2 = R.drawable.shop6,
                bottomImage3 = R.drawable.shop14,
                isLargeOnLeft = false, // Layout mirror seperti referensi NBA jerseys
                onExploreClick = { onProductClick("performance_gear") }
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Card Mozaik 3: "Nike Skateboarding"
            BentoMosaicCard(
                title = "Nike Skateboarding",
                largeImageRes = R.drawable.shop1,
                smallImage1 = R.drawable.shop10,
                smallImage2 = R.drawable.shop11,
                bottomImage1 = R.drawable.shop9,
                bottomImage2 = R.drawable.shop12,
                bottomImage3 = R.drawable.shop17,
                isLargeOnLeft = true,
                onExploreClick = { onProductClick("skateboarding") }
            )

            Spacer(modifier = Modifier.height(34.dp))

            // ----------------------------------------------------
            // 6. SECTION: "Lifestyle Picks" (3-Kolom Product Grid)
            // ----------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Lifestyle Picks",
                        color = Color(0xFF111111),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "Based on your interests",
                        color = Color(0xFF8E8E93),
                        fontSize = 14.sp
                    )
                }

                Surface(
                    onClick = { /* More action */ },
                    shape = RoundedCornerShape(50),
                    color = Color(0xFFEBEBF0),
                    border = BorderStroke(1.dp, Color(0xFFE0E0E6))
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Shuffle,
                            contentDescription = "More",
                            tint = Color(0xFF111111),
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "More",
                            color = Color(0xFF111111),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // 3-Kolom Grid Produk
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                for (i in 0 until lifestylePicks.size step 3) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        val p1 = lifestylePicks[i]
                        ProductGridItem(
                            product = p1,
                            onClick = { onProductClick(p1.id) },
                            modifier = Modifier.weight(1f)
                        )

                        if (i + 1 < lifestylePicks.size) {
                            val p2 = lifestylePicks[i + 1]
                            ProductGridItem(
                                product = p2,
                                onClick = { onProductClick(p2.id) },
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        if (i + 2 < lifestylePicks.size) {
                            val p3 = lifestylePicks[i + 2]
                            ProductGridItem(
                                product = p3,
                                onClick = { onProductClick(p3.id) },
                                modifier = Modifier.weight(1f)
                            )
                        } else {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            // Bottom Spacer agar konten tidak tertutup Floating Navbar
            Spacer(modifier = Modifier.height(130.dp))
        }
    }
}

/**
 * Komponen Card Kategori Mozaik (2 Kolom)
 */
@Composable
private fun CategoryMosaicCard(
    item: ShopCategory,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(112.dp)
            .clip(RoundedCornerShape(14.dp))
            .clickable(onClick = onClick)
    ) {
        Image(
            painter = painterResource(id = item.imageRes),
            contentDescription = item.title,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        // Gradient gelap pada bagian bawah kartu agar teks terbaca jelas
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.65f)
                .align(Alignment.BottomCenter)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.40f),
                            Color.Black.copy(alpha = 0.75f)
                        )
                    )
                )
        )

        Text(
            text = item.title,
            color = Color.White,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(horizontal = 12.dp, vertical = 10.dp)
        )
    }
}

/**
 * Komponen Bento Mosaic Card (Kombinasi gambar besar & kecil + footer navigasi detail)
 */
@Composable
private fun BentoMosaicCard(
    title: String,
    @DrawableRes largeImageRes: Int,
    @DrawableRes smallImage1: Int,
    @DrawableRes smallImage2: Int,
    @DrawableRes bottomImage1: Int,
    @DrawableRes bottomImage2: Int,
    @DrawableRes bottomImage3: Int,
    isLargeOnLeft: Boolean,
    onExploreClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(20.dp),
        color = Color.White,
        shadowElevation = 2.dp,
        border = BorderStroke(1.dp, Color(0xFFECECF0)),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // Baris Atas: 1 Gambar Besar & 2 Gambar Kecil Menumpuk
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isLargeOnLeft) {
                    // Gambar Besar di Kiri
                    Box(
                        modifier = Modifier
                            .weight(1.25f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF4F4F6))
                    ) {
                        Image(
                            painter = painterResource(id = largeImageRes),
                            contentDescription = title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    // 2 Gambar Menumpuk di Kanan
                    Column(
                        modifier = Modifier
                            .weight(0.75f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF4F4F6))
                        ) {
                            Image(
                                painter = painterResource(id = smallImage1),
                                contentDescription = title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF4F4F6))
                        ) {
                            Image(
                                painter = painterResource(id = smallImage2),
                                contentDescription = title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }
                } else {
                    // 2 Gambar Menumpuk di Kiri (Versi Mirror)
                    Column(
                        modifier = Modifier
                            .weight(0.75f)
                            .fillMaxHeight(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF4F4F6))
                        ) {
                            Image(
                                painter = painterResource(id = smallImage1),
                                contentDescription = title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .weight(1f)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color(0xFFF4F4F6))
                        ) {
                            Image(
                                painter = painterResource(id = smallImage2),
                                contentDescription = title,
                                contentScale = ContentScale.Crop,
                                modifier = Modifier.fillMaxSize()
                            )
                        }
                    }

                    // Gambar Besar di Kanan
                    Box(
                        modifier = Modifier
                            .weight(1.25f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF4F4F6))
                    ) {
                        Image(
                            painter = painterResource(id = largeImageRes),
                            contentDescription = title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Baris Bawah: 3 Gambar Kotak Berdampingan
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(96.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf(bottomImage1, bottomImage2, bottomImage3).forEach { imgRes ->
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxHeight()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFF4F4F6))
                    ) {
                        Image(
                            painter = painterResource(id = imgRes),
                            contentDescription = title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Footer: Judul Card & Tombol Chevron Right
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onExploreClick)
                    .padding(horizontal = 6.dp, vertical = 6.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    color = Color(0xFF111111),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                Surface(
                    shape = CircleShape,
                    color = Color(0xFFF2F2F7),
                    modifier = Modifier.size(34.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.ChevronRight,
                            contentDescription = "Explore",
                            tint = Color(0xFF111111),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Komponen Item Produk 3-Kolom pada Lifestyle Picks
 */
@Composable
private fun ProductGridItem(
    product: ShopProduct,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .clickable(onClick = onClick)
    ) {
        // Thumbnail Container dipaskan dengan ukuran gambar tanpa white space / padding
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Nama Produk
        Text(
            text = product.name,
            color = Color(0xFF111111),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            lineHeight = 16.sp,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )

        Spacer(modifier = Modifier.height(2.dp))

        // Harga Produk & Coretan Diskon (jika ada)
        Text(
            text = product.price,
            color = Color(0xFF222222),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )

        if (product.originalPrice != null) {
            Text(
                text = product.originalPrice,
                color = Color(0xFF8E8E93),
                fontSize = 11.sp,
                textDecoration = TextDecoration.LineThrough
            )
        }
    }
}
