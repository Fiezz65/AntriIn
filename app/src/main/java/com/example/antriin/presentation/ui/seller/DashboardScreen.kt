package com.example.antriin.presentation.ui.seller

import com.example.antriin.presentation.viewmodel.auth.AuthViewModel
import com.example.antriin.presentation.viewmodel.seller.DashboardViewModel
import com.example.antriin.presentation.viewmodel.seller.HistoryViewModel
import com.example.antriin.presentation.viewmodel.seller.MenuViewModel
import com.example.antriin.presentation.viewmodel.seller.SellerNotificationViewModel
import com.example.antriin.presentation.viewmodel.seller.SellerProfileViewModel
import com.example.antriin.presentation.viewmodel.student.CartViewModel
import com.example.antriin.presentation.viewmodel.student.HomeViewModel
import com.example.antriin.presentation.viewmodel.student.LiveTrackingViewModel
import com.example.antriin.presentation.viewmodel.student.StudentProfileViewModel

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
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
import com.example.antriin.presentation.components.OrderCard
import com.example.antriin.presentation.theme.BackgroundLight
import com.example.antriin.presentation.theme.PrimaryOrange
import com.example.antriin.presentation.theme.TextBlack
import com.example.antriin.presentation.theme.TextGray
import com.example.antriin.utils.formatRupiah

import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext

@Composable
fun DashboardScreen(
    onNavigate: (String) -> Unit,
    onTabNavigate: (String) -> Unit,
    viewModel: DashboardViewModel = viewModel(factory = com.example.antriin.di.ViewModelFactory.Factory),
    notificationViewModel: SellerNotificationViewModel = viewModel(factory = com.example.antriin.di.ViewModelFactory.Factory)
) {
    val incomingOrders by viewModel.incomingOrders.collectAsState()
    val portionsSold by viewModel.portionsSold.collectAsState()
    val totalRevenue by viewModel.totalRevenue.collectAsState()
    val notifications by notificationViewModel.notifications.collectAsState()
    val notificationCount by notificationViewModel.unreadCount.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(Unit) {
        notificationViewModel.startGlobalListener(context)
    }

    val orderFilters = listOf("Semua", "Menunggu", "Diproses", "Siap")
    var selectedFilter by remember { mutableStateOf("Semua") }

    val filteredOrders = incomingOrders.filter { order ->
        when (selectedFilter) {
            "Menunggu" -> order.status == "Menunggu Validasi" || order.status == "Belum Bayar"
            "Diproses" -> order.status == "Diproses"
            "Siap" -> order.status == "Siap Diambil"
            else -> true
        }
    }

    val indicatorText = when (selectedFilter) {
        "Semua" -> "${filteredOrders.size} Pesanan"
        "Menunggu" -> "${filteredOrders.size} Menunggu"
        "Diproses" -> "${filteredOrders.size} Diproses"
        "Siap" -> "${filteredOrders.size} Siap"
        else -> ""
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = "dashboard",
                onNavigate = onTabNavigate,
                isSeller = true
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
                Text(text = "Ringkasan Hari Ini", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                Text(text = "Pantau pesanan dan performa kantin Anda.", fontSize = 14.sp, color = TextGray)
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f).height(100.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "PORSI TERJUAL", fontSize = 10.sp, color = TextGray, fontWeight = FontWeight.Bold)
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(text = portionsSold.toString(), fontSize = 24.sp, fontWeight = FontWeight.Bold, color = PrimaryOrange)
                                Text(text = " porsi", fontSize = 12.sp, color = TextGray, modifier = Modifier.padding(bottom = 4.dp, start = 4.dp))
                            }
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f).height(100.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp).fillMaxSize(), verticalArrangement = Arrangement.SpaceBetween) {
                            Text(text = "TOTAL PENDAPATAN", fontSize = 10.sp, color = TextGray, fontWeight = FontWeight.Bold)
                            Text(text = formatRupiah(totalRevenue), fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Pesanan Masuk", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                    Text(
                        text = indicatorText,
                        fontSize = 12.sp,
                        color = PrimaryOrange,
                        modifier = Modifier
                            .background(Color(0xFFFDECE2), RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    orderFilters.forEach { filterName ->
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(20.dp))
                                .background(if (selectedFilter == filterName) PrimaryOrange else Color.White)
                                .clickable { selectedFilter = filterName }
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                        ) {
                            Text(
                                text = filterName,
                                color = if (selectedFilter == filterName) Color.White else TextGray,
                                fontSize = 12.sp,
                                fontWeight = if (selectedFilter == filterName) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            if (filteredOrders.isEmpty()) {
                item {
                    com.example.antriin.presentation.components.EmptyState(
                        title = "Belum Ada Pesanan",
                        message = "Belum ada pesanan masuk untuk status ini.",
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }
            } else {
                items(filteredOrders) { order ->
                    OrderCard(
                        order = order,
                        onNextStepClick = { },
                        onCancelClick = { }
                    )
                }
            }

            item { Spacer(modifier = Modifier.height(24.dp)) }
        }
    }
}

