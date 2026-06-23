package com.stylebeauty.assistant.presentation.closet

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

data class ClothingItem(
    val name: String,
    val category: String,
    val color: Color,
    val colorName: String,
    val brand: String,
    val timesWorn: Int,
    val isFavorite: Boolean = false
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ClosetScreen(navController: NavController) {
    var selectedCategory by remember { mutableStateOf("All") }
    var showAddDialog by remember { mutableStateOf(false) }

    val categories = listOf("All", "Tops", "Bottoms", "Dresses", "Shoes", "Bags", "Accessories")

    val items = remember {
        mutableStateListOf(
            ClothingItem("White Button-Down Shirt", "Tops", Color(0xFFF5F5F5), "White", "Zara", 12, true),
            ClothingItem("Classic Blue Jeans", "Bottoms", Color(0xFF1565C0), "Indigo Blue", "Levi's", 8),
            ClothingItem("Black Midi Dress", "Dresses", Color(0xFF212121), "Black", "& Other Stories", 5, true),
            ClothingItem("Beige Trench Coat", "Tops", Color(0xFFD7B896), "Camel", "Burberry", 3),
            ClothingItem("White Sneakers", "Shoes", Color(0xFFFAFAFA), "White", "Nike", 20, true),
            ClothingItem("Tan Leather Boots", "Shoes", Color(0xFF8D6E63), "Tan", "Steve Madden", 6),
            ClothingItem("Mini Crossbody Bag", "Bags", Color(0xFF4E342E), "Brown", "Coach", 15, true),
            ClothingItem("Gold Hoop Earrings", "Accessories", Color(0xFFFFD700), "Gold", "Mejuri", 25),
            ClothingItem("Floral Wrap Skirt", "Bottoms", Color(0xFFE91E63), "Pink", "H&M", 4),
            ClothingItem("Cashmere Sweater", "Tops", Color(0xFFCE93D8), "Lavender", "Everlane", 7, true)
        )
    }

    val filtered = if (selectedCategory == "All") items else items.filter { it.category == selectedCategory }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("My Closet", fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { showAddDialog = true }) {
                        Icon(Icons.Default.Add, "Add item")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFE0F7FA),
                    titleContentColor = Color(0xFF006064)
                )
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(paddingValues),
            contentPadding = PaddingValues(bottom = 16.dp)
        ) {
            // Stats row
            item {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    StatCard("Total Items", "${items.size}", Color(0xFF00BCD4), modifier = Modifier.weight(1f))
                    StatCard("Favorites", "${items.count { it.isFavorite }}", Color(0xFFE91E63), modifier = Modifier.weight(1f))
                    StatCard("Categories", "${items.map { it.category }.distinct().size}", Color(0xFF9C27B0), modifier = Modifier.weight(1f))
                }
            }

            // Outfit suggestion
            item {
                Card(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9))
                ) {
                    Row(modifier = Modifier.padding(14.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AutoAwesome, null, tint = Color(0xFF2E7D32), modifier = Modifier.size(28.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text("Today's Suggestion 🌟", style = MaterialTheme.typography.titleSmall, fontWeight = FontWeight.Bold, color = Color(0xFF1B5E20))
                            Text("White Shirt + Blue Jeans + White Sneakers", style = MaterialTheme.typography.bodySmall, color = Color(0xFF2E7D32))
                        }
                    }
                }
            }

            // Category filter
            item {
                Text("Browse by Category", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.SemiBold, modifier = Modifier.padding(16.dp, 16.dp, 16.dp, 8.dp))
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(categories) { cat ->
                        FilterChip(
                            selected = selectedCategory == cat,
                            onClick = { selectedCategory = cat },
                            label = { Text(cat) }
                        )
                    }
                }
            }

            item {
                Text("${filtered.size} items", style = MaterialTheme.typography.labelMedium, color = MaterialTheme.colorScheme.onSurfaceVariant, modifier = Modifier.padding(16.dp, 12.dp, 16.dp, 4.dp))
            }

            items(filtered.chunked(2)) { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { item ->
                        ClosetItemCard(item, modifier = Modifier.weight(1f))
                    }
                    if (rowItems.size == 1) Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
    }

    if (showAddDialog) {
        AlertDialog(
            onDismissRequest = { showAddDialog = false },
            title = { Text("Add to Closet") },
            text = { Text("Take a photo of your clothing item or upload from gallery to add it to your virtual closet.") },
            confirmButton = {
                Button(onClick = { showAddDialog = false }) { Text("Take Photo") }
            },
            dismissButton = {
                TextButton(onClick = { showAddDialog = false }) { Text("Cancel") }
            }
        )
    }
}

@Composable
fun StatCard(label: String, value: String, color: Color, modifier: Modifier = Modifier) {
    Card(modifier = modifier, colors = CardDefaults.cardColors(containerColor = color.copy(alpha = 0.1f))) {
        Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Text(value, style = MaterialTheme.typography.headlineSmall, fontWeight = FontWeight.Bold, color = color)
            Text(label, style = MaterialTheme.typography.labelSmall, color = color.copy(alpha = 0.8f))
        }
    }
}

@Composable
fun ClosetItemCard(item: ClothingItem, modifier: Modifier = Modifier) {
    var favorite by remember { mutableStateOf(item.isFavorite) }
    Card(modifier = modifier.clickable {}, elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)) {
        Column(modifier = Modifier.padding(10.dp)) {
            Box(
                modifier = Modifier.fillMaxWidth().height(90.dp).clip(RoundedCornerShape(8.dp)).background(item.color.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Box(modifier = Modifier.size(50.dp).clip(RoundedCornerShape(8.dp)).background(item.color))
                IconButton(
                    onClick = { favorite = !favorite },
                    modifier = Modifier.align(Alignment.TopEnd).size(28.dp)
                ) {
                    Icon(if (favorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder, null, tint = Color(0xFFE91E63), modifier = Modifier.size(16.dp))
                }
            }
            Spacer(modifier = Modifier.height(6.dp))
            Text(item.brand, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            Text(item.name, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.SemiBold, maxLines = 2)
            Spacer(modifier = Modifier.height(4.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(10.dp).clip(RoundedCornerShape(50)).background(item.color))
                Spacer(modifier = Modifier.width(4.dp))
                Text(item.colorName, style = MaterialTheme.typography.labelSmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
            Text("Worn ${item.timesWorn}x", style = MaterialTheme.typography.labelSmall, color = Color(0xFF00BCD4))
        }
    }
}
