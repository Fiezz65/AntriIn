package com.example.antriin.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.antriin.domain.model.Menu
import com.example.antriin.presentation.theme.PrimaryOrange
import com.example.antriin.presentation.theme.TextBlack
import com.example.antriin.presentation.theme.TextGray
import com.example.antriin.utils.formatRupiah

@Composable
fun MenuCard(
    menu: Menu,
    onAddClick: () -> Unit,
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
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFDECE2)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "🍽️", fontSize = 24.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = menu.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextBlack
                )
                Text(
                    text = menu.description,
                    fontSize = 12.sp,
                    color = TextGray
                )
                Text(
                    text = formatRupiah(menu.price),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryOrange,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }

            if (!menu.isSoldOut) {
                IconButton(
                    onClick = onAddClick,
                    modifier = Modifier
                        .background(PrimaryOrange, CircleShape)
                        .size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            } else {
                Text(
                    text = "Habis",
                    color = Color.Red,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}