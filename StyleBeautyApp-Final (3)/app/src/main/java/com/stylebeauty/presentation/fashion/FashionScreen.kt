package com.stylebeauty.assistant.presentation.fashion

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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

data class OutfitItem(
    val name: String,
    val style: String,
    val occasion: String,
    val colors: List<Color>,
    val pieces: List<String>,
    val price: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FashionScreen(navController: NavController) {
    var selectedOccasion by remember { mutableStateOf("All") }
    var selectedStyle by remember { mutableStateOf("All") }

    val occasions = listOf("All", "Casual", "Work", "Date Night", "Party", "Modest")
    val styles = listOf("All", "Minimalist", "Boho", "Classic", "Streetwear", "Romantic")

    val outfits = listOf(
        OutfitItem("Effortless Monday", "Minimalist", "Casual",
            listOf(Color(0xFFECEFF1), Color(0xFF607D8B), Color(0xFF455A64)),
            listOf("White linen shirt", "Straight-leg jeans", "White sneakers", "Tote bag"),
            "\$120 - \$200"),
        OutfitItem("Power Meeting", "Classic", "Work",
            listOf(Color(0xFF1A237E), Color(0xFFFFFFFF), Color(0xFF9E9E9E)),
            listOf("Navy blazer", "White silk blouse", "Tailored trousers", "Block heels"),
            "\$250 - \$400"),
        OutfitItem("Golden Hour Date", "Romantic", "Date Night",
            listOf(Color(0xFFFFD54F), Color(0xFFFF8A65), Color(0xFF4E342E)),
            listOf("Satin slip dress", "Strappy heels", "Gold jewelry", "Mini clutch"),
            "\$150 - \$300"),
        OutfitItem("Weekend Boho", "Boho", "Casual",
            listOf(Color(0xFF8D6E63), Color(0xFFBCAAA4), Color(0xFFFF8F00)),
            listOf("Flowy maxi skirt", "Embroidered blouse", "Sandals", "Layered necklaces"),
            "\$100 - \$180"),
        OutfitItem("Night Out", "Streetwear", "Party",
            listOf(Color(0xFF212121), Color(0xFF9C27B0), Color(0xFF757575)),
            listOf("Leather jacket", "Sequin mini skirt", "Ankle boots", "Chain bag"),
            "\$200 - \$350"),
        OutfitItem("Elegant Modesty", "Classic", "Modest",
            listOf(Color(0xFFE8EAF6), Color(0xFF9FA8DA), Color(0xFFFFFFFF)),
            listOf("Longline cardigan", "Wide-leg trousers", "Turtleneck top", "Ballet flats"),
            "\$130 - \$220")
    )

    val filtered = outfits.filter {
        (selectedOccasion == "All" || it.occasion == selectedOccasion) &&
        (selectedStyle == "All" || it.style == selectedStyle)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Fashion & Style", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFEDE7F6),
                    titleContentColor = Color(0xFF4A148C)
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Hero
            item {
                Box(
                    modifier = Modifier.fillMaxWidth().height(130.dp)
                        .background(Color(0xFFEDE7F6)).padding(20.dp)
                ) {
                    Column {
                        Text("Your Style, Your Rules 👗", style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = Color(0xFF4A148C))
                        Text("Curated outfits for every occasion", style = MaterialTheme.typography.bodyMedium, color = Color(0xFF7B1FA2))
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            AssistChip(onClick = {}, label = { Text("Style Quiz") }, leadingIcon = { Icon(Icons.Default.Quiz, null, modifier = Modifier.size(14.dp)) })
                            Spacer(modifier = Modifier.width(8.dp))
                            AssistChip(onClick = {}, label = { Text("Trending") }, leadingIcon = { Icon(Icons.Default.TrendingUp, null, modifier = Modifier.size(14.dp)) })
                        }
                    }
                }
            }

            // Occasion filter
            item {
                Text("Occasion", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(occasions) { occ ->
                        FilterChip(
                            selected = selectedOccasion == occ,
                            onClick = { selectedOccasion = occ },
                            label = { Text(occ) }
                        )
                    }
                }
            }

            // Style filter
            item {
                Text("Style", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(16.dp, 12.dp, 16.dp, 8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(styles) { style ->
                        FilterChip(
                            selected = selectedStyle == style,
                            onClick = { selectedStyle = style },
                            label = { Text(style) }
                        )
                    }
                }
            }

            item {
                Text("${filtered.size} Outfits Found", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp))
            }

            items(filtered) { outfit ->
                OutfitCard(outfit)
            }
        }
    }
}

@Composable
fun OutfitCard(outfit: OutfitItem) {
    var isSaved by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp).clickable {},
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Color palette preview
                Row(horizontalArrangement = Arrangement.spacedBy((-8).dp)) {
                    outfit.colors.forEach { color ->
                        Box(modifier = Modifier.size(32.dp).clip(RoundedCornerShape(50)).background(color))
                    }
                }
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(outfit.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Row {
                        AssistChip(onClick = {}, label = { Text(outfit.occasion, style = MaterialTheme.typography.labelSmall) }, modifier = Modifier.height(24.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        AssistChip(onClick = {}, label = { Text(outfit.style, style = MaterialTheme.typography.labelSmall) }, modifier = Modifier.height(24.dp))
                    }
                }
                IconButton(onClick = { isSaved = !isSaved }) {
                    Icon(if (isSaved) Icons.Default.Bookmark else Icons.Default.BookmarkBorder, null, tint = Color(0xFF7C4DFF))
                }
            }

            Spacer(modifier = Modifier.height(12.dp))
            Text("What to wear:", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Spacer(modifier = Modifier.height(6.dp))
            outfit.pieces.forEachIndexed { i, piece ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                    Text("${i + 1}.", style = MaterialTheme.typography.labelMedium, color = Color(0xFF7C4DFF), modifier = Modifier.width(20.dp))
                    Text(piece, style = MaterialTheme.typography.bodyMedium)
                }
            }
            Spacer(modifier = Modifier.height(10.dp))
            Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.PriceTag, null, tint = Color(0xFF7C4DFF), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(outfit.price, style = MaterialTheme.typography.labelMedium, color = Color(0xFF7C4DFF))
                Spacer(modifier = Modifier.weight(1f))
                Button(
                    onClick = {},
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7C4DFF)),
                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Text("Shop Look", style = MaterialTheme.typography.labelMedium)
                }
            }
        }
    }
}
