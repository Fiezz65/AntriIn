package com.example.antriin.presentation.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.basicMarquee
import com.example.antriin.domain.model.Menu
import com.example.antriin.presentation.theme.PrimaryOrange
import com.example.antriin.presentation.theme.TextBlack
import com.example.antriin.presentation.theme.TextGray
import com.example.antriin.utils.formatRupiah

@androidx.compose.foundation.ExperimentalFoundationApi
@Composable
fun MenuCard(
    menu: Menu,
    onAddClick: () -> Unit,
    modifier: Modifier = Modifier,
    cartQuantity: Int = 0,
    onQuantityChange: (Int) -> Unit = {}
) {
    val isClosed = !menu.isCanteenOpen

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
        colors = CardDefaults.cardColors(containerColor = if (isClosed) Color(0xFFF5F5F5) else Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isClosed) 0.dp else 2.dp),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .alpha(if (isClosed) 0.5f else 1f),
            verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFFFDECE2)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = menu.icon, fontSize = 28.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = menu.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextBlack
                )
                if (menu.canteenName.isNotEmpty()) {
                    Box(
                        modifier = Modifier
                            .padding(top = 2.dp, bottom = 4.dp)
                            .background(Color(0xFFFFF3E0), RoundedCornerShape(4.dp))
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = menu.canteenName,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = PrimaryOrange,
                            maxLines = 1,
                            modifier = Modifier.basicMarquee()
                        )
                    }
                }
                Text(
                    text = menu.description,
                    fontSize = 12.sp,
                    color = TextGray,
                    maxLines = 2,
                    overflow = androidx.compose.ui.text.style.TextOverflow.Ellipsis
                )
                
                Spacer(modifier = Modifier.height(12.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = androidx.compose.foundation.layout.Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = formatRupiah(menu.price),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryOrange
                    )

                    if (isClosed) {
                        Text(
                            text = "Tutup",
                            color = Color.Gray,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold
                        )
                    } else if (!menu.soldOut) {
                        if (cartQuantity > 0) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.background(Color(0xFFFDECE2), RoundedCornerShape(16.dp))
                            ) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clickable { onQuantityChange(cartQuantity - 1) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "-", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryOrange)
                                }
                                Text(
                                    text = cartQuantity.toString(),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextBlack,
                                    modifier = Modifier.padding(horizontal = 8.dp)
                                )
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clickable { onQuantityChange(cartQuantity + 1) },
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = "+", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = PrimaryOrange)
                                }
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .background(PrimaryOrange)
                                    .clickable { onAddClick() }
                                    .padding(horizontal = 14.dp, vertical = 6.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "Tambah",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
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
    }
}