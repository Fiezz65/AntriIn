package com.example.antriin.presentation.seller

import androidx.lifecycle.ViewModel
import com.example.antriin.domain.model.Menu
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class MenuViewModel : ViewModel() {

    private val _sellerMenus = MutableStateFlow<List<Menu>>(emptyList())
    val sellerMenus: StateFlow<List<Menu>> = _sellerMenus

    init {
        loadDummyMenus()
    }

    private fun loadDummyMenus() {
        _sellerMenus.value = listOf(
            Menu(id = "1", name = "Mie Ayam Spesial", description = "Mie ayam dengan topping jamur, ayam kecap.", price = 15000, isSoldOut = false, icon = "🍜"),
            Menu(id = "2", name = "Es Teh Manis", description = "Es teh manis segar ukuran jumbo.", price = 4000, isSoldOut = false, icon = "🍹"),
            Menu(id = "3", name = "Burger Kampung", description = "Roti burger isi telur dada, sayur, saus.", price = 12000, isSoldOut = true, icon = "🍔")
        )
    }
}