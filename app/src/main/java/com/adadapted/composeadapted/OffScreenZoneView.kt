package com.adadapted.composeadapted

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import com.adadapted.android.sdk.core.view.AdadaptedComposable

@SuppressLint("UnrememberedMutableState")
@Composable
fun OffScreenZoneView() {
    val isZoneViewOneVisible = remember { mutableStateOf(false) }
    val isZoneViewTwoVisible = remember { mutableStateOf(false) }
    val zoneContextId = remember { mutableStateOf("organic") }
    val scrollState = rememberScrollState()
    val screenHeightPx = with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp.dp.toPx() }

    Column(modifier = Modifier
        .padding(16.dp)
        .verticalScroll(scrollState)
    ) {
        // Zone View One
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .onGloballyPositioned { layoutCoordinates ->
                    val isVisible = layoutCoordinates.isVisible(screenHeightPx)
                    if (isVisible != isZoneViewOneVisible.value) {
                        isZoneViewOneVisible.value = isVisible
                    }
                }
        ) {
            AdadaptedComposable(LocalContext.current).ZoneView("110002", null, null, isZoneViewOneVisible, zoneContextId)
        }
            Spacer(modifier = Modifier.height(16.dp))

        //dummy items
        repeat(5) { index ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Text(
                    text = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
                    modifier = Modifier
                        .padding(16.dp)
                        .background(Color.Gray.copy(alpha = 0.1f))
                        .padding(16.dp),
                    fontSize = 16.sp
                )

                Button(
                    onClick = {
                        zoneContextId.value = ""
                        println("Button ${index + 1} tapped")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                ) {
                    Text(text = "Interaction")
                }
            }
        }

        // Zone View Two
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
                .onGloballyPositioned { layoutCoordinates ->
                    val isVisible = layoutCoordinates.isVisible(screenHeightPx)
                    if (isVisible != isZoneViewTwoVisible.value) {
                        isZoneViewTwoVisible.value = isVisible
                    }
                }
        ) {
            AdadaptedComposable(LocalContext.current).ZoneView("102110", null, null, isZoneViewTwoVisible)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

private fun LayoutCoordinates.isVisible(screenHeightPx: Float): Boolean {
    val bounds = this.boundsInWindow()

    // Check if the bounds of the layout are within the visible area of the screen
    return bounds.bottom > 0 && bounds.top < screenHeightPx
}

// Preview function for development
@Preview(showBackground = true)
@Composable
fun OffScreenZoneViewPreview() {
    OffScreenZoneView()
}
