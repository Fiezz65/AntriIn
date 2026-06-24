package com.example.antriin.presentation.seller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antriin.domain.model.Menu
import com.example.antriin.domain.repository.MenuRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MenuViewModel(
    private val menuRepository: MenuRepository
) : ViewModel() {

    private val _sellerMenus = MutableStateFlow<List<Menu>>(emptyList())
    val sellerMenus: StateFlow<List<Menu>> = _sellerMenus

    init {
        loadMenus()
    }

    private fun loadMenus() {
        val sellerId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        viewModelScope.launch {
            menuRepository.getMenus(sellerId).collect { menus ->
                _sellerMenus.value = menus
            }
        }
    }

    fun addMenu(menu: Menu, onSuccess: () -> Unit = {}) {
        val sellerId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val newMenu = menu.copy(sellerId = sellerId)
        viewModelScope.launch {
            menuRepository.addMenu(newMenu)
            onSuccess()
        }
    }

    fun updateMenu(menu: Menu, onSuccess: () -> Unit = {}) {
        val sellerId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val updatedMenu = menu.copy(sellerId = sellerId)
        viewModelScope.launch {
            menuRepository.updateMenu(updatedMenu)
            onSuccess()
        }
    }

    fun deleteMenu(menuId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            menuRepository.deleteMenu(menuId)
            onSuccess()
        }
    }
}