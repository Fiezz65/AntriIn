package com.example.antriin.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import com.example.antriin.presentation.theme.TextGray

@Composable
fun OrderCard(
    order: Order,
    onProcessClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = order.buyerName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextBlack
                )
                Text(
                    text = order.paymentMethod,
                    fontSize = 12.sp,
                    color = TextGray,
                    modifier = Modifier
                        .background(Color(0xFFEEEEEE), RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            order.items.forEach { item ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = "${item.quantity}x ${item.menuName}", fontSize = 14.sp, color = TextBlack)
                    Text(text = "Rp ${item.price}", fontSize = 14.sp, color = TextBlack)
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Total",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextBlack
                )
                Text(
                    text = "Rp ${order.totalPrice}",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextBlack
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = order.status,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryOrange
                )

                if (order.status == "Menunggu Validasi") {
                    Button(
                        onClick = onProcessClick,
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(text = "Terima & Proses", color = Color.White)
                    }
                }
            }
        }
    }
}