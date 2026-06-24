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
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.antriin.presentation.theme.BackgroundLight
import com.example.antriin.presentation.theme.PrimaryOrange
import com.example.antriin.presentation.theme.TextBlack
import com.example.antriin.presentation.theme.TextGray

@Composable
fun StudentNotificationScreen(onBackClick: () -> Unit) {
    val dummyNotifications = listOf(
        "Pesanan #ANT-092 kamu sudah siap diambil di Kasir!",
        "Hore! Pesanan Nasi Gorengmu sedang diproses oleh penjual.",
        "Pesanan #ANT-088 kamu telah selesai. Selamat menikmati!"
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = onBackClick) {
                Icon(Icons.Default.ArrowBack, contentDescription = null, tint = TextBlack)
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Notifikasi", fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextBlack)
        }
        Spacer(modifier = Modifier.height(24.dp))

        LazyColumn {
            if (dummyNotifications.isEmpty()) {
                item {
                    com.example.antriin.presentation.components.EmptyState(
                        title = "Belum Ada Notifikasi",
                        message = "Tidak ada pemberitahuan baru saat ini.",
                        modifier = Modifier.padding(top = 40.dp)
                    )
                }
            } else {
                items(dummyNotifications.size) { index ->
                Card(
                    modifier = Modifier.fillMaxWidth().padding(vertical = 6.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Notifications, contentDescription = null, tint = PrimaryOrange)
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = if (index == 0) "Pesanan Siap Diambil!" else "Update Pesanan",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextBlack
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = dummyNotifications[index],
                                fontSize = 12.sp,
                                color = TextGray,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }
        }
        }
    }
}

