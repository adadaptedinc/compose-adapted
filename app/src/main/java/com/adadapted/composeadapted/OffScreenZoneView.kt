import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.LayoutCoordinates
import androidx.compose.ui.layout.boundsInWindow
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.adadapted.android.sdk.core.view.AaZoneView
import com.adadapted.android.sdk.core.view.AdadaptedComposable
//import com.adadapted.android.sdk.core.view.ZonePadding

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OffScreenTabbedZoneViews() {
    var selectedTabIndex by remember { mutableIntStateOf(0) }
    val tabTitles = listOf("Tab 1", "Tab 2", "Tab 3")

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tabbed Zone Views") },
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
        ) {
            // Tab Row with 3 tabs
            TabRow(selectedTabIndex = selectedTabIndex) {
                tabTitles.forEachIndexed { index, title ->
                    Tab(
                        selected = selectedTabIndex == index,
                        onClick = { selectedTabIndex = index },
                        text = { Text(title) }
                    )
                }
            }

            // Display OffScreenZoneView in the selected tab
            when (selectedTabIndex) {
                0 -> OffScreenZoneView(zoneId = "101990", adZoneId = "102166") //101990 102166 / 110003 102110
                1 -> OffScreenZoneView(zoneId = "110002", adZoneId = "110004")
                2 -> OffScreenZoneView(zoneId = "110005", adZoneId = "110006")
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@Composable
fun OffScreenZoneView(zoneId: String, adZoneId: String) {
    val isZoneViewOneVisible = remember { mutableStateOf(false) }
    val isZoneViewTwoVisible = remember { mutableStateOf(false) }
    val zoneContextId = remember { mutableStateOf("organic") }
    val scrollState = rememberScrollState()
    val screenHeightPx = with(LocalDensity.current) { LocalConfiguration.current.screenHeightDp.dp.toPx() }

    //testing
    val aaZoneView = AaZoneView(LocalContext.current)
    aaZoneView.init(zoneId)
    aaZoneView.setAdZoneVisibility(true)
    aaZoneView.configureFixedAspectRatio(true, 32)
    aaZoneView.onStart()

    Column(
        modifier = Modifier
            .padding(16.dp)
            .verticalScroll(scrollState)
    ) {
        // Zone View One
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .onGloballyPositioned { layoutCoordinates ->
                    val isVisible = layoutCoordinates.isVisible(screenHeightPx)
                    if (isVisible != isZoneViewOneVisible.value) {
                        isZoneViewOneVisible.value = isVisible
                    }
                }
        ) {

//            AndroidView(
//                modifier = Modifier.padding(16.dp),
//                factory = { aaZoneView }
//            )

            AdadaptedComposable(LocalContext.current).ZoneView(
                zoneId,
                null,
                null,
                isZoneViewOneVisible,
                zoneContextId,
                isFixedAspectRatioEnabled = true,
                fixedAspectPaddingOffset = 32,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .padding(16.dp)
            )

        }
        Spacer(modifier = Modifier.height(16.dp))

        // Dummy items
        repeat(5) { index ->
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp)
            ) {
                Text(
                    text = "Sample content $index",
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
            AdadaptedComposable(LocalContext.current).ZoneView(adZoneId, null, null, isZoneViewTwoVisible, isFixedAspectRatioEnabled = true)
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

private fun LayoutCoordinates.isVisible(screenHeightPx: Float): Boolean {
    val bounds = this.boundsInWindow()
    return bounds.bottom > 0 && bounds.top < screenHeightPx
}

@Preview(showBackground = true)
@Composable
fun OffscreenTabbedZoneViewsPreview() {
    OffScreenTabbedZoneViews()
}
