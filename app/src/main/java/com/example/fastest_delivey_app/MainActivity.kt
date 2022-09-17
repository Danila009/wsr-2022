package com.example.fastest_delivey_app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.fastest_delivey_app.screen.*
import com.example.fastest_delivey_app.ui.theme.FastestdeliveyappTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            FastestdeliveyappTheme {

                val navController = rememberNavController()

                NavHost(
                    navController = navController,
                    startDestination = "start_screen",
                    builder = {
                        composable("start_screen"){
                            StartScreen(navController = navController)
                        }
                        composable("pager_screen"){
                            PagerScreen(navController = navController)
                        }
                        composable("reg_screen"){
                            RegistScreen(navController = navController)
                        }
                        composable("main_screen"){
                            MainScreen(navController = navController)
                        }
                        composable("auth_screen"){
                            AuthScreen(navController = navController)
                        }
                    }
                )
            }
        }
    }
}