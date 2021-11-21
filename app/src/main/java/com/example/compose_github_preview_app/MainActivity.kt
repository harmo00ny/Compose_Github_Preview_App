package com.example.compose_github_preview_app

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.compose_github_preview_app.ui.screen.home.HomeScreen
import com.example.compose_github_preview_app.ui.theme.Compose_GitHub_Preview_AppTheme
import com.orhanobut.logger.Logger
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Logger.i("Launch")
        setContent {
            Compose_GitHub_Preview_AppTheme {
                App()
            }
        }
    }
}

@Composable
fun App() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") { HomeScreen(navController, hiltViewModel()) }
        composable(
            "web-view/?url={url}",
            arguments = listOf(navArgument("url") { type = NavType.StringType }),
        ) { backStackEntry ->
//            WebViewScreen(url = backStackEntry.arguments?.getString("url") ?: "")
        }
    }
}