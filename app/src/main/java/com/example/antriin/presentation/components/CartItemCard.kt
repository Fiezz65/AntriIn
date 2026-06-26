package com.example.antriin.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.antriin.domain.model.CartItem
import com.example.antriin.presentation.theme.PrimaryOrange
import com.example.antriin.presentation.theme.TextBlack
import com.example.antriin.presentation.theme.TextGray
import com.example.antriin.utils.formatRupiah

@Composable
fun CartItemCard(
    cartItem: CartItem,
    onIncreaseClick: () -> Unit,
    onDecreaseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFDECE2)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🍱", fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = cartItem.menuName,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextBlack
                )
                Box(
                    modifier = Modifier
                        .padding(vertical = 4.dp)
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color(0xFFFDECE2))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = cartItem.canteenName,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryOrange
                    )
                }
                if (cartItem.notes.isNotEmpty()) {
                    Text(
                        text = "Catatan: ${cartItem.notes}",
                        fontSize = 12.sp,
                        color = TextGray,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatRupiah(cartItem.price),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryOrange
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.background(Color(0xFFFDECE2), RoundedCornerShape(20.dp))
                    ) {
                        IconButton(onClick = onDecreaseClick, modifier = Modifier.size(32.dp)) {
                            Text(text = "-", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PrimaryOrange)
                        }
                        Text(
                            text = cartItem.quantity.toString(),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 8.dp)
                        )
                        IconButton(onClick = onIncreaseClick, modifier = Modifier.size(32.dp)) {
                            Text(text = "+", fontSize = 18.sp, fontWeight = FontWeight.Bold, color = PrimaryOrange)
                        }
                    }
                }
            }
        }
    }
}