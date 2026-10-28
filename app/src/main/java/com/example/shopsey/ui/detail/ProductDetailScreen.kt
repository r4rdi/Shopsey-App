package com.example.shopsey.ui.detail

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsey.R
import kotlinx.coroutines.launch

/**
 * Model data rekomendasi produk ("You Might Also Like").
 */
data class RecommendationProduct(
    val id: String,
    val title: String,
    val category: String,
    val price: String,
    val imageRes: Int
)

/**
 * Model data lengkap untuk Halaman Detail Produk.
 */
data class ProductDetailState(
    val id: String = "aj4-toro",
    val title: String = "Air Jordan 4 'Toro'",
    val colorway: String = "Fire Red/Black/Cement Grey/White",
    val category: String = "Men's Shoes",
    val price: String = "Rp3.329.000",
    val description: String = "¡Olé! We're bringing the 'Toro' colourway all the way back from 2013 in all its bullish glory. A Fire Red upper brings the heat, accented with Black and cool Cement Grey. Staying true to form, Nike Air cushioning in the heel and forefoot softens your step. Original design elements—like floating eyestays and mesh-like panels—make it feel as fresh as it did in '89.",
    val images: List<Int> = listOf(
        R.drawable.detail1,
        R.drawable.detail2,
        R.drawable.detail3,
        R.drawable.detail4,
        R.drawable.detail5,
        R.drawable.detail6,
        R.drawable.detail7,
        R.drawable.detail8
    ),
    val sizes: List<Pair<String, Boolean>> = listOf(
        "EU 40" to true,
        "EU 40.5" to true,
        "EU 41" to true,
        "EU 42" to true,
        "EU 42.5" to true,
        "EU 43" to true,
        "EU 44" to true,
        "EU 44.5" to true,
        "EU 45.5" to true,
        "EU 47" to true,
        "EU 47.5" to false
    ),
    val reviewCount: Int = 207,
    val rating: Float = 4.9f
)

/**
 * Halaman Detail Produk (Product Detail Screen).
 *
 * Mengimplementasikan seluruh komponen sesuai referensi Screenshot 1, 2, 3, dan 4:
 * 1. Top Bar: Tombol Back (<-), Judul Produk, dan Icon Share.
 * 2. Galeri Foto Multi-Arah: HorizontalPager (detail1.png s/d detail8.png) dengan Dot Indicator.
 * 3. Informasi Produk: Colorway, Nama Produk, Kategori, dan Harga.
 * 4. Deskripsi Cerita Produk & Link "View Product Details" (modal dialog spek teknis).
 * 5. Pilihan Ukuran Sepatu (Select Size) & Link "Size Guide" (EU 40 s/d EU 47.5 coret/habis).
 * 6. Tombol Aksi: "Add to Bag" (hitam pekat) & "Favorited ❤" / "Favorite ♡" (toggleable).
 * 7. Accordion Ulasan: "Reviews (207) ★★★★★ ⌄" yang dapat di-expand.
 * 8. Tombol Bantuan: "Chat".
 * 9. Rekomendasi: "You Might Also Like" dengan carousel horizontal.
 * 10. Catatan Kaki: "*Store availability and pricing not guaranteed.".
 */
