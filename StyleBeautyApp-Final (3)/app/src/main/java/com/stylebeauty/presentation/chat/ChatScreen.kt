package com.stylebeauty.assistant.presentation.chat

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

data class ChatMessage(
    val content: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(navController: NavController) {
    val messages = remember {
        mutableStateListOf(
            ChatMessage(
                "Hi! I'm Bella, your personal AI stylist 💅 I'm here to help you with makeup, fashion, skincare, and anything style-related. What can I help you with today?",
                isUser = false
            )
        )
    }
    var inputText by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()

    val quickPrompts = listOf(
        "What makeup suits my face shape?",
        "Outfit ideas for a date night",
        "Skincare routine for glowing skin",
        "How to style my hair?"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .clip(CircleShape)
                                .background(MaterialTheme.colorScheme.primaryContainer),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("B", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text("Bella", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                            Text("AI Stylist • Online", style = MaterialTheme.typography.labelSmall, color = Color(0xFF4CAF50))
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            // Messages list
            LazyColumn(
                state = listState,
                modifier = Modifier.weight(1f).padding(horizontal = 12.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(vertical = 8.dp)
            ) {
                items(messages) { message ->
                    MessageBubble(message)
                }
                if (isLoading) {
                    item {
                        Row(modifier = Modifier.padding(start = 8.dp)) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(MaterialTheme.colorScheme.surfaceVariant)
                                    .padding(12.dp)
                            ) {
                                Text("Bella is typing...", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                    }
                }
            }

            // Quick prompts (show only at start)
            if (messages.size == 1) {
                LazyColumn(
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(quickPrompts) { prompt ->
                        SuggestionChip(
                            onClick = { inputText = prompt },
                            label = { Text(prompt, style = MaterialTheme.typography.bodySmall) },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            HorizontalDivider()

            // Input row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = inputText,
                    onValueChange = { inputText = it },
                    placeholder = { Text("Ask Bella anything...") },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(24.dp),
                    maxLines = 3
                )
                Spacer(modifier = Modifier.width(8.dp))
                FloatingActionButton(
                    onClick = {
                        if (inputText.isNotBlank() && !isLoading) {
                            val userMsg = inputText.trim()
                            messages.add(ChatMessage(userMsg, isUser = true))
                            inputText = ""
                            isLoading = true
                            coroutineScope.launch {
                                listState.animateScrollToItem(messages.size - 1)
                                val reply = callClaudeApi(messages.dropLast(1).takeLast(10), userMsg)
                                messages.add(ChatMessage(reply, isUser = false))
                                isLoading = false
                                listState.animateScrollToItem(messages.size - 1)
                            }
                        }
                    },
                    modifier = Modifier.size(48.dp),
                    containerColor = MaterialTheme.colorScheme.primary
                ) {
                    Icon(Icons.Default.Send, "Send", tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
        }
    }
}

@Composable
fun MessageBubble(message: ChatMessage) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = if (message.isUser) Arrangement.End else Arrangement.Start
    ) {
        if (!message.isUser) {
            Box(
                modifier = Modifier
                    .size(32.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text("B", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimaryContainer, style = MaterialTheme.typography.labelMedium)
            }
            Spacer(modifier = Modifier.width(8.dp))
        }
        Box(
            modifier = Modifier
                .clip(
                    RoundedCornerShape(
                        topStart = if (message.isUser) 16.dp else 4.dp,
                        topEnd = if (message.isUser) 4.dp else 16.dp,
                        bottomStart = 16.dp,
                        bottomEnd = 16.dp
                    )
                )
                .background(
                    if (message.isUser) MaterialTheme.colorScheme.primary
                    else MaterialTheme.colorScheme.surfaceVariant
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
                .widthIn(max = 280.dp)
        ) {
            Text(
                message.content,
                color = if (message.isUser) Color.White else MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

suspend fun callClaudeApi(history: List<ChatMessage>, newMessage: String): String {
    return withContext(Dispatchers.IO) {
        try {
            val url = URL("https://api.anthropic.com/v1/messages")
            val connection = url.openConnection() as HttpURLConnection
            connection.requestMethod = "POST"
            connection.setRequestProperty("Content-Type", "application/json")
            connection.setRequestProperty("x-api-key", "your_api_key_here")
            connection.setRequestProperty("anthropic-version", "2023-06-01")
            connection.doOutput = true
            connection.connectTimeout = 30000
            connection.readTimeout = 60000

            val messagesArray = JSONArray()
            history.forEach { msg ->
                val msgObj = JSONObject()
                msgObj.put("role", if (msg.isUser) "user" else "assistant")
                msgObj.put("content", msg.content)
                messagesArray.put(msgObj)
            }
            val newMsg = JSONObject()
            newMsg.put("role", "user")
            newMsg.put("content", newMessage)
            messagesArray.put(newMsg)

            val body = JSONObject()
            body.put("model", "claude-sonnet-4-6")
            body.put("max_tokens", 1024)
            body.put("system", "You are Bella, a warm and knowledgeable AI beauty and style advisor. You help with makeup, fashion, skincare, and hair advice. Be friendly, encouraging, and give practical tips. Keep responses concise but helpful.")
            body.put("messages", messagesArray)

            connection.outputStream.write(body.toString().toByteArray())

            val responseCode = connection.responseCode
            val stream = if (responseCode == 200) connection.inputStream else connection.errorStream
            val response = stream.bufferedReader().readText()

            val jsonResponse = JSONObject(response)
            jsonResponse.getJSONArray("content").getJSONObject(0).getString("text")
        } catch (e: Exception) {
            "I'm having trouble connecting right now. Please check your internet connection and try again. (${e.message})"
        }
    }
}
