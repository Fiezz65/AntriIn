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
import com.example.antriin.presentation.viewmodel.student.StudentNotificationViewModel
import com.example.antriin.presentation.viewmodel.student.StudentProfileViewModel

import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
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
import androidx.compose.foundation.layout.widthIn
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.antriin.presentation.components.BottomNavBar
import com.example.antriin.presentation.components.QueueStatusCard
import com.example.antriin.presentation.theme.BackgroundLight
import com.example.antriin.presentation.theme.PrimaryOrange
import com.example.antriin.presentation.theme.TextBlack
import com.example.antriin.presentation.theme.TextGray

@OptIn(androidx.compose.foundation.ExperimentalFoundationApi::class)
@Composable
fun LiveTrackingScreen(
    onNavigate: (String) -> Unit,
    onTabNavigate: (String) -> Unit,
    viewModel: LiveTrackingViewModel = viewModel(factory = com.example.antriin.di.ViewModelFactory.Factory),
    notificationViewModel: StudentNotificationViewModel = viewModel(factory = com.example.antriin.di.ViewModelFactory.Factory),
    cartViewModel: CartViewModel = viewModel(factory = com.example.antriin.di.ViewModelFactory.Factory)
) {
    val currentOrder by viewModel.currentOrder.collectAsState()
    val queueList by viewModel.queueList.collectAsState()
    val isBusy by viewModel.isBusy.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val sellerName by viewModel.sellerName.collectAsState()
    val sellers by viewModel.sellers.collectAsState()
    val selectedSellerId by viewModel.selectedSellerId.collectAsState()
    val cartItems by cartViewModel.cartItems.collectAsState()
    val notifications by notificationViewModel.notifications.collectAsState()
    val notificationCount by notificationViewModel.unreadCount.collectAsState()

    val context = androidx.compose.ui.platform.LocalContext.current
    androidx.compose.runtime.LaunchedEffect(Unit) {
        viewModel.refresh()
        notificationViewModel.startGlobalListener(context)
    }

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
                val titleText = if (currentOrder == null) "Pantau Antrean Kantin" else "Status Pesanan Anda"
                val subtitleAction = if (currentOrder == null) "Pilih kantin di bawah untuk melihat antrean." else "Pantau pesananmu secara real-time."

                Text(text = titleText, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextBlack)
                Text(
                    text = subtitleAction,
                    fontSize = 14.sp,
                    color = TextGray
                )
                Spacer(modifier = Modifier.height(16.dp))

                if (currentOrder == null) {
                    if (sellers.isNotEmpty()) {
                        androidx.compose.foundation.lazy.LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(sellers) { seller ->
                                val isSelected = seller.uid == selectedSellerId
                                Box(
                                    modifier = Modifier
                                        .width(140.dp)
                                        .background(
                                            color = if (isSelected) PrimaryOrange else Color.White,
                                            shape = RoundedCornerShape(16.dp)
                                        )
                                        .clickable { viewModel.selectSeller(seller.uid) }
                                        .padding(horizontal = 8.dp, vertical = 8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = seller.fullName,
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = if (isSelected) Color.White else TextGray,
                                        maxLines = 1,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.basicMarquee(iterations = Int.MAX_VALUE)
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                } else {
                    val order = currentOrder
                    if (order != null) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFAF2ED)),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Pesanan Anda",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White,
                                        modifier = Modifier.background(PrimaryOrange, RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(12.dp))
                                Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    val displayItems = order.items.take(3)
                                    displayItems.forEach { item ->
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Row(verticalAlignment = Alignment.Top, modifier = Modifier.weight(1f)) {
                                                Text(
                                                    text = "x${item.quantity}",
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = PrimaryOrange,
                                                    modifier = Modifier.width(28.dp)
                                                )
                                                Text(
                                                    text = item.menuName,
                                                    fontSize = 14.sp,
                                                    fontWeight = FontWeight.SemiBold,
                                                    color = TextBlack,
                                                    maxLines = 1,
                                                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis,
                                                    modifier = Modifier.padding(end = 8.dp)
                                                )
                                            }
                                            Text(
                                                text = com.example.antriin.utils.formatRupiah(item.price),
                                                fontSize = 14.sp,
                                                fontWeight = FontWeight.SemiBold,
                                                color = TextBlack
                                            )
                                        }
                                    }
                                    if (order.items.size > 3) {
                                        Text(
                                            text = "+ ${order.items.size - 3} menu lainnya",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = TextGray,
                                            modifier = Modifier.padding(start = 28.dp, top = 2.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "${com.example.antriin.utils.formatRupiah(order.totalPrice)} - ${order.paymentMethod}", fontSize = 14.sp, color = TextGray)
    
                                Spacer(modifier = Modifier.height(24.dp))
                                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
                                    Text(text = "Menunggu", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (order.status == com.example.antriin.domain.model.OrderStatus.WAITING_VALIDATION || order.status == "Menunggu Validasi") PrimaryOrange else TextBlack)
                                    Text(text = "Diproses", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (order.status == com.example.antriin.domain.model.OrderStatus.PROCESSING) PrimaryOrange else if (order.status == com.example.antriin.domain.model.OrderStatus.READY) TextBlack else TextGray)
                                    Text(text = "Siap Diambil", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (order.status == com.example.antriin.domain.model.OrderStatus.READY) PrimaryOrange else TextGray)
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Divider(color = PrimaryOrange, thickness = 4.dp)
                            }
                        }
    
                        Spacer(modifier = Modifier.height(24.dp))
                    }
                }

                if (isBusy) {
                    Text(
                        text = "Kantin Sedang Ramai",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Red, RoundedCornerShape(8.dp))
                            .padding(12.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                }

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
                            Text(
                                text = if (sellerName.isNotEmpty()) "Antrean di $sellerName" else "Antrean Kantin",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextBlack,
                                maxLines = 1,
                                modifier = Modifier.weight(1f, fill = false).padding(end = 12.dp).basicMarquee(iterations = Int.MAX_VALUE)
                            )
                            Text(
                                text = "Total: ${queueList.size}",
                                fontSize = 12.sp,
                                color =  TextBlack,
                                modifier = Modifier.background(Color(0xFFFDECE2), RoundedCornerShape(4.dp)).padding(horizontal = 8.dp, vertical = 4.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))

                        if (queueList.isEmpty()) {
                            Box(modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp), contentAlignment = Alignment.Center) {
                                Text(
                                    text = "Belum ada antrean di kantin ini.",
                                    fontSize = 14.sp,
                                    color = TextGray
                                )
                            }
                        } else {
                            queueList.forEachIndexed { index, qOrder ->
                                QueueStatusCard(
                                    order = qOrder,
                                    isCurrentUser = currentUser?.uid != null && qOrder.buyerId == currentUser?.uid,
                                    queueNumber = index + 1
                                )
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

