package com.example.antriin.presentation.viewmodel.seller

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antriin.domain.model.Menu
import com.example.antriin.domain.repository.MenuRepository
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

data class MenuState(
    val menus: List<Menu> = emptyList(),
    val lastUpdated: Long = 0L
)

class MenuViewModel(
    private val menuRepository: MenuRepository
) : ViewModel() {

    private val _sellerMenus = MutableStateFlow(MenuState())
    val sellerMenus: StateFlow<MenuState> = _sellerMenus

    private var fetchJob: kotlinx.coroutines.Job? = null

    init {
        refresh()
    }

    fun refresh() {
        val sellerId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        fetchJob?.cancel()
        fetchJob = viewModelScope.launch {
            try {
                menuRepository.getMenus(sellerId).collect { menus ->
                    _sellerMenus.value = MenuState(menus, System.currentTimeMillis())
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun addMenu(menu: Menu, onSuccess: () -> Unit = {}) {
        val sellerId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val newMenu = menu.copy(sellerId = sellerId)
        viewModelScope.launch {
            try {
                menuRepository.addMenu(newMenu)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun updateMenu(menu: Menu, onSuccess: () -> Unit = {}) {
        val sellerId = FirebaseAuth.getInstance().currentUser?.uid ?: return
        val updatedMenu = menu.copy(sellerId = sellerId)
        viewModelScope.launch {
            try {
                menuRepository.updateMenu(updatedMenu)
                onSuccess()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun deleteMenu(menuId: String, onSuccess: () -> Unit = {}) {
        viewModelScope.launch {
            menuRepository.deleteMenu(menuId)
            onSuccess()
        }
    }
}
