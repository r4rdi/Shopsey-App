package com.example.shopsey.ui.search

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
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.shopsey.R

/**
 * Model produk untuk pencarian dan filter di halaman Search.
 */
data class SearchProductItem(
    val id: String,
    val name: String,
    val category: String,
    val price: String,
    val imageRes: Int
)

/**
 * Model kategori navigasi pada halaman Search.
 */
data class SearchCategoryTile(
    val title: String,
    val subtitle: String,
    val imageRes: Int,
    val query: String
)

/**
 * Halaman Search (Search Page) bertema Terang (Light Mode).
 * Didesain stabil tanpa ketergantungan API eksperimental (mencegah NoSuchMethodError FlowRow).
 */
@Composable
fun SearchScreen(
    modifier: Modifier = Modifier,
    onProductClick: (SearchProductItem) -> Unit = {}
) {
    val focusManager = LocalFocusManager.current
    var searchQuery by remember { mutableStateOf("") }

    val recentSearches = remember {
        mutableStateListOf(
            "Air Jordan 4 Toro",
            "Nike Dunk Low",
            "Kobe 8 Protro"
        )
    }

    // Kelompok Trending Searches dalam baris-baris terstruktur yang stabil
    val trendingRows = remember {
        listOf(
            listOf("Air Jordan 1", "Nike Dunk", "Kobe Protro"),
            listOf("Predator Elite", "Tech Fleece", "Curry Flow"),
            listOf("Air Max Plus", "Puma Future", "Samba OG")
        )
    }

    // Katalog produk untuk pencarian interaktif
    val catalogProducts = remember {
        listOf(
            SearchProductItem("1", "Air Jordan 4 'Toro'", "Men's Shoes", "Rp3.329.000", R.drawable.bag1),
            SearchProductItem("2", "Nike Dunk Low Retro", "Men's Shoes", "Rp1.549.000", R.drawable.shop1),
            SearchProductItem("3", "Air Jordan 1 Low OG", "Unisex Shoes", "Rp2.199.000", R.drawable.shop2),
            SearchProductItem("4", "Kobe 8 Protro 'Venice Beach'", "Basketball", "Rp2.849.000", R.drawable.shop3),
            SearchProductItem("5", "Nike Air Max Plus Drift", "Men's Shoes", "Rp2.699.000", R.drawable.shop4),
            SearchProductItem("6", "Puma Future 7 Ultimate", "Football Boots", "Rp3.199.000", R.drawable.shop5)
        )
    }

    // Kategori Populer
    val popularCategories = remember {
        listOf(
            SearchCategoryTile("Sepatu Pria", "Koleksi Terbaru", R.drawable.shop1, "Men"),
            SearchCategoryTile("Sepatu Wanita", "Tren Terkini", R.drawable.shop2, "Women"),
            SearchCategoryTile("Jordan", "Performance & Street", R.drawable.bag1, "Jordan"),
            SearchCategoryTile("Pakaian & Aksesoris", "Hoodie, Celana & Tas", R.drawable.shop3, "Apparel")
        )
    }

    // Filter produk berdasarkan searchQuery
    val filteredProducts = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            emptyList()
        } else {
            catalogProducts.filter {
                it.name.contains(searchQuery, ignoreCase = true) ||
                        it.category.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F9))
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .statusBarsPadding()
                .padding(horizontal = 20.dp, vertical = 12.dp)
        ) {
            // Header Bar
            Text(
                text = "Search",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF111111)
            )

            Spacer(modifier = Modifier.height(14.dp))

            // Search Input Bar Interaktif
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = Color.White,
                shadowElevation = 2.dp,
                border = BorderStroke(1.dp, Color(0xFFE5E5EA)),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(horizontal = 16.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        tint = Color(0xFF707072),
                        modifier = Modifier.size(22.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Box(
                        modifier = Modifier.weight(1f),
                        contentAlignment = Alignment.CenterStart
                    ) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Cari sepatu, pakaian, gear...",
                                color = Color(0xFF8E8E93),
                                fontSize = 15.sp
                            )
                        }

                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            singleLine = true,
                            textStyle = TextStyle(
                                color = Color(0xFF111111),
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Normal
                            ),
                            cursorBrush = SolidColor(Color(0xFF111111)),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(
                                onSearch = {
                                    if (searchQuery.isNotBlank() && !recentSearches.contains(searchQuery.trim())) {
                                        recentSearches.add(0, searchQuery.trim())
                                    }
                                    focusManager.clearFocus()
                                }
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    if (searchQuery.isNotEmpty()) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .clickable { searchQuery = "" },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Clear",
                                tint = Color(0xFF707072),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (searchQuery.isNotEmpty()) {
                // Tampilan Hasil Pencarian
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Hasil untuk \"$searchQuery\"",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF111111)
                    )
                    Text(
                        text = "${filteredProducts.size} item",
                        fontSize = 13.sp,
                        color = Color(0xFF707072)
                    )
                }

                Spacer(modifier = Modifier.height(14.dp))

                if (filteredProducts.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 40.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = Color(0xFFCCCCCC),
                                modifier = Modifier.size(54.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Tidak ada produk yang cocok",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFF333333)
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Coba kata kunci lain seperti Jordan, Dunk, atau Kobe",
                                fontSize = 13.sp,
                                color = Color(0xFF888888)
                            )
                        }
                    }
                } else {
                    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        filteredProducts.forEach { product ->
                            SearchResultCard(
                                product = product,
                                onClick = { onProductClick(product) }
                            )
                        }
                    }
                }
            } else {
                // Tampilan Default: Trending Searches, Recent Searches, Kategori Populer

                // 1. Trending Searches
                Text(
                    text = "Trending Searches",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111111)
                )

                Spacer(modifier = Modifier.height(12.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    trendingRows.forEach { rowItems ->
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            rowItems.forEach { term ->
                                Surface(
                                    onClick = {
                                        searchQuery = term
                                        if (!recentSearches.contains(term)) {
                                            recentSearches.add(0, term)
                                        }
                                    },
                                    shape = RoundedCornerShape(50),
                                    color = Color.White,
                                    shadowElevation = 0.5.dp,
                                    border = BorderStroke(1.dp, Color(0xFFE5E5EA))
                                ) {
                                    Text(
                                        text = term,
                                        color = Color(0xFF222222),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 7.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(28.dp))

                // 2. Recent Searches
                if (recentSearches.isNotEmpty()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Pencarian Terakhir",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF111111)
                        )
                        Text(
                            text = "Hapus Semua",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = Color(0xFF707072),
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .clickable { recentSearches.clear() }
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Surface(
                        shape = RoundedCornerShape(16.dp),
                        color = Color.White,
                        border = BorderStroke(1.dp, Color(0xFFEBEBF0))
                    ) {
                        Column {
                            recentSearches.forEachIndexed { index, term ->
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable { searchQuery = term }
                                        .padding(horizontal = 16.dp, vertical = 13.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Search,
                                            contentDescription = null,
                                            tint = Color(0xFF8E8E93),
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(
                                            text = term,
                                            fontSize = 14.sp,
                                            color = Color(0xFF222222),
                                            fontWeight = FontWeight.Normal
                                        )
                                    }

                                    Box(
                                        modifier = Modifier
                                            .size(22.dp)
                                            .clip(CircleShape)
                                            .clickable { recentSearches.remove(term) },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Close,
                                            contentDescription = "Hapus",
                                            tint = Color(0xFFC7C7CC),
                                            modifier = Modifier.size(14.dp)
                                        )
                                    }
                                }

                                if (index < recentSearches.size - 1) {
                                    Box(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(0.6.dp)
                                            .padding(horizontal = 16.dp)
                                            .background(Color(0xFFF0F0F2))
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))
                }

                // 3. Jelajahi Kategori
                Text(
                    text = "Jelajahi Kategori",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111111)
                )

                Spacer(modifier = Modifier.height(14.dp))

                Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                    val tilePairs = popularCategories.chunked(2)
                    tilePairs.forEach { pair ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            pair.forEach { tile ->
                                Surface(
                                    onClick = { searchQuery = tile.query },
                                    shape = RoundedCornerShape(16.dp),
                                    color = Color.White,
                                    border = BorderStroke(1.dp, Color(0xFFEBEBF0)),
                                    modifier = Modifier
                                        .weight(1f)
                                        .height(115.dp)
                                ) {
                                    Box(modifier = Modifier.fillMaxSize()) {
                                        Image(
                                            painter = painterResource(id = tile.imageRes),
                                            contentDescription = tile.title,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color(0xFFF3F3F5))
                                        )

                                        Box(
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .background(Color.Black.copy(alpha = 0.38f))
                                        )

                                        Column(
                                            modifier = Modifier
                                                .align(Alignment.BottomStart)
                                                .padding(12.dp)
                                        ) {
                                            Text(
                                                text = tile.title,
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                            Text(
                                                text = tile.subtitle,
                                                fontSize = 11.sp,
                                                color = Color.White.copy(alpha = 0.85f)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(140.dp))
        }
    }
}

/**
 * Kartu Item Hasil Pencarian Produk
 */
@Composable
private fun SearchResultCard(
    product: SearchProductItem,
    onClick: () -> Unit
) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        border = BorderStroke(1.dp, Color(0xFFEBEBF0)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = product.imageRes),
                contentDescription = product.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(72.dp)
                    .clip(RoundedCornerShape(12.dp))
            )

            Spacer(modifier = Modifier.width(14.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = product.name,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color(0xFF111111)
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = product.category,
                    fontSize = 12.sp,
                    color = Color(0xFF707072)
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = product.price,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF111111)
                )
            }
        }
    }
}
