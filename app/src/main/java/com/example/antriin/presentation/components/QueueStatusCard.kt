package com.example.antriin.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.antriin.domain.model.Order
import com.example.antriin.presentation.theme.PrimaryOrange
import com.example.antriin.presentation.theme.TextBlack

@Composable
fun QueueStatusCard(
    order: Order,
    isCurrentUser: Boolean,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isCurrentUser) Color(0xFFFDECE2) else Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(Color(0xFFEEEEEE), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(text = "👤", fontSize = 16.sp)
                }

                Text(
                    text = if (isCurrentUser) "Saya" else order.buyerName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextBlack,
                    modifier = Modifier.padding(start = 12.dp)
                )
            }

            Text(
                text = order.status,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (order.status == "Siap Diambil") Color(0xFF4CAF50) else PrimaryOrange,
                modifier = Modifier
                    .background(
                        color = if (order.status == "Siap Diambil") Color(0xFFE8F5E9) else Color(0xFFFDECE2),
                        shape = RoundedCornerShape(4.dp)
                    )
                    .padding(horizontal = 8.dp, vertical = 4.dp)
            )
        }
    }
}