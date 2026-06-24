package com.example.antriin.domain.repository

import com.example.antriin.domain.model.Menu
import kotlinx.coroutines.flow.Flow

interface MenuRepository {
    fun getMenus(sellerId: String): Flow<List<Menu>>
    suspend fun addMenu(menu: Menu)
    suspend fun updateMenu(menu: Menu)
    suspend fun deleteMenu(menuId: String)
}
