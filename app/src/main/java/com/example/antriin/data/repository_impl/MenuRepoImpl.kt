package com.example.antriin.data.repository_impl

import com.example.antriin.domain.model.Menu
import com.example.antriin.domain.repository.MenuRepository
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class MenuRepoImpl(
    private val database: FirebaseDatabase
) : MenuRepository {

    private val menusRef = database.getReference("menus")

    override fun getMenus(sellerId: String): Flow<List<Menu>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val menuList = mutableListOf<Menu>()
                for (child in snapshot.children) {
                    val menu = child.getValue(Menu::class.java)
                    if (menu != null && menu.sellerId == sellerId) {
                        menuList.add(menu)
                    }
                }
                trySend(menuList)
            }

            override fun onCancelled(error: DatabaseError) {
                close()
            }
        }
        menusRef.addValueEventListener(listener)
        awaitClose { menusRef.removeEventListener(listener) }
    }

    override suspend fun addMenu(menu: Menu) {
        val key = menusRef.push().key ?: return
        val newMenu = menu.copy(id = key)
        menusRef.child(key).setValue(newMenu).await()
    }

    override suspend fun updateMenu(menu: Menu) {
        if (menu.id.isNotEmpty()) {
            menusRef.child(menu.id).setValue(menu).await()
        }
    }

    override suspend fun deleteMenu(menuId: String) {
        if (menuId.isNotEmpty()) {
            menusRef.child(menuId).removeValue().await()
        }
    }

    override fun getAllMenus(): Flow<List<Menu>> = callbackFlow {
        val listener = object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val menuList = mutableListOf<Menu>()
                for (child in snapshot.children) {
                    val menu = child.getValue(Menu::class.java)
                    if (menu != null) {
                        menuList.add(menu)
                    }
                }
                trySend(menuList)
            }

            override fun onCancelled(error: DatabaseError) {
                close()
            }
        }
        menusRef.addValueEventListener(listener)
        awaitClose { menusRef.removeEventListener(listener) }
    }
}
