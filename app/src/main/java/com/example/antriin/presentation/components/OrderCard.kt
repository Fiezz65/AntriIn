package com.example.antriin.presentation.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.antriin.domain.model.Order
import com.example.antriin.presentation.theme.PrimaryOrange
import com.example.antriin.presentation.theme.TextBlack
import com.example.antriin.presentation.theme.TextGray
import com.example.antriin.utils.formatRupiah

@Composable
fun OrderCard(
    order: Order,
    onNextStepClick: () -> Unit,
    onCancelClick: () -> Unit,
    modifier: Modifier = Modifier,
    queueNumber: Int? = null
) {
    var actionToConfirm by remember { mutableStateOf<String?>(null) }

    if (actionToConfirm != null) {
        val title = when (actionToConfirm) {
            "Tolak" -> "Tolak Pesanan"
            "Terima" -> "Terima Pesanan"
            "Siap" -> "Pesanan Siap"
            "Selesai" -> "Selesaikan Pesanan"
            else -> ""
        }
        val message = when (actionToConfirm) {
            "Tolak" -> "Apakah Anda yakin ingin menolak pesanan dari ${order.buyerName}?"
            "Terima" -> "Apakah Anda yakin ingin menerima dan memproses pesanan dari ${order.buyerName}?"
            "Siap" -> "Tandai pesanan dari ${order.buyerName} sebagai siap diambil?"
            "Selesai" -> "Pastikan pesanan sudah diambil oleh ${order.buyerName}. Selesaikan sekarang?"
            else -> ""
        }
        val isDestructive = actionToConfirm == "Tolak"

        androidx.compose.material3.AlertDialog(
            onDismissRequest = { actionToConfirm = null },
            title = { Text(title, fontWeight = FontWeight.Bold) },
            text = { Text(message) },
            containerColor = Color.White,
            confirmButton = {
                androidx.compose.material3.TextButton(
                    onClick = {
                        if (actionToConfirm == "Tolak") {
                            onCancelClick()
                        } else {
                            onNextStepClick()
                        }
                        actionToConfirm = null
                    }
                ) {
                    Text(
                        text = if (isDestructive) "Tolak" else "Ya, Lanjutkan", 
                        color = if (isDestructive) Color.Red else PrimaryOrange, 
                        fontWeight = FontWeight.Bold
                    )
                }
            },
            dismissButton = {
                androidx.compose.material3.TextButton(onClick = { actionToConfirm = null }) {
                    Text("Batal", color = TextGray)
                }
            }
        )
    }

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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.weight(1f)
                ) {
                    if (queueNumber != null) {
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .background(PrimaryOrange, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = queueNumber.toString(), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text(
                        text = order.buyerName,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextBlack,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                }
                val isTunai = order.paymentMethod.equals("Tunai", ignoreCase = true)
                Text(
                    text = order.paymentMethod,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isTunai) Color(0xFF2E7D32) else Color(0xFF1565C0),
                    modifier = Modifier
                        .background(
                            color = if (isTunai) Color(0xFFE8F5E9) else Color(0xFFE3F2FD), 
                            shape = RoundedCornerShape(4.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            order.items.forEach { item ->
                Column(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(text = "x${item.quantity} ${item.menuName}", fontSize = 14.sp, color = TextBlack)
                        Text(text = formatRupiah(item.price), fontSize = 14.sp, color = TextBlack)
                    }
                    if (item.notes.isNotEmpty()) {
                        Text(
                            text = "Catatan: ${item.notes}",
                            fontSize = 12.sp,
                            fontStyle = FontStyle.Italic,
                            color = PrimaryOrange,
                            modifier = Modifier.padding(top = 2.dp, start = 20.dp, bottom = 4.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
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
                    text = formatRupiah(order.totalPrice),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextBlack
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = order.status,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = PrimaryOrange
                )
                Spacer(modifier = Modifier.height(12.dp))

                when (order.status) {
                    "Menunggu Validasi", "Belum Bayar" -> {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            OutlinedButton(
                                onClick = { actionToConfirm = "Tolak" },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(8.dp),
                                border = BorderStroke(1.dp, Color.Red),
                                colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)
                            ) {
                                Text(text = "Tolak", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                            }
                            Button(
                                onClick = { actionToConfirm = "Terima" },
                                modifier = Modifier.weight(1f),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(text = "Terima & Proses", fontSize = 13.sp, fontWeight = FontWeight.Bold, color = Color.White, textAlign = TextAlign.Center)
                            }
                        }
                    }
                    "Diproses" -> {
                        Button(
                            onClick = { actionToConfirm = "Siap" },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = PrimaryOrange),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "Tandai Siap Diambil", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                    "Siap Diambil" -> {
                        Button(
                            onClick = { actionToConfirm = "Selesai" },
                            modifier = Modifier.fillMaxWidth(),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF4CAF50)),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text(text = "Selesaikan Pesanan", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                }
            }
        }
    }
}