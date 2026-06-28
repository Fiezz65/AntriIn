package com.example.antriin.presentation.ui.seller

import com.example.antriin.presentation.viewmodel.seller.SellerNotificationViewModel
import com.example.antriin.presentation.viewmodel.seller.SellerProfileViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.foundation.border
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SellerProfileScreen(
    onNavigate: (String) -> Unit,
    onTabNavigate: (String) -> Unit,
    viewModel: SellerProfileViewModel = viewModel(factory = com.example.antriin.di.ViewModelFactory.Factory),
    notificationViewModel: SellerNotificationViewModel = viewModel(factory = com.example.antriin.di.ViewModelFactory.Factory)
) {
    val user by viewModel.sellerProfile.collectAsState()

    val notificationCount by notificationViewModel.unreadCount.collectAsState()
    
    val context = androidx.compose.ui.platform.LocalContext.current
    androidx.compose.runtime.LaunchedEffect(Unit) {
        notificationViewModel.startGlobalListener(context)
    }

    var showEditCanteenSheet by remember { mutableStateOf(false) }
    var showEditPaymentSheet by remember { mutableStateOf(false) }
    var editCanteenName by remember { mutableStateOf("") }
    
    var editPaymentNumber by remember { mutableStateOf("") }
    var isDanaChecked by remember { mutableStateOf(false) }
    var isGopayChecked by remember { mutableStateOf(false) }
    var isShopeepayChecked by remember { mutableStateOf(false) }
    var isOvoChecked by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    fun parsePaymentInfo(info: String) {
        isDanaChecked = info.contains("DANA", ignoreCase = true)
        isGopayChecked = info.contains("GoPay", ignoreCase = true)
        isShopeepayChecked = info.contains("ShopeePay", ignoreCase = true)
        isOvoChecked = info.contains("OVO", ignoreCase = true)
        
        val parts = info.split(" - ")
        editPaymentNumber = if (parts.size == 2) {
            parts[1]
        } else {
            info.replace(Regex("[^0-9]"), "")
        }
    }

    if (showEditCanteenSheet) {
        ModalBottomSheet(
            onDismissRequest = { showEditCanteenSheet = false },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(text = "Edit Nama Kantin", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    value = editCanteenName,
                    onValueChange = { if (it.length <= 25) editCanteenName = it },
                    label = "Nama Kantin",
                    placeholder = "Contoh: Kantin Teknik",
                    supportingText = {
                        Text(
                            text = "${editCanteenName.length}/25",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.End,
                            color = TextGray
                        )
                    }
                )

                PrimaryButton(
                    text = "SIMPAN PERUBAHAN",
                    onClick = {
                        viewModel.updateProfile(editCanteenName, user.isOpen, user.paymentInfo)
                        showEditCanteenSheet = false
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    if (showEditPaymentSheet) {
        ModalBottomSheet(
            onDismissRequest = { showEditPaymentSheet = false },
            sheetState = sheetState,
            containerColor = Color.White
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
            ) {
                Text(text = "Metode Pembayaran", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                Spacer(modifier = Modifier.height(16.dp))

                Text(text = "E-Wallet yang Diterima", fontSize = 14.sp, fontWeight = FontWeight.SemiBold, color = TextBlack)
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    EWalletOption(name = "DANA", isSelected = isDanaChecked, onClick = { isDanaChecked = !isDanaChecked }, modifier = Modifier.weight(1f))
                    EWalletOption(name = "GoPay", isSelected = isGopayChecked, onClick = { isGopayChecked = !isGopayChecked }, modifier = Modifier.weight(1f))
                }
                Spacer(modifier = Modifier.height(12.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    EWalletOption(name = "ShopeePay", isSelected = isShopeepayChecked, onClick = { isShopeepayChecked = !isShopeepayChecked }, modifier = Modifier.weight(1f))
                    EWalletOption(name = "OVO", isSelected = isOvoChecked, onClick = { isOvoChecked = !isOvoChecked }, modifier = Modifier.weight(1f))
                }

                Spacer(modifier = Modifier.height(16.dp))

                CustomTextField(
                    value = editPaymentNumber,
                    onValueChange = { editPaymentNumber = it.replace(Regex("[^0-9]"), "") },
                    label = "Nomor HP",
                    placeholder = "Contoh: 081234567890",
                    supportingText = {
                        Text(
                            text = "Kosongkan nomor ini jika hanya menerima tunai.",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = androidx.compose.ui.text.style.TextAlign.Start,
                            color = TextGray
                        )
                    }
                )

                Spacer(modifier = Modifier.height(32.dp))

                PrimaryButton(
                    text = "SIMPAN PEMBAYARAN",
                    onClick = {
                        val selectedMethods = mutableListOf<String>()
                        if (isDanaChecked) selectedMethods.add("DANA")
                        if (isGopayChecked) selectedMethods.add("GoPay")
                        if (isShopeepayChecked) selectedMethods.add("ShopeePay")
                        if (isOvoChecked) selectedMethods.add("OVO")

                        viewModel.updatePaymentInfo(user.canteenName, user.isOpen, selectedMethods, editPaymentNumber)
                        showEditPaymentSheet = false
                    }
                )
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = "seller_profile",
                onNavigate = onTabNavigate,
                isSeller = true
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
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

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color(0xFFFDECE2), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Home, contentDescription = null, modifier = Modifier.size(50.dp), tint = PrimaryOrange)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = user.canteenName, 
                            fontSize = 20.sp, 
                            fontWeight = FontWeight.Bold, 
                            color = TextBlack,
                            modifier = Modifier.weight(1f, fill = false),
                            maxLines = 1,
                            overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.Edit, 
                            contentDescription = "Edit Nama Kantin",
                            tint = TextGray,
                            modifier = Modifier
                                .size(24.dp)
                                .clip(CircleShape)
                                .clickable { 
                                    editCanteenName = user.canteenName
                                    showEditCanteenSheet = true 
                                }
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = user.location, fontSize = 14.sp, color = TextGray)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Button(
                        onClick = { viewModel.updateProfile(user.canteenName, !user.isOpen, user.paymentInfo) },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (user.isOpen) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                            contentColor = if (user.isOpen) Color(0xFF4CAF50) else Color.Red
                        ),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.height(40.dp)
                    ) {
                        Text(
                            text = if (user.isOpen) "Buka" else "Tutup",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = { 
                            parsePaymentInfo(user.paymentInfo)
                            showEditPaymentSheet = true 
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth().height(48.dp)
                    ) {
                        Icon(Icons.Default.QrCode, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = "Atur Pembayaran", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = { 
                    viewModel.logout() 
                    onNavigate("login")
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color.Red)
                    Text(text = "Keluar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Red, modifier = Modifier.padding(start = 16.dp))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun EWalletOption(
    name: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .clip(RoundedCornerShape(12.dp))
            .background(if (isSelected) PrimaryOrange.copy(alpha = 0.1f) else Color(0xFFF5F5F5))
            .clickable { onClick() }
            .border(
                width = 1.dp,
                color = if (isSelected) PrimaryOrange else Color.Transparent,
                shape = RoundedCornerShape(12.dp)
            )
            .padding(vertical = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = name,
            color = if (isSelected) PrimaryOrange else TextGray,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}