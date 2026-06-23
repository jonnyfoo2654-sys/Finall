package com.stylebeauty.assistant.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.stylebeauty.assistant.presentation.home.HomeScreen
import com.stylebeauty.assistant.presentation.makeup.MakeupScreen
import com.stylebeauty.assistant.presentation.fashion.FashionScreen
import com.stylebeauty.assistant.presentation.closet.ClosetScreen
import com.stylebeauty.assistant.presentation.chat.ChatScreen
import com.stylebeauty.assistant.presentation.profile.ProfileScreen

sealed class Screen(val route: String) {
    object Home : Screen("home")
    object Makeup : Screen("makeup")
    object Fashion : Screen("fashion")
    object Closet : Screen("closet")
    object Hair : Screen("hair")
    object Skincare : Screen("skincare")
    object Chat : Screen("chat")
    object Profile : Screen("profile")
    object FaceAnalysis : Screen("face_analysis")
    object VirtualTryOn : Screen("virtual_tryon")
}

@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = Screen.Home.route
    ) {
        composable(Screen.Home.route) {
            HomeScreen(navController = navController)
        }
        
        composable(Screen.Makeup.route) {
            MakeupScreen(navController = navController)
        }
        
        composable(Screen.Fashion.route) {
            FashionScreen(navController = navController)
        }
        
        composable(Screen.Closet.route) {
            ClosetScreen(navController = navController)
        }
        
        composable(Screen.Chat.route) {
            ChatScreen(navController = navController)
        }
        
        composable(Screen.Profile.route) {
            ProfileScreen(navController = navController)
        }
    }
}
