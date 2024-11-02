package com.adadapted.composeadapted

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "home") {
        composable("home") {
            Scaffold(
                topBar = {
                    TopAppBar(title = { Text("Compose Adapted", color = Color(0xFFFFA500)) })
                }
            ) { innerPadding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text("Test app for Jetpack Compose implementation", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = { navController.navigate("shoppingList") },
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text("Go to List View", color = Color.Blue)
                    }

                    Button(
                        onClick = { navController.navigate("offScreenZone") },
                        modifier = Modifier.padding(vertical = 8.dp)
                    ) {
                        Text("Go to Off Screen View", color = Color.Blue)
                    }

                    Spacer(modifier = Modifier.weight(1f))
                }
            }
        }
        composable("shoppingList") { ShoppingListView(navController) }
        composable("offScreenZone") { OffScreenZoneView() }
    }
}
