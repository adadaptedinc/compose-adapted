package com.adadapted.composeadapted

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.adadapted.android.sdk.core.ad.AdContentListener
import com.adadapted.android.sdk.core.atl.AddToListContent
import com.adadapted.android.sdk.core.view.AdadaptedComposable
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ShoppingListViewModel : ViewModel(), AdContentListener, AdadaptedComposable.Listener {
    private val _shoppingItems = MutableStateFlow(listOf("Eggs", "Bread", "Oranges"))
    val shoppingItems: StateFlow<List<String>> = _shoppingItems

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing

    fun addItem(item: String) {
        _shoppingItems.value += item
    }

    fun removeItems(index: Int) {
        _shoppingItems.value = _shoppingItems.value.toMutableList().apply { removeAt(index) }
    }

    fun refreshList() {
        _isRefreshing.value = true
        viewModelScope.launch {
            delay(1000)
            _isRefreshing.value = false
        }
    }

    override fun onContentAvailable(zoneId: String, content: AddToListContent) {
        val items = content.getItems()
        for (item in items) {
            addItem(item.title)
            content.itemAcknowledge(item)
        }
    }

    override fun onAdLoadFailed() {
        var check = true
    }

    override fun onAdLoaded() {
        var check = true
    }

    override fun onZoneHasAds(hasAds: Boolean) {
        var check = hasAds
    }
}
