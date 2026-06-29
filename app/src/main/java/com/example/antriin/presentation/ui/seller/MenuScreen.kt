package com.example.antriin.presentation.ui.seller

import com.example.antriin.presentation.viewmodel.seller.MenuViewModel
import com.example.antriin.presentation.viewmodel.seller.SellerNotificationViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.verticalScroll
import androidx.compose.ui.draw.scale
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
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
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.antriin.di.ViewModelFactory
import com.example.antriin.domain.model.Menu
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
    viewModel: MenuViewModel = viewModel(factory = ViewModelFactory.Factory),
    notificationViewModel: SellerNotificationViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    val menuState by viewModel.sellerMenus.collectAsState()
    val menus = menuState.menus
    val notificationCount by notificationViewModel.unreadCount.collectAsState()
    val context = androidx.compose.ui.platform.LocalContext.current

    LaunchedEffect(Unit) {
        viewModel.refresh()
        notificationViewModel.startGlobalListener(context)
    }

    var showMenuSheet by rememberSaveable { mutableStateOf(false) }
    var editMenuId by rememberSaveable { mutableStateOf("") }
    var menuName by rememberSaveable { mutableStateOf("") }
    var menuPrice by rememberSaveable { mutableStateOf("") }
    var menuDesc by rememberSaveable { mutableStateOf("") }
    var menuCategory by rememberSaveable { mutableStateOf("") }
    var menuIcon by rememberSaveable { mutableStateOf("🍽️") }
    var soldOut by rememberSaveable { mutableStateOf(false) }
    var menuToDelete by remember { mutableStateOf<Menu?>(null) }

    val iconOptions = listOf("🍽️", "🍚", "🍛", "🍜", "🍔", "🍟", "🌭", "☕", "🥛", "🍵", "🧋", "🧃", "🍹")
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
                    .navigationBarsPadding()
                    .imePadding()
                    .verticalScroll(rememberScrollState())
            ) {
                Text(text = "Tambah/Edit Menu", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextBlack)
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
                var expandedCategory by remember { mutableStateOf(false) }
                val categories = listOf("Makanan", "Minuman", "Cemilan")
                
                ExposedDropdownMenuBox(
                    expanded = expandedCategory,
                    onExpandedChange = { expandedCategory = !expandedCategory }
                ) {
                    Column(modifier = Modifier.fillMaxWidth().menuAnchor(androidx.compose.material3.ExposedDropdownMenuAnchorType.PrimaryNotEditable)) {
                        Text(
                            text = "Kategori",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = TextBlack,
                            modifier = Modifier.padding(bottom = 8.dp)
                        )
                        androidx.compose.material3.OutlinedTextField(
                            value = menuCategory,
                            onValueChange = {},
                            placeholder = { Text(text = "Pilih Kategori", color = TextGray) },
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedCategory) },
                            singleLine = true,
                            shape = RoundedCornerShape(12.dp),
                            colors = androidx.compose.material3.OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = PrimaryOrange,
                                unfocusedBorderColor = Color.LightGray,
                                cursorColor = PrimaryOrange
                            ),
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                    ExposedDropdownMenu(
                        expanded = expandedCategory,
                        onDismissRequest = { expandedCategory = false },
                        containerColor = Color.White
                    ) {
                        categories.forEach { selectionOption ->
                            DropdownMenuItem(
                                text = { Text(selectionOption) },
                                onClick = {
                                    menuCategory = selectionOption
                                    expandedCategory = false
                                }
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
                CustomTextField(
                    value = menuDesc,
                    onValueChange = { if (it.length <= 100) menuDesc = it },
                    label = "Deskripsi Singkat (Maks. 100 Karakter)",
                    placeholder = "Contoh: Nasi goreng pedas dengan telur"
                )
                Text(
                    text = "${menuDesc.length}/100",
                    fontSize = 12.sp,
                    color = TextGray,
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 4.dp, end = 4.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                PrimaryButton(
                    text = "SIMPAN MENU",
                    onClick = {
                        val menu = Menu(
                            id = editMenuId,
                            name = menuName,
                            price = menuPrice.toIntOrNull() ?: 0,
                            category = menuCategory,
                            description = menuDesc,
                            icon = menuIcon,
                            soldOut = soldOut
                        )
                        if (editMenuId.isEmpty()) {
                            viewModel.addMenu(menu) {
                                android.widget.Toast.makeText(context, "Menu berhasil ditambahkan!", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            viewModel.updateMenu(menu) {
                                android.widget.Toast.makeText(context, "Menu berhasil diubah!", android.widget.Toast.LENGTH_SHORT).show()
                            }
                        }
                        showMenuSheet = false
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (menuToDelete != null) {
        AlertDialog(
            onDismissRequest = { menuToDelete = null },
            title = { Text("Hapus Menu", fontWeight = FontWeight.Bold) },
            text = { Text("Apakah Anda yakin ingin menghapus menu ${menuToDelete?.name}?") },
            containerColor = Color.White,
            confirmButton = {
                TextButton(
                    onClick = {
                        menuToDelete?.let {
                            viewModel.deleteMenu(it.id)
                            android.widget.Toast.makeText(context, "Menu berhasil dihapus!", android.widget.Toast.LENGTH_SHORT).show()
                        }
                        menuToDelete = null
                    }
                ) {
                    Text("Hapus", color = Color.Red, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { menuToDelete = null }) {
                    Text("Batal", color = TextGray)
                }
            }
        )
    }

    Scaffold(
        bottomBar = { BottomNavBar(currentRoute = "menu", onNavigate = onTabNavigate, isSeller = true) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    editMenuId = ""
                    menuName = ""
                    menuPrice = ""
                    menuDesc = ""
                    menuCategory = ""
                    menuIcon = "🍽️"
                    soldOut = false
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
        ) {
            item {
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
                        BadgedBox(
                            badge = {
                                if (notificationCount > 0) {
                                    Badge(containerColor = Color.Red, contentColor = Color.White) {
                                        Text(text = notificationCount.toString())
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = PrimaryOrange)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Kelola Menu", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                Text(text = "Daftar menu makanan dan minuman Anda.", fontSize = 14.sp, color = TextGray)
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (menus.isEmpty()) {
                item {
                    com.example.antriin.presentation.components.EmptyState(
                        title = "Belum Ada Menu",
                        message = "Mulai tambahkan menu jualan Anda sekarang!",
                        modifier = Modifier.padding(top = 160.dp)
                    )
                }
            } else {
                items(menus, key = { it.id }) { menu ->
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(64.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFFDECE2)), 
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = menu.icon, fontSize = 28.sp)
                                }
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(text = menu.name, fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextBlack)
                                    Text(text = formatRupiah(menu.price), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = PrimaryOrange)
                                    Text(text = menu.description, fontSize = 12.sp, color = TextGray, modifier = Modifier.padding(top = 4.dp), maxLines = 3, overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis)
                                }
                                Column(horizontalAlignment = Alignment.End) {
                                    Row {
                                        IconButton(onClick = {
                                            editMenuId = menu.id
                                            menuName = menu.name
                                            menuPrice = menu.price.toString()
                                            menuDesc = menu.description
                                            menuCategory = menu.category
                                            menuIcon = menu.icon
                                            soldOut = menu.soldOut
                                            showMenuSheet = true
                                        }) { Icon(Icons.Default.Edit, contentDescription = null, tint = TextGray) }
                                        IconButton(onClick = { 
                                            menuToDelete = menu
                                        }) { Icon(Icons.Default.Delete, contentDescription = null, tint = Color.Red) }
                                    }
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Switch(
                                            checked = menu.soldOut,
                                            onCheckedChange = { newSoldOut ->
                                                viewModel.updateMenu(menu.copy(soldOut = newSoldOut))
                                            },
                                            colors = SwitchDefaults.colors(
                                                checkedThumbColor = Color.White,
                                                checkedTrackColor = Color.Red,
                                                uncheckedThumbColor = Color.White,
                                                uncheckedTrackColor = Color(0xFF4CAF50)
                                            ),
                                            modifier = Modifier.scale(0.7f).height(24.dp)
                                        )
                                        Text(text = if (menu.soldOut) "Habis" else "Tersedia", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (menu.soldOut) Color.Red else Color(0xFF4CAF50), modifier = Modifier.padding(top = 4.dp))
                                    }
                                }
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(80.dp)) }
            }
        }
    }