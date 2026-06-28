package com.example.antriin.presentation.ui.student

import com.example.antriin.presentation.viewmodel.student.CartViewModel
import com.example.antriin.presentation.viewmodel.student.StudentNotificationViewModel
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.antriin.presentation.components.BottomNavBar
import com.example.antriin.presentation.components.CartItemCard
import com.example.antriin.presentation.components.EmptyState
import com.example.antriin.presentation.theme.BackgroundLight
import com.example.antriin.presentation.theme.PrimaryOrange
import com.example.antriin.presentation.theme.TextBlack
import com.example.antriin.presentation.theme.TextGray
import com.example.antriin.utils.formatRupiah

@Composable
fun CartScreen(
    onNavigate: (String) -> Unit,
    onTabNavigate: (String) -> Unit,
    viewModel: CartViewModel = viewModel(factory = com.example.antriin.di.ViewModelFactory.Factory),
    notificationViewModel: StudentNotificationViewModel = viewModel(factory = com.example.antriin.di.ViewModelFactory.Factory)
) {
    val cartItems by viewModel.cartItems.collectAsState()
    val totalPrice by viewModel.totalPrice.collectAsState()
    var paymentMethod by remember { mutableStateOf("Tunai") }
    val context = LocalContext.current
    @Suppress("DEPRECATION")
    val clipboardManager = LocalClipboardManager.current
    val checkoutSuccess by viewModel.checkoutSuccess.collectAsState()

    LaunchedEffect(Unit) {
        notificationViewModel.startGlobalListener(context)
    }

    LaunchedEffect(checkoutSuccess) {
        if (checkoutSuccess) {
            viewModel.resetCheckoutStatus()
            onTabNavigate("tracking")
            Toast.makeText(context, "Pesanan berhasil dibuat!", Toast.LENGTH_SHORT).show()
        }
    }

    val sellerPaymentInfo by viewModel.sellerPaymentInfo.collectAsState()


    LaunchedEffect(sellerPaymentInfo) {
        if (sellerPaymentInfo.isEmpty()) {
            paymentMethod = "Tunai"
        }
    }
    val notificationCount by notificationViewModel.unreadCount.collectAsState()

    Scaffold(
        bottomBar = {
            Column {
                if (cartItems.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White)
                            .padding(horizontal = 24.dp, vertical = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(text = "Total Pembayaran • $paymentMethod", fontSize = 12.sp, color = TextGray)
                            Text(text = formatRupiah(totalPrice), fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PrimaryOrange)
                        }
                        Button(
                            onClick = { viewModel.checkout(paymentMethod) },
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Pesan", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                Spacer(modifier = Modifier.width(8.dp))
                                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }
                BottomNavBar(
                    currentRoute = "cart",
                    onNavigate = onTabNavigate,
                    isSeller = false,
                    cartItemCount = cartItems.size
                )
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
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = PrimaryOrange)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Keranjang Saya", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                Text(text = "Periksa kembali pesanan Anda sebelum membayar.", fontSize = 14.sp, color = TextGray)
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (cartItems.isEmpty()) {
                item {
                    EmptyState(
                        title = "Keranjang Kosong",
                        message = "Anda belum memilih menu apa pun. Ayo pesan sekarang!",
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }
            } else {
                items(cartItems) { item ->
                        CartItemCard(
                            cartItem = item,
                            icon = viewModel.getMenuIcon(item.menuId),
                            onIncreaseClick = { viewModel.updateQuantity(item.menuId, item.quantity + 1) },
                            onDecreaseClick = { viewModel.updateQuantity(item.menuId, item.quantity - 1) }
                        )
                }

                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Info, contentDescription = null, tint = PrimaryOrange)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(text = "Pembayaran", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                                }
                                Row(
                                    modifier = Modifier.background(Color(0xFFEEEEEE), RoundedCornerShape(20.dp))
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(20.dp))
                                            .background(if (paymentMethod == "Tunai") PrimaryOrange else Color.Transparent)
                                            .clickable { paymentMethod = "Tunai" }
                                            .padding(horizontal = 16.dp, vertical = 8.dp)
                                    ) {
                                        Text(text = "Tunai", color = if (paymentMethod == "Tunai") Color.White else TextGray, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                    }
                                    if (sellerPaymentInfo.isNotEmpty()) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(20.dp))
                                                .background(if (paymentMethod == "Transfer") PrimaryOrange else Color.Transparent)
                                                .clickable { paymentMethod = "Transfer" }
                                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                        ) {
                                            Text(text = "Transfer", color = if (paymentMethod == "Transfer") Color.White else TextGray, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Info, contentDescription = null, tint = TextGray, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = if (paymentMethod == "Tunai") "Segera bayar tunai di kasir agar pesanan diproses." else "Lakukan transfer sesuai nominal dan nomor penjual agar pesananmu divalidasi.",
                                    fontSize = 12.sp,
                                    color = TextGray
                                )
                            }

                            if (paymentMethod == "Transfer" && sellerPaymentInfo.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(16.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .background(Color(0xFFFAFAFA), RoundedCornerShape(8.dp))
                                        .padding(16.dp)
                                ) {
                                    val parts = sellerPaymentInfo.split(" - ")
                                    val methods = if (parts.isNotEmpty()) parts[0].replace("Dana", "DANA") else ""
                                    val number = if (parts.size > 1) parts[1] else sellerPaymentInfo

                                    Column(modifier = Modifier.fillMaxWidth()) {
                                        Text(text = "Metode yang Didukung:", color = TextGray, fontSize = 12.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(text = methods, color = TextBlack, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                                        
                                        Spacer(modifier = Modifier.height(16.dp))
                                        
                                        Text(text = "Nomor Transfer:", color = TextGray, fontSize = 12.sp)
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Row(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(Color.White, RoundedCornerShape(8.dp))
                                                .border(1.dp, Color(0xFFEEEEEE), RoundedCornerShape(8.dp))
                                                .padding(12.dp),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.CenterVertically
                                        ) {
                                            Text(text = number, color = PrimaryOrange, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                                            Icon(
                                                imageVector = Icons.Default.ContentCopy,
                                                contentDescription = "Salin Nomor",
                                                tint = PrimaryOrange,
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .clickable {
                                                        clipboardManager.setText(AnnotatedString(number))
                                                        Toast.makeText(context, "Nomor disalin!", Toast.LENGTH_SHORT).show()
                                                    }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}