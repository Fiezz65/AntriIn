package com.example.antriin.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.antriin.presentation.theme.PrimaryOrange
import com.example.antriin.presentation.theme.TextBlack

@Composable
fun RoleSelector(
    selectedRole: String,
    onRoleSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .clip(RoundedCornerShape(24.dp))
            .background(Color(0xFFFDECE2))
    ) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(24.dp))
                .background(if (selectedRole == "Mahasiswa") PrimaryOrange else Color.Transparent)
                .clickable { onRoleSelected("Mahasiswa") }
        ) {
            Text(
                text = "Mahasiswa",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (selectedRole == "Mahasiswa") Color.White else TextBlack
            )
        }
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .clip(RoundedCornerShape(24.dp))
                .background(if (selectedRole == "Penjual") PrimaryOrange else Color.Transparent)
                .clickable { onRoleSelected("Penjual") }
        ) {
            Text(
                text = "Penjual",
                fontSize = 14.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (selectedRole == "Penjual") Color.White else TextBlack
            )
        }
    }
}