package com.adadapted.composeadapted

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.adadapted.android.sdk.core.view.AdadaptedComposable
import com.adadapted.composeadapted.ui.theme.ComposeAdaptedTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShoppingListView(navController: NavController) {
    var selectedTabIndex by remember { mutableIntStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Shopping List") })
        }
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {

            // Tabs
            TabRow(
                selectedTabIndex = selectedTabIndex,
                modifier = Modifier.fillMaxWidth()
            ) {
                Tab(
                    text = { Text("Tab 1") },
                    selected = selectedTabIndex == 0,
                    onClick = { selectedTabIndex = 0 }
                )
                Tab(
                    text = { Text("Tab 2") },
                    selected = selectedTabIndex == 1,
                    onClick = { selectedTabIndex = 1 }
                )
                Tab(
                    text = { Text("Tab 3") },
                    selected = selectedTabIndex == 2,
                    onClick = { selectedTabIndex = 2 }
                )
            }

            // Content for the selected tab
            when (selectedTabIndex) {
                0 -> TabContent("101990") //101990 / 102110
                1 -> TabContent("102166") //102166 / 110002
                2 -> TabContent("101990")
            }
        }
    }
}

@Composable
fun TabContent(
    zoneId: String
) {
    val viewModel = ShoppingListViewModel()
    val shoppingItems by viewModel.shoppingItems.collectAsState()
    val isRefreshing by viewModel.isRefreshing.collectAsState()
    var newItem by remember { mutableStateOf(TextFieldValue("")) }

    Column(modifier = Modifier.padding(8.dp)) {
        // Refresh Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Button(
                onClick = {
                    viewModel.refreshList()  // Trigger data refresh
                },
                enabled = !isRefreshing
            ) {
                Text(if (isRefreshing) "Refreshing..." else "Refresh")
            }
        }

        // Shopping List
        LazyColumn(modifier = Modifier.weight(1f)) {
            items(shoppingItems) { item ->
                Text(item, modifier = Modifier.padding(8.dp))
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Add New Item
        Row(verticalAlignment = Alignment.CenterVertically) {
            BasicTextField(
                value = newItem,
                onValueChange = { newItem = it },
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
                    .padding(8.dp)
                    .border(BorderStroke(1.dp, Color.Gray))
                    .background(Color.White),
            )

            Button(onClick = {
                if (newItem.text.isNotBlank()) {
                    viewModel.addItem(newItem.text)
                }
            }) {
                Text("Add Item")
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ZoneView Component
        AdadaptedComposable(LocalContext.current).ZoneView(zoneId, viewModel, viewModel, isFixedAspectRatioEnabled = true, modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(start = 4.dp, end = 4.dp))
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
