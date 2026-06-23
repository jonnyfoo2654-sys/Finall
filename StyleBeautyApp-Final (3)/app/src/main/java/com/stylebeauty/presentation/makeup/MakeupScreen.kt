package com.stylebeauty.assistant.presentation.makeup

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class MakeupProduct(
    val name: String,
    val brand: String,
    val category: String,
    val price: String,
    val color: Color,
    val description: String,
    val rating: Float
)

data class MakeupCategory(val name: String, val icon: androidx.compose.ui.graphics.vector.ImageVector, val color: Color)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MakeupScreen(navController: NavController) {
    var selectedCategory by remember { mutableStateOf("All") }

    val categories = listOf(
        MakeupCategory("All", Icons.Default.GridView, Color(0xFFFF4081)),
        MakeupCategory("Foundation", Icons.Default.Face, Color(0xFFFF9800)),
        MakeupCategory("Lips", Icons.Default.FavoriteBorder, Color(0xFFE91E63)),
        MakeupCategory("Eyes", Icons.Default.RemoveRedEye, Color(0xFF9C27B0)),
        MakeupCategory("Blush", Icons.Default.ColorLens, Color(0xFFFF7043)),
        MakeupCategory("Highlighter", Icons.Default.AutoAwesome, Color(0xFFFFD700))
    )

    val products = listOf(
        MakeupProduct("Luminous Silk Foundation", "Giorgio Armani", "Foundation", "$69", Color(0xFFD4A76A), "Buildable coverage with a natural finish", 4.8f),
        MakeupProduct("Pro Filt'r Foundation", "Fenty Beauty", "Foundation", "$40", Color(0xFFC68642), "40 shades for all skin tones", 4.9f),
        MakeupProduct("Pillow Lips Lipstick", "Charlotte Tilbury", "Lips", "$38", Color(0xFFB5485D), "Hydrating plumping formula", 4.7f),
        MakeupProduct("Velvet Lip Liner", "MAC", "Lips", "$22", Color(0xFF8B2252), "Long-lasting definition", 4.6f),
        MakeupProduct("Better Than Sex Mascara", "Too Faced", "Eyes", "$29", Color(0xFF1A1A1A), "Dramatic volume and length", 4.8f),
        MakeupProduct("Palette Nude", "Urban Decay", "Eyes", "$55", Color(0xFF8B6914), "12 versatile nude shades", 4.7f),
        MakeupProduct("Man Ray Blush", "NARS", "Blush", "$38", Color(0xFFFF7B7B), "Buildable peachy-pink flush", 4.9f),
        MakeupProduct("Hourglass Ambient Blush", "Hourglass", "Blush", "$48", Color(0xFFFFAB91), "Photogenic diffused radiance", 4.8f),
        MakeupProduct("Becca Shimmering Skin Perfector", "BECCA", "Highlighter", "$46", Color(0xFFFFD700), "Liquid-like glow", 4.9f),
        MakeupProduct("Fairy Bomb Shimmer", "Patrick Ta", "Highlighter", "$38", Color(0xFFFFE0B2), "Buildable champagne glow", 4.7f)
    )

    val filtered = if (selectedCategory == "All") products else products.filter { it.category == selectedCategory }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Makeup & Beauty", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFFCE4EC),
                    titleContentColor = Color(0xFF880E4F)
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Hero banner
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(140.dp)
                        .background(Color(0xFFFCE4EC))
                        .padding(20.dp)
                ) {
                    Column {
                        Text("Virtual Try-On ✨", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color(0xFF880E4F))
                        Text("Discover your perfect makeup look", style = MaterialTheme.typography.bodyMedium, color = Color(0xFFAD1457))
                        Spacer(modifier = Modifier.height(10.dp))
                        Button(
                            onClick = {},
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE91E63))
                        ) {
                            Icon(Icons.Default.CameraAlt, null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Try On Now")
                        }
                    }
                }
            }

            // Category filter
            item {
                Text("Categories", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat.name,
                            onClick = { selectedCategory = cat.name },
                            label = { Text(cat.name) },
                            leadingIcon = { Icon(cat.icon, null, modifier = Modifier.size(16.dp)) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = cat.color.copy(alpha = 0.2f),
                                selectedLabelColor = cat.color
                            )
                        )
                    }
                }
            }

            // Products
            item {
                Text("Products", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp))
            }

            items(filtered.chunked(2)) { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { product ->
                        MakeupProductCard(product, modifier = Modifier.weight(1f))
                    }
                    if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }

            // Tips section
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text("Pro Tips 💡", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(16.dp, 8.dp, 16.dp, 8.dp))
                val tips = listOf(
                    "Always apply primer before foundation for longer-lasting wear",
                    "Match your foundation to your neck, not your face",
                    "Blend eyeshadow in circular motions for a seamless finish",
                    "Use a lip liner slightly outside your natural lip line for a fuller look"
                )
                tips.forEach { tip ->
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1))
                    ) {
                        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.Top) {
                            Text("✨", style = MaterialTheme.typography.bodyLarge)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(tip, style = MaterialTheme.typography.bodyMedium, color = Color(0xFF5D4037))
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MakeupProductCard(product: MakeupProduct, modifier: Modifier = Modifier) {
    var isFavorite by remember { mutableStateOf(false) }
    Card(
        modifier = modifier.clickable {},
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(80.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(product.color.copy(alpha = 0.15f)),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.size(48.dp).clip(CircleShape).background(product.color))
                IconButton(
                    onClick = { isFavorite = !isFavorite },
                    modifier = Modifier.align(Alignment.TopEnd).size(32.dp)
                ) {
                    Icon(
                        if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        null, tint = Color(0xFFE91E63), modifier = Modifier.size(18.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(product.brand, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(product.name, style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, maxLines = 2)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Star, null, tint = Color(0xFFFFB300), modifier = Modifier.size(14.dp))
                Text(" ${product.rating}", style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.weight(1f))
                Text(product.price, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.Bold, color = Color(0xFFE91E63))
            }
        }
    }
}
