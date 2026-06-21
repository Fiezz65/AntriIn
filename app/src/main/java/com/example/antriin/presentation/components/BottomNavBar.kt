package com.example.antriin.presentation.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.example.antriin.presentation.theme.PrimaryOrange
import com.example.antriin.presentation.theme.TextGray

@Composable
fun BottomNavBar(
    currentRoute: String,
    onNavigate: (String) -> Unit,
    isSeller: Boolean
) {
    NavigationBar(
        containerColor = Color.White
    ) {
        if (isSeller) {
            NavigationBarItem(
                selected = currentRoute == "dashboard",
                onClick = { onNavigate("dashboard") },
                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                label = { Text("Dasbor") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryOrange,
                    selectedTextColor = PrimaryOrange,
                    unselectedIconColor = TextGray,
                    unselectedTextColor = TextGray,
                    indicatorColor = Color(0xFFFDECE2)
                )
            )
            NavigationBarItem(
                selected = currentRoute == "menu",
                onClick = { onNavigate("menu") },
                icon = { Icon(Icons.Default.List, contentDescription = null) },
                label = { Text("Menu") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryOrange,
                    selectedTextColor = PrimaryOrange,
                    unselectedIconColor = TextGray,
                    unselectedTextColor = TextGray,
                    indicatorColor = Color(0xFFFDECE2)
                )
            )
            NavigationBarItem(
                selected = currentRoute == "history",
                onClick = { onNavigate("history") },
                icon = { Icon(Icons.Default.List, contentDescription = null) },
                label = { Text("Riwayat") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryOrange,
                    selectedTextColor = PrimaryOrange,
                    unselectedIconColor = TextGray,
                    unselectedTextColor = TextGray,
                    indicatorColor = Color(0xFFFDECE2)
                )
            )
        } else {
            NavigationBarItem(
                selected = currentRoute == "home",
                onClick = { onNavigate("home") },
                icon = { Icon(Icons.Default.Home, contentDescription = null) },
                label = { Text("Beranda") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryOrange,
                    selectedTextColor = PrimaryOrange,
                    unselectedIconColor = TextGray,
                    unselectedTextColor = TextGray,
                    indicatorColor = Color(0xFFFDECE2)
                )
            )
            NavigationBarItem(
                selected = currentRoute == "cart",
                onClick = { onNavigate("cart") },
                icon = { Icon(Icons.Default.ShoppingCart, contentDescription = null) },
                label = { Text("Keranjang") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryOrange,
                    selectedTextColor = PrimaryOrange,
                    unselectedIconColor = TextGray,
                    unselectedTextColor = TextGray,
                    indicatorColor = Color(0xFFFDECE2)
                )
            )
            NavigationBarItem(
                selected = currentRoute == "tracking",
                onClick = { onNavigate("tracking") },
                icon = { Icon(Icons.Default.List, contentDescription = null) },
                label = { Text("Antrean") },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = PrimaryOrange,
                    selectedTextColor = PrimaryOrange,
                    unselectedIconColor = TextGray,
                    unselectedTextColor = TextGray,
                    indicatorColor = Color(0xFFFDECE2)
                )
            )
        }

        NavigationBarItem(
            selected = currentRoute == "profile",
            onClick = { onNavigate("profile") },
            icon = { Icon(Icons.Default.Person, contentDescription = null) },
            label = { Text("Profil") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = PrimaryOrange,
                selectedTextColor = PrimaryOrange,
                unselectedIconColor = TextGray,
                unselectedTextColor = TextGray,
                indicatorColor = Color(0xFFFDECE2)
            )
        )
    }
}