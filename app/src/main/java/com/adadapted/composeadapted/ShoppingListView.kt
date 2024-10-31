package com.adadapted.composeadapted

import android.annotation.SuppressLint
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.adadapted.android.sdk.core.view.AdadaptedComposable
import com.adadapted.composeadapted.ui.theme.ComposeAdaptedTheme

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListView(navController: NavController) {
    val viewModel: ShoppingListViewModel = viewModel()
    val shoppingItems by viewModel.shoppingItems.collectAsState()
    var newItem by remember { mutableStateOf(TextFieldValue("")) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Shopping List") }) }
    ) { innerPadding -> // Capture the padding provided by the Scaffold
        Column(modifier = Modifier.padding(innerPadding)) { // Apply the padding to the column
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(shoppingItems) { item ->
                    Text(item, modifier = Modifier.padding(8.dp))
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                BasicTextField(
                    value = newItem,
                    onValueChange = { newItem = it },
                    modifier = Modifier
                        .weight(1f)
                        .height(32.dp)
                        .padding(start = 8.dp, end = 8.dp)
                        .border(BorderStroke(1.dp, Color.Gray)) // Add border
                        .background(Color.White), // Add background color
                )

                Button(onClick = {
                    if (newItem.text.isNotBlank()) {
                        viewModel.addItem(newItem.text)
                        newItem = TextFieldValue("")
                    }
                }) {
                    Text("Add Item")
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            AdadaptedComposable(LocalContext.current).ZoneView("102110", viewModel, viewModel)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ShoppingListViewPreview() {
    ComposeAdaptedTheme {
        val navController = rememberNavController()
        ShoppingListView(navController)
    }
}
