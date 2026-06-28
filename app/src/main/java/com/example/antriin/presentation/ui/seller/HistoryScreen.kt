package com.example.antriin.presentation.ui.seller

import com.example.antriin.presentation.viewmodel.seller.HistoryViewModel
import com.example.antriin.presentation.viewmodel.seller.SellerNotificationViewModel
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.antriin.presentation.components.BottomNavBar
import com.example.antriin.presentation.theme.BackgroundLight
import com.example.antriin.presentation.theme.PrimaryOrange
import com.example.antriin.presentation.theme.TextBlack
import com.example.antriin.presentation.theme.TextGray
import com.example.antriin.utils.formatRupiah
import com.example.antriin.utils.formatDate

@Composable
fun HistoryScreen(
    onNavigate: (String) -> Unit,
    onTabNavigate: (String) -> Unit,
    viewModel: HistoryViewModel = viewModel(factory = com.example.antriin.di.ViewModelFactory.Factory),
    notificationViewModel: SellerNotificationViewModel = viewModel(factory = com.example.antriin.di.ViewModelFactory.Factory)
) {
    val historyList by viewModel.completedOrders.collectAsState()

    val notificationCount by notificationViewModel.unreadCount.collectAsState()

    val context = androidx.compose.ui.platform.LocalContext.current
    androidx.compose.runtime.LaunchedEffect(Unit) {
        notificationViewModel.startGlobalListener(context)
        viewModel.refresh()
    }

    Scaffold(
        bottomBar = { BottomNavBar(currentRoute = "history", onNavigate = onTabNavigate, isSeller = true) }
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
                Text(text = "Riwayat Hari Ini", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                Text(text = "Daftar pesanan yang telah diselesaikan hari ini.", fontSize = 14.sp, color = TextGray)
                Spacer(modifier = Modifier.height(16.dp))
            }

            if (historyList.isEmpty()) {
                item {
                    com.example.antriin.presentation.components.EmptyState(
                        title = "Belum Ada Riwayat",
                        message = "Belum ada pesanan yang selesai pada periode ini.",
                        modifier = Modifier.padding(top = 24.dp)
                    )
                }
            } else {
                items(historyList) { order ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                                Text(
                                    text = order.buyerName, 
                                    fontSize = 16.sp, 
                                    fontWeight = FontWeight.Bold, 
                                    color = TextBlack,
                                    modifier = Modifier.weight(1f).padding(end = 8.dp)
                                )
                                Text(
                                    text = "Selesai",
                                    fontSize = 12.sp,
                                    color = Color(0xFF4CAF50),
                                    modifier = Modifier
                                        .background(Color(0xFFE8F5E9), RoundedCornerShape(4.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                            val formattedDate = formatDate(order.timestamp)
                            Text(text = formattedDate, fontSize = 12.sp, color = TextGray)

                            Spacer(modifier = Modifier.height(12.dp))

                            order.items.forEach { item ->
                                Column(modifier = Modifier.fillMaxWidth()) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(text = "${item.quantity}x ${item.menuName}", fontSize = 14.sp, color = TextBlack)
                                        Text(text = formatRupiah(item.price * item.quantity), fontSize = 14.sp, color = TextBlack)
                                    }

                                    Spacer(modifier = Modifier.height(4.dp))
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))
                            HorizontalDivider(color = Color.LightGray)
                            Spacer(modifier = Modifier.height(12.dp))

                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "Total Pendapatan", fontSize = 14.sp, color = TextGray)
                                Text(text = formatRupiah(order.totalPrice), fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryOrange)
                            }
                        }
                    }
                }
                item { Spacer(modifier = Modifier.height(24.dp)) }
            }
        }
    }
}