package com.adadapted.composeadapted

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.adadapted.android.sdk.AdAdapted
import com.adadapted.android.sdk.core.atl.AddToListContent
import com.adadapted.android.sdk.core.atl.AddToListItem
import com.adadapted.android.sdk.core.interfaces.AaSdkAdditContentListener
import com.adadapted.android.sdk.core.interfaces.AaSdkEventListener
import com.adadapted.android.sdk.core.interfaces.AaSdkSessionListener
import com.adadapted.composeadapted.ui.theme.ComposeAdaptedTheme
import java.util.Locale

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeAdAdapted()
        enableEdgeToEdge()
        setContent {
            ComposeAdaptedTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) {
                    HomeView()
                }
            }
        }
    }
    private fun initializeAdAdapted() {
        val tag = "ComposeAdapted"
        AdAdapted
            .withAppId("7D58810X6333241C")
            .inEnv(AdAdapted.Env.DEV)
            .enableKeywordIntercept(true)
            .setSdkSessionListener(createSdkSessionListener(tag))
            .setSdkEventListener(createSdkEventListener(tag))
            .setSdkAdditContentListener(createSdkContentListener())
            .enableDebugLogging()
            .start(this)
    }
    private fun createSdkSessionListener(tag: String) = object : AaSdkSessionListener {
        override fun onHasAdsToServe(hasAds: Boolean, availableZoneIds: List<String>) {
            Log.i(tag, "Has Ads To Serve: $hasAds")
            Log.i(tag, "The following zones have ads to serve: $availableZoneIds")
        }
    }

    private fun createSdkEventListener(tag: String) = object : AaSdkEventListener {
        override fun onNextAdEvent(zoneId: String, eventType: String) {
            Log.i(tag, "Ad $eventType for Zone $zoneId")
        }
    }

    private fun createSdkContentListener() = object : AaSdkAdditContentListener {
        override fun onContentAvailable(content: AddToListContent) {
            val listItems: List<AddToListItem> = content.getItems()
            Toast.makeText(
                applicationContext,
                String.format(
                    Locale.ENGLISH,
                    "%d item(s) received from payload or circular.",
                    listItems.size
                ),
                Toast.LENGTH_LONG
            ).show()
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    ComposeAdaptedTheme {
        HomeView()
    }
}