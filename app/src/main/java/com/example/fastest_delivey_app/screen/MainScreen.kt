package com.example.fastest_delivey_app.screen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavController

@Composable
fun MainScreen(
    navController: NavController
) {
    Text(text = token.toString())
}