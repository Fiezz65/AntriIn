package com.example.antriin.presentation.ui.student

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
import androidx.compose.material3.Divider
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
import com.example.antriin.presentation.components.QueueStatusCard
import com.example.antriin.presentation.theme.BackgroundLight
import com.example.antriin.presentation.theme.PrimaryOrange
import com.example.antriin.presentation.theme.TextBlack
import com.example.antriin.presentation.theme.TextGray

@Composable
fun LiveTrackingScreen(
    onNavigate: (String) -> Unit,
    onTabNavigate: (String) -> Unit,
    viewModel: LiveTrackingViewModel = viewModel(),
    cartViewModel: CartViewModel = viewModel(factory = com.example.antriin.di.ViewModelFactory.Factory)
) {
    val queueList by viewModel.queueList.collectAsState()
    val cartItems by cartViewModel.cartItems.collectAsState()
    val dummyNotificationCount = 3

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = "tracking",
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
                                if (dummyNotificationCount > 0) {
                                    Badge(containerColor = Color.Red, contentColor = Color.White) {
                                        Text(text = dummyNotificationCount.toString())
                                    }
                                }
                            }
                        ) {
                            Icon(Icons.Default.Notifications, contentDescription = null, tint = PrimaryOrange)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Text(text = "Status Pesanan", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                Text(text = "Pantau pesananmu secara real-time", fontSize = 14.sp, color = TextGray)
                Spacer(modifier = Modifier.height(16.dp))

                if (queueList.isEmpty()) {
                    com.example.antriin.presentation.components.EmptyState(
                        title = "Belum Ada Antrean",
                        message = "Belum ada pesanan yang sedang diproses. Silakan pesan menu terlebih dahulu."
                    )
                } else {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF2ED)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "#ANT-092",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextBlack,
                                modifier = Modifier.background(Color(0xFFE0E0E0), RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                            Column(horizontalAlignment = Alignment.End) {
                                Text(text = "~5 mnt", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = PrimaryOrange)
                                Text(text = "Estimasi", fontSize = 12.sp, color = TextGray)
                            }
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Nasi Goreng Spesial", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                        Text(text = "Kantin Teknik - Kedai 4", fontSize = 14.sp, color = TextGray)

                        Spacer(modifier = Modifier.height(24.dp))
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                            Text(text = "Diterima", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                            Text(text = "Diproses", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PrimaryOrange)
                            Text(text = "Siap Diambil", fontSize = 12.sp, color = TextGray)
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Divider(color = PrimaryOrange, thickness = 4.dp)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

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
                            Text(text = "Antrean Kedai 4", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                            Text(
                                text = "Total: ${queueList.size}",
                                fontSize = 12.sp,
                                color =  TextBlack,
                                modifier = Modifier.background(Color(0xFFFDECE2), RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        queueList.forEach { order ->
                            QueueStatusCard(
                                order = order,
                                isCurrentUser = order.buyerName == "Saya"
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
            }
        }
    }
}