@Composable
fun ProductDetailScreen(
    onBack: () -> Unit,
    modifier: Modifier = Modifier,
    product: ProductDetailState = remember { ProductDetailState() },
    onAddToBagClick: (String) -> Unit = {},
    onRecommendationClick: (RecommendationProduct) -> Unit = {}
) {
    // Tangani tombol back perangkat Android
    BackHandler {
        onBack()
    }

    val scrollState = rememberScrollState()
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val pagerState = rememberPagerState(pageCount = { product.images.size })

    var selectedSize by remember { mutableStateOf("EU 42") }
    var isFavorited by remember { mutableStateOf(true) }
    var reviewsExpanded by remember { mutableStateOf(false) }
    var showProductSpecsDialog by remember { mutableStateOf(false) }
    var showSizeGuideDialog by remember { mutableStateOf(false) }

    val recommendations = remember {
        listOf(
            RecommendationProduct("aj5-carolina", "Air Jordan 5 'Black Carolina'", "Men's Shoes", "Rp3.269.000", R.drawable.shop4),
            RecommendationProduct("aj4-retro", "Air Jordan 4 Retro 'Fl...'", "Men's shoes", "Rp3.329.000", R.drawable.shop2),
            RecommendationProduct("kobe8-venice", "Kobe 8 Protro 'Venice Beach'", "Basketball", "Rp2.849.000", R.drawable.shop3),
            RecommendationProduct("dunk-low", "Nike Dunk Low Retro", "Men's Shoes", "Rp1.549.000", R.drawable.shop1)
        )
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
        ) {
            // ----------------------------------------------------
            // 1. TOP APP BAR (Back, Title, Share)
            // ----------------------------------------------------
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 8.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(onClick = onBack) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        tint = Color(0xFF111111),
                        modifier = Modifier.size(24.dp)
                    )
                }

                Text(
                    text = product.title,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111111),
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center
                )

                IconButton(
                    onClick = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Tautan ${product.title} disalin ke clipboard!")
                        }
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Share,
                        contentDescription = "Share",
                        tint = Color(0xFF111111),
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            // ----------------------------------------------------
            // 2. GALERI FOTO PRODUK (HorizontalPager detail1 s/d detail8)
            // Kelebaran gambar dipaskan penuh dengan lebar device HP pengguna (edge-to-edge),
            // tanpa padding atau white space / negative space.
            // ----------------------------------------------------
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1070f / 1338f)
            ) {
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier.fillMaxSize()
                ) { pageIndex ->
                    val imageRes = product.images.getOrElse(pageIndex) { R.drawable.bag1 }
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = "${product.title} Angle ${pageIndex + 1}",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }

            // Indikator Titik Pager (Dot Indicators)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp, bottom = 22.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                repeat(product.images.size) { index ->
                    val isCurrent = pagerState.currentPage == index
                    Box(
                        modifier = Modifier
                            .padding(horizontal = 3.5.dp)
                            .size(if (isCurrent) 6.5.dp else 5.5.dp)
                            .clip(CircleShape)
                            .background(
                                if (isCurrent) Color(0xFF111111) else Color(0xFFD1D1D6)
                            )
                    )
                }
            }

            // ----------------------------------------------------
            // 3. INFORMASI PRODUK (Colorway, Nama, Kategori, Harga)
            // ----------------------------------------------------
            Column(modifier = Modifier.padding(horizontal = 20.dp)) {
                Text(
                    text = product.colorway,
                    fontSize = 14.sp,
                    color = Color(0xFF707072),
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111111)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = product.category,
                    fontSize = 15.sp,
                    color = Color(0xFF111111),
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = product.price,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111111)
                )

                Spacer(modifier = Modifier.height(28.dp))

                // ----------------------------------------------------
                // 4. DESKRIPSI PRODUK & LINK VIEW PRODUCT DETAILS
                // ----------------------------------------------------
                Text(
                    text = product.description,
                    fontSize = 14.sp,
                    color = Color(0xFF222222),
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Normal
                )

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "View Product Details",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111111),
                    textDecoration = TextDecoration.Underline,
                    modifier = Modifier.clickable { showProductSpecsDialog = true }
                )

                Spacer(modifier = Modifier.height(30.dp))

                // ----------------------------------------------------
                // 5. SELECT SIZE & SIZE GUIDE
                // ----------------------------------------------------
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Select Size",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111111)
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .clickable { showSizeGuideDialog = true }
                            .padding(4.dp)
                    ) {
                        Text(
                            text = "Size Guide",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF111111)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Daftar Ukuran Sepatu (Grid / Horizontal Flow)
                val sizeRows = remember(product.sizes) { product.sizes.chunked(3) }
                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    sizeRows.forEach { rowSizes ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            rowSizes.forEach { (sizeText, isAvailable) ->
                                val isSelected = selectedSize == sizeText
                                Surface(
                                    onClick = {
                                        if (isAvailable) {
                                            selectedSize = sizeText
                                        }
                                    },
                                    shape = RoundedCornerShape(10.dp),
                                    color = when {
                                        !isAvailable -> Color(0xFFF3F3F5)
                                        isSelected -> Color.White
                                        else -> Color.White
                                    },
                                    border = BorderStroke(
                                        width = if (isSelected) 1.6.dp else 1.dp,
                                        color = when {
                                            !isAvailable -> Color(0xFFE5E5EA)
                                            isSelected -> Color(0xFF111111)
                                            else -> Color(0xFFE5E5EA)
                                        }
                                    ),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(52.dp)
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = sizeText,
                                            fontSize = 14.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = when {
                                                !isAvailable -> Color(0xFFB0B0B5)
                                                isSelected -> Color(0xFF111111)
                                                else -> Color(0xFF222222)
                                            },
                                            textDecoration = if (!isAvailable) TextDecoration.LineThrough else TextDecoration.None
                                        )
                                    }
                                }
                            }
                            // Isi sisa kolom jika baris tidak penuh
                            if (rowSizes.size < 3) {
                                repeat(3 - rowSizes.size) {
                                    Spacer(modifier = Modifier.weight(1f))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))

                // ----------------------------------------------------
                // 6. TOMBOL AKSI: ADD TO BAG & FAVORITED
                // ----------------------------------------------------
                Surface(
                    onClick = {
                        onAddToBagClick(selectedSize)
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("${product.title} ($selectedSize) ditambahkan ke Bag!")
                        }
                    },
                    shape = RoundedCornerShape(50),
                    color = Color(0xFF111111),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Add to Bag",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Surface(
                    onClick = { isFavorited = !isFavorited },
                    shape = RoundedCornerShape(50),
                    color = Color.White,
                    border = BorderStroke(1.2.dp, Color(0xFFE5E5EA)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(54.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = if (isFavorited) "Favorited " else "Favorite ",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF111111)
                        )
                        Text(
                            text = if (isFavorited) "❤" else "♡",
                            fontSize = 16.sp,
                            color = if (isFavorited) Color(0xFF111111) else Color(0xFF111111)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Catatan Excluded from Promotions
                Text(
                    text = "This product is excluded from all promotions and discounts",
                    fontSize = 12.sp,
                    color = Color(0xFF707072),
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(32.dp))

                HorizontalDivider(color = Color(0xFFEBEBF0), thickness = 1.dp)

                // ----------------------------------------------------
                // 7. REVIEWS ACCORDION
                // ----------------------------------------------------
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(bounded = true),
                            onClick = { reviewsExpanded = !reviewsExpanded }
                        )
                        .padding(vertical = 18.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Reviews (${product.reviewCount})",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111111)
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        repeat(5) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = Color(0xFF111111),
                                modifier = Modifier.size(17.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(6.dp))
                        Icon(
                            imageVector = if (reviewsExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                            contentDescription = if (reviewsExpanded) "Collapse" else "Expand",
                            tint = Color(0xFF111111),
                            modifier = Modifier.size(22.dp)
                        )
                    }
                }

                AnimatedVisibility(
                    visible = reviewsExpanded,
                    enter = fadeIn() + expandVertically(),
                    exit = fadeOut() + shrinkVertically()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 18.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {
                        // Review Item 1
                        ReviewItem(
                            author = "Michael B.",
                            date = "24 Okt 2026",
                            rating = 5,
                            review = "The Red Toro is an absolute grail! Premium materials, exact shape from 2013, and comfortable for all-day wear. Highly recommended."
                        )
                        // Review Item 2
                        ReviewItem(
                            author = "Sarah K.",
                            date = "18 Okt 2026",
                            rating = 5,
                            review = "Great packaging and fast shipping. Fits true to size. Looks even better in person under natural light."
                        )
                    }
                }

                HorizontalDivider(color = Color(0xFFEBEBF0), thickness = 1.dp)

                Spacer(modifier = Modifier.height(24.dp))

                // ----------------------------------------------------
                // 8. CHAT / SUPPORT ROW
                // ----------------------------------------------------
                Surface(
                    onClick = {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Menghubungkan ke layanan Chat Customer Support...")
                        }
                    },
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White,
                    border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "💬",
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Chat",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color(0xFF111111)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(36.dp))

                // ----------------------------------------------------
                // 9. YOU MIGHT ALSO LIKE SECTION
                // ----------------------------------------------------
                Text(
                    text = "You Might Also Like",
                    fontSize = 19.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111111)
                )

                Spacer(modifier = Modifier.height(16.dp))
            }

            // Carousel Rekomendasi Horizontal
            LazyRow(
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(recommendations) { item ->
                    RecommendationCard(
                        item = item,
                        onClick = { onRecommendationClick(item) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(18.dp))

            // Catatan Toko & Harga
            Text(
                text = "*Store availability and pricing not guaranteed.",
                fontSize = 11.sp,
                color = Color(0xFF8E8E93),
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            // Ruang ekstra bawah agar konten bisa di-scroll bebas di atas floating navbar
            Spacer(modifier = Modifier.height(140.dp))
        }

        // Snackbar Host untuk notifikasi cepat
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 100.dp, start = 20.dp, end = 20.dp)
        )
    }

    // Modal Spesifikasi Detail Produk
    if (showProductSpecsDialog) {
        AlertDialog(
            onDismissRequest = { showProductSpecsDialog = false },
            title = {
                Text(
                    text = "Product Details",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("• Style Code: 308497-603", fontSize = 13.sp, color = Color(0xFF333333))
                    Text("• Color: Fire Red / Black / Cement Grey / White", fontSize = 13.sp, color = Color(0xFF333333))
                    Text("• Material: Premium Nubuck Leather Upper with Mesh Panels", fontSize = 13.sp, color = Color(0xFF333333))
                    Text("• Cushioning: Nike Air-Sole unit in heel & forefoot", fontSize = 13.sp, color = Color(0xFF333333))
                    Text("• Outsole: Durable herringbone rubber traction", fontSize = 13.sp, color = Color(0xFF333333))
                    Text("• Origin: Vietnam", fontSize = 13.sp, color = Color(0xFF333333))
                }
            },
            confirmButton = {
                TextButton(onClick = { showProductSpecsDialog = false }) {
                    Text("Close", color = Color(0xFF111111), fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    // Modal Panduan Ukuran (Size Guide)
    if (showSizeGuideDialog) {
        AlertDialog(
            onDismissRequest = { showSizeGuideDialog = false },
            title = {
                Text(
                    text = "Men's Shoe Size Guide",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                    Text("EU 40  -> US 7   (25.0 cm)", fontSize = 13.sp, color = Color(0xFF333333))
                    Text("EU 41  -> US 8   (26.0 cm)", fontSize = 13.sp, color = Color(0xFF333333))
                    Text("EU 42  -> US 8.5 (26.5 cm) — Standard Fit", fontSize = 13.sp, fontWeight = FontWeight.SemiBold, color = Color(0xFF111111))
                    Text("EU 43  -> US 9.5 (27.5 cm)", fontSize = 13.sp, color = Color(0xFF333333))
                    Text("EU 44  -> US 10  (28.0 cm)", fontSize = 13.sp, color = Color(0xFF333333))
                    Text("EU 45.5-> US 11.5(29.5 cm)", fontSize = 13.sp, color = Color(0xFF333333))
                    Text("EU 47  -> US 12.5(30.5 cm)", fontSize = 13.sp, color = Color(0xFF333333))
                }
            },
            confirmButton = {
                TextButton(onClick = { showSizeGuideDialog = false }) {
                    Text("Understood", color = Color(0xFF111111), fontWeight = FontWeight.Bold)
                }
            }
        )
    }
}

/**
 * Komponen Kartu Rekomendasi ("You Might Also Like")
 */
@Composable
private fun RecommendationCard(
    item: RecommendationProduct,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(185.dp)
            .clickable(onClick = onClick)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(185.dp)
                .clip(RoundedCornerShape(12.dp))
        ) {
            Image(
                painter = painterResource(id = item.imageRes),
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = item.title,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF111111),
            maxLines = 1
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = item.category,
            fontSize = 12.sp,
            color = Color(0xFF707072)
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = item.price,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color(0xFF111111)
        )
    }
}

/**
 * Komponen Ulasan Pengguna (Review Item)
 */
@Composable
private fun ReviewItem(
    author: String,
    date: String,
    rating: Int,
    review: String
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF9F9FA), RoundedCornerShape(10.dp))
            .padding(12.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = author,
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp,
                color = Color(0xFF111111)
            )
            Text(
                text = date,
                fontSize = 11.sp,
                color = Color(0xFF8E8E93)
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row {
            repeat(rating) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = Color(0xFF111111),
                    modifier = Modifier.size(13.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = review,
            fontSize = 12.sp,
            lineHeight = 17.sp,
            color = Color(0xFF333333)
        )
    }
}
