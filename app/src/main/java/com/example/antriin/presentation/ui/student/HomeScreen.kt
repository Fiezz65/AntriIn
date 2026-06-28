package com.example.antriin.presentation.ui.student

import com.example.antriin.presentation.viewmodel.student.CartViewModel
import com.example.antriin.presentation.viewmodel.student.HomeViewModel
import com.example.antriin.presentation.viewmodel.student.StudentNotificationViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.antriin.domain.model.Menu
import com.example.antriin.presentation.components.BottomNavBar
import com.example.antriin.presentation.components.CustomTextField
import com.example.antriin.presentation.components.MenuCard
import com.example.antriin.presentation.components.PrimaryButton
import com.example.antriin.presentation.theme.BackgroundLight
import com.example.antriin.presentation.theme.PrimaryOrange
import com.example.antriin.presentation.theme.TextBlack
import com.example.antriin.presentation.theme.TextGray
import com.example.antriin.utils.formatRupiah

@OptIn(ExperimentalMaterial3Api::class, androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onNavigate: (String) -> Unit,
    onTabNavigate: (String) -> Unit,
    viewModel: HomeViewModel = viewModel(factory = com.example.antriin.di.ViewModelFactory.Factory),
    cartViewModel: CartViewModel = viewModel(factory = com.example.antriin.di.ViewModelFactory.Factory),
    notificationViewModel: StudentNotificationViewModel = viewModel(factory = com.example.antriin.di.ViewModelFactory.Factory)
) {
    val weather by viewModel.weatherInfo.collectAsState()
    val crowdCount by viewModel.crowdCount.collectAsState()

    val crowdStatusBgColor = when {
        crowdCount <= 3 -> Color(0xFFE8F5E9)
        crowdCount < 10 -> Color(0xFFFDECE2)
        else -> Color(0xFFFFEBEE)
    }
    
    val crowdStatusIndicatorColor = when {
        crowdCount <= 3 -> Color(0xFF4CAF50)
        crowdCount < 10 -> PrimaryOrange
        else -> Color.Red
    }
    
    val crowdStatusText = when {
        crowdCount <= 3 -> "Kantin Sepi"
        crowdCount < 10 -> "Kantin Normal"
        else -> "Kantin Ramai"
    }
    val menuState by viewModel.menuList.collectAsState()
    val menuList = menuState.menus
    val locations by viewModel.locations.collectAsState()
    val selectedLocation by viewModel.selectedLocation.collectAsState()
    val userName by viewModel.userName.collectAsState()
    val cartItems by cartViewModel.cartItems.collectAsState()
    val cartTotalPrice by cartViewModel.totalPrice.collectAsState()

    val categories = listOf("Semua", "Makanan", "Mie", "Minuman", "Cemilan")
    var selectedCategory by remember { mutableStateOf("Semua") }
    var isDropdownExpanded by remember { mutableStateOf(false) }

    val canteens = remember(menuList) {
        listOf("Semua Kantin") + menuList.map { it.canteenName }.distinct().filter { it.isNotEmpty() }
    }
    var selectedCanteen by remember { mutableStateOf("Semua Kantin") }
    var isCanteenDropdownExpanded by remember { mutableStateOf(false) }

    val context = androidx.compose.ui.platform.LocalContext.current
    LaunchedEffect(Unit) {
        notificationViewModel.startGlobalListener(context)
    }

    LaunchedEffect(menuList) {
        if (selectedCanteen != "Semua Kantin" && !menuList.any { it.canteenName == selectedCanteen }) {
            selectedCanteen = "Semua Kantin"
        }
    }

    var showBottomSheet by remember { mutableStateOf(false) }
    var showKantinWarningDialog by remember { mutableStateOf(false) }
    var selectedMenuForNote by remember { mutableStateOf<Menu?>(null) }
    var noteText by remember { mutableStateOf("") }
    var quantity by remember { androidx.compose.runtime.mutableIntStateOf(1) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)


    val notificationCount by notificationViewModel.unreadCount.collectAsState()

    if (showKantinWarningDialog) {
        androidx.compose.material3.AlertDialog(
            onDismissRequest = { showKantinWarningDialog = false },
            title = { Text("Beda Kantin", fontWeight = FontWeight.Bold, color = TextBlack) },
            text = { Text("Kamu hanya bisa memesan dari 1 kantin dalam 1 pesanan. Selesaikan pesanan sebelumnya atau kosongkan keranjang terlebih dahulu.", color = TextGray) },
            containerColor = Color.White,
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = { showKantinWarningDialog = false }
                ) {
                    Text("Mengerti", color = PrimaryOrange, fontWeight = FontWeight.Bold)
                }
            }
        )
    }

    if (showBottomSheet && selectedMenuForNote != null) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
                noteText = ""
                quantity = 1
            },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            selectedMenuForNote?.let { menu ->
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp)
                        .imePadding()
                        .verticalScroll(rememberScrollState())
                ) {
                    Text(
                        text = menu.name,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextBlack
                    )
                    Text(
                        text = formatRupiah(menu.price),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = PrimaryOrange,
                        modifier = Modifier.padding(top = 4.dp, bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "Jumlah", fontSize = 16.sp, fontWeight = FontWeight.SemiBold, color = TextBlack)
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.background(Color(0xFFFDECE2), RoundedCornerShape(20.dp))
                        ) {
                            IconButton(
                                onClick = { if (quantity > 1) quantity-- },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Text(text = "-", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PrimaryOrange)
                            }
                            Text(
                                text = quantity.toString(),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextBlack,
                                modifier = Modifier.padding(horizontal = 12.dp)
                            )
                            IconButton(
                                onClick = { quantity++ },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Text(text = "+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PrimaryOrange)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomTextField(
                        value = noteText,
                        onValueChange = { if (it.length <= 100) noteText = it },
                        label = "Catatan Opsional (Maks. 100 Karakter)",
                        placeholder = "Contoh: Jangan pedas, karetnya 2 ya..."
                    )
                    Text(
                        text = "${noteText.length}/100",
                        fontSize = 12.sp,
                        color = TextGray,
                        modifier = Modifier
                            .align(Alignment.End)
                            .padding(top = 4.dp, end = 4.dp)
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    val totalItemPrice = menu.price * quantity
                    PrimaryButton(
                        text = "Tambah - ${formatRupiah(totalItemPrice)}",
                        onClick = {
                            val currentCart = cartItems
                            if (currentCart.isNotEmpty()) {
                                val firstItemId = currentCart.first().menuId
                                val firstItemMenu = menuList.find { it.id == firstItemId }
                                if (firstItemMenu != null && firstItemMenu.sellerId != menu.sellerId) {
                                    showKantinWarningDialog = true
                                    return@PrimaryButton
                                }
                            }
                            
                            cartViewModel.addToCart(menu, quantity, noteText)
                            showBottomSheet = false
                            noteText = ""
                            quantity = 1
                        }
                    )
                    Spacer(modifier = Modifier.height(24.dp))
                }
            }
        }
    }

    Scaffold(
        floatingActionButtonPosition = androidx.compose.material3.FabPosition.Center,
        floatingActionButton = {
            if (cartItems.isNotEmpty() && selectedLocation != "Belum Dipilih") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth(0.9f)
                        .clip(RoundedCornerShape(32.dp))
                        .background(PrimaryOrange)
                        .clickable { onTabNavigate("cart") }
                        .padding(horizontal = 24.dp, vertical = 10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(text = "${cartItems.sumOf { it.quantity }} item", color = Color.White, fontSize = 12.sp)
                            Text(text = "Lanjutkan Pembayaran", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        }
                        Text(text = formatRupiah(cartTotalPrice), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                    }
                }
            }
        },
        bottomBar = {
            BottomNavBar(
                currentRoute = "home",
                onNavigate = onTabNavigate,
                isSeller = false,
                cartItemCount = cartItems.size
            )
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
                            .clickable { onNavigate("notification") },
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
                            Icon(
                                imageVector = Icons.Default.Notifications,
                                contentDescription = null,
                                tint = PrimaryOrange
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = buildAnnotatedString {
                        withStyle(
                            style = SpanStyle(color = TextGray, fontWeight = FontWeight.Normal)
                        ) {
                            append("Selamat datang, ")
                        }
                        withStyle(
                            style = SpanStyle(color = TextBlack, fontWeight = FontWeight.Bold)
                        ) {
                            append(userName.ifEmpty { "Memuat..." })
                        }
                    },
                    fontSize = 16.sp,
                    lineHeight = 24.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(text = "Pilih Lokasi", fontSize = 12.sp, color = TextGray)

                        Box {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { isDropdownExpanded = true }
                                    .padding(vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.LocationOn, contentDescription = null, tint = PrimaryOrange)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = selectedLocation,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = TextBlack
                                    )
                                }
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = TextBlack)
                            }

                            DropdownMenu(
                                expanded = isDropdownExpanded,
                                onDismissRequest = { isDropdownExpanded = false },
                                modifier = Modifier
                                    .background(Color.White)
                                    .heightIn(max = 220.dp)
                            ) {
                                locations.filter { it != "Belum Dipilih" || selectedLocation == "Belum Dipilih" }.forEach { location ->
                                    DropdownMenuItem(
                                        text = { Text(text = location, color = TextBlack) },
                                        onClick = {
                                            if (location != selectedLocation) {
                                                if (selectedLocation != "Belum Dipilih") {
                                                    cartViewModel.clearCart()
                                                }
                                                viewModel.updateSelectedLocation(location)
                                                selectedCanteen = "Semua Kantin"
                                            }
                                            isDropdownExpanded = false
                                        },
                                        modifier = Modifier.background(Color.White)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (selectedLocation != "Belum Dipilih") {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(IntrinsicSize.Max),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = weather.emoji,
                                        fontSize = 20.sp
                                    )
                                    Text(text = weather.temperature, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Column {
                                    Text(text = weather.city, fontSize = 11.sp, fontWeight = FontWeight.Bold, color = PrimaryOrange)
                                    Text(
                                        text = weather.description,
                                        fontSize = 11.sp,
                                        color = TextGray,
                                        lineHeight = 14.sp,
                                        maxLines = 2,
                                        overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                                    )
                                }
                            }
                        }

                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            colors = CardDefaults.cardColors(
                                containerColor = crowdStatusBgColor
                            ),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 16.dp, vertical = 12.dp),
                                verticalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(text = "👥", fontSize = 20.sp)
                                    Box(
                                        modifier = Modifier
                                            .size(10.dp)
                                            .background(crowdStatusIndicatorColor, CircleShape)
                                    )
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = crowdStatusText,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = crowdStatusIndicatorColor,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Lagi Pengen Apa?",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextBlack
                        )

                        Box {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color.White)
                                    .clickable { isCanteenDropdownExpanded = true }
                                    .padding(horizontal = 8.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = if (selectedCanteen == "Semua Kantin") "Semua Kantin" else selectedCanteen,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = PrimaryOrange,
                                    maxLines = 1,
                                    modifier = Modifier
                                        .weight(1f, fill = false)
                                        .basicMarquee()
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = PrimaryOrange)
                            }

                            DropdownMenu(
                                expanded = isCanteenDropdownExpanded,
                                onDismissRequest = { isCanteenDropdownExpanded = false },
                                modifier = Modifier.background(Color.White).heightIn(max = 220.dp)
                            ) {
                                canteens.forEach { canteen ->
                                    DropdownMenuItem(
                                        text = { Text(text = canteen, color = TextBlack, fontSize = 14.sp) },
                                        onClick = {
                                            selectedCanteen = canteen
                                            isCanteenDropdownExpanded = false
                                        },
                                        modifier = Modifier.background(Color.White)
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        categories.forEach { category ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (selectedCategory == category) PrimaryOrange else Color.White)
                                    .clickable { selectedCategory = category }
                                    .padding(horizontal = 20.dp, vertical = 10.dp)
                            ) {
                                Text(
                                    text = category,
                                    color = if (selectedCategory == category) Color.White else TextBlack,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                }
            }

            if (selectedLocation == "Belum Dipilih") {
                item {
                    com.example.antriin.presentation.components.EmptyState(
                        title = "Pilih Lokasi Dulu",
                        message = "Silakan pilih lokasi fakultas Anda di atas untuk melihat menu.",
                        modifier = Modifier.padding(top = 48.dp)
                    )
                }
            } else {
                val filteredMenu = menuList.filter { menu ->
                    val matchCategory = selectedCategory == "Semua" || menu.category == selectedCategory
                    val matchCanteen = selectedCanteen == "Semua Kantin" || menu.canteenName == selectedCanteen
                    matchCategory && matchCanteen
                }

                if (filteredMenu.isEmpty()) {
                    item {
                        com.example.antriin.presentation.components.EmptyState(
                            title = "Menu Tidak Ditemukan",
                            message = "Maaf, belum ada menu yang tersedia untuk kategori atau lokasi ini.",
                            modifier = Modifier.padding(top = 48.dp)
                        )
                    }
                } else {
                    items(filteredMenu) { menu ->
                        val cartItem = cartItems.find { it.menuId == menu.id }
                        val cartQuantity = cartItem?.quantity ?: 0

                        
                        MenuCard(
                            menu = menu,
                            cartQuantity = cartQuantity,
                            onAddClick = {
                                selectedMenuForNote = menu
                                quantity = 1
                                noteText = ""
                                showBottomSheet = true
                            },
                            onQuantityChange = { newQty ->
                                cartViewModel.updateQuantity(menu.id, newQty)
                            }
                        )
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(if (cartItems.isNotEmpty()) 100.dp else 24.dp))
            }
        }
    }
}