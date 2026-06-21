package com.example.antriin.presentation.seller

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.antriin.presentation.components.BottomNavBar
import com.example.antriin.presentation.components.CustomTextField
import com.example.antriin.presentation.components.PrimaryButton
import com.example.antriin.presentation.theme.BackgroundLight
import com.example.antriin.presentation.theme.PrimaryOrange
import com.example.antriin.presentation.theme.TextBlack
import com.example.antriin.presentation.theme.TextGray
import com.example.antriin.utils.formatRupiah

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    onNavigate: (String) -> Unit,
    onTabNavigate: (String) -> Unit,
    viewModel: MenuViewModel = viewModel()
) {
    val menus by viewModel.sellerMenus.collectAsState()

    var showMenuSheet by remember { mutableStateOf(false) }
    var menuName by remember { mutableStateOf("") }
    var menuPrice by remember { mutableStateOf("") }
    var menuDesc by remember { mutableStateOf("") }
    var menuCategory by remember { mutableStateOf("") }
    var menuIcon by remember { mutableStateOf("🍽️") }
    var isSoldOut by remember { mutableStateOf(false) }

    val iconOptions = listOf("🍽️", "🍚", "🍜", "🍲", "🍔", "🍟", "🍕", "🍹", "☕", "🍰")
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    if (showMenuSheet) {
        ModalBottomSheet(
            onDismissRequest = { showMenuSheet = false },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Tambah/Edit Menu", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextBlack)

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "Habis", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = if (isSoldOut) Color.Red else TextGray)
                        Spacer(modifier = Modifier.width(8.dp))
                        Switch(
                            checked = isSoldOut,
                            onCheckedChange = { isSoldOut = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color.Red,
                                uncheckedThumbColor = Color.White,
                                uncheckedTrackColor = Color.LightGray
                            )
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "Pilih Ikon Menu", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextBlack)
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    iconOptions.forEach { icon ->
                        Box(
                            modifier = Modifier
                                .size(48.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (menuIcon == icon) Color(0xFFFDECE2) else Color(0xFFEEEEEE))
                                .border(
                                    width = if (menuIcon == icon) 2.dp else 0.dp,
                                    color = if (menuIcon == icon) PrimaryOrange else Color.Transparent,
                                    shape = RoundedCornerShape(8.dp)
                                )
                                .clickable { menuIcon = icon },
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = icon, fontSize = 24.sp)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    value = menuName,
                    onValueChange = { menuName = it },
                    label = "Nama Menu",
                    placeholder = "Contoh: Nasi Goreng Gila"
                )
                Spacer(modifier = Modifier.height(12.dp))
                CustomTextField(
                    value = menuPrice,
                    onValueChange = { menuPrice = it },
                    label = "Harga (Rp)",
                    placeholder = "Contoh: 15000",
                    keyboardType = KeyboardType.Number
                )
                Spacer(modifier = Modifier.height(12.dp))
                CustomTextField(
                    value = menuCategory,
                    onValueChange = { menuCategory = it },
                    label = "Kategori",
                    placeholder = "Contoh: Nasi, Mie, Minuman"
                )
                Spacer(modifier = Modifier.height(12.dp))
                CustomTextField(
                    value = menuDesc,
                    onValueChange = { menuDesc = it },
                    label = "Deskripsi Singkat",
                    placeholder = "Contoh: Nasi goreng pedas"
                )
                Spacer(modifier = Modifier.height(24.dp))
                PrimaryButton(
                    text = "SIMPAN MENU",
                    onClick = { showMenuSheet = false }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    Scaffold(
        bottomBar = { BottomNavBar(currentRoute = "menu", onNavigate = onTabNavigate, isSeller = true) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    menuName = ""
                    menuPrice = ""
                    menuDesc = ""
                    menuCategory = ""
                    menuIcon = "🍽️"
                    isSoldOut = false
                    showMenuSheet = true
                },
                containerColor = PrimaryOrange,
                contentColor = Color.White,
                shape = RoundedCornerShape(16.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "AntriIn", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryOrange)
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFFDECE2), CircleShape)
                        .clickable { onNavigate("seller_notification") },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(Icons.Default.Notifications, contentDescription = null, tint = PrimaryOrange)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Text(text = "Kelola Menu", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextBlack)
            Text(text = "Daftar menu makanan dan minuman Anda.", fontSize = 14.sp, color = TextGray)
            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(menus) { menu ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                            Box(modifier = Modifier
                                .size(60.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFFFDECE2)), contentAlignment = Alignment.Center) {
                                Text(text = menu.icon, fontSize = 24.sp)
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(text = menu.name, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                                Text(text = formatRupiah(menu.price), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryOrange)
                                Text(text = menu.description, fontSize = 12.sp, color = TextGray, modifier = Modifier.padding(top = 4.dp))
                                if (menu.isSoldOut) {
                                    Text(text = "Habis", fontSize = 10.sp, color = Color.Red, modifier = Modifier
                                        .padding(top = 4.dp)
                                        .background(Color(0xFFFFEBEE), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 2.dp))
                                }
                            }
                            Column {
                                IconButton(onClick = {
                                    menuName = menu.name
                                    menuPrice = menu.price.toString()
                                    menuDesc = menu.description
                                    menuCategory = menu.category
                                    menuIcon = menu.icon
                                    isSoldOut = menu.isSoldOut
                                    showMenuSheet = true
                                }) { Icon(Icons.Default.Edit, contentDescription = null, tint = TextGray) }
                                IconButton(onClick = { }) { Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red) }
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }
}