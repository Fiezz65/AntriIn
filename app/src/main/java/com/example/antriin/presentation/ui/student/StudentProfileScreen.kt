package com.example.antriin.presentation.ui.student

import com.example.antriin.presentation.viewmodel.student.CartViewModel
import com.example.antriin.presentation.viewmodel.student.StudentNotificationViewModel
import com.example.antriin.presentation.viewmodel.student.StudentProfileViewModel
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.rememberScrollState
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.antriin.presentation.components.BottomNavBar
import com.example.antriin.presentation.theme.BackgroundLight
import com.example.antriin.presentation.theme.PrimaryOrange
import com.example.antriin.presentation.theme.TextBlack
import com.example.antriin.presentation.theme.TextGray

@Composable
fun StudentProfileScreen(
    onNavigate: (String) -> Unit,
    onTabNavigate: (String) -> Unit,
    viewModel: StudentProfileViewModel = viewModel(factory = com.example.antriin.di.ViewModelFactory.Factory),
    cartViewModel: CartViewModel = viewModel(factory = com.example.antriin.di.ViewModelFactory.Factory),
    notificationViewModel: StudentNotificationViewModel = viewModel(factory = com.example.antriin.di.ViewModelFactory.Factory)
) {
    val user by viewModel.userProfile.collectAsState()
    val cartItems by cartViewModel.cartItems.collectAsState()

    val notificationCount by notificationViewModel.unreadCount.collectAsState()

    val context = androidx.compose.ui.platform.LocalContext.current
    androidx.compose.runtime.LaunchedEffect(Unit) {
        notificationViewModel.startGlobalListener(context)
    }

    Scaffold(
        bottomBar = {
            BottomNavBar(
                currentRoute = "profile",
                onNavigate = onTabNavigate,
                isSeller = false,
                cartItemCount = cartItems.size
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(BackgroundLight)
                .padding(paddingValues)
                .padding(horizontal = 24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
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

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(100.dp)
                            .background(Color(0xFFEEEEEE), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(60.dp), tint = TextGray)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(text = user.fullName, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = TextBlack, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = user.studentId, fontSize = 14.sp, color = TextGray, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = user.email, fontSize = 14.sp, color = TextGray, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = user.phoneNumber, fontSize = 14.sp, color = TextGray, textAlign = TextAlign.Center)
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Mahasiswa",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = Color(0xFF3F51B5),
                        modifier = Modifier
                            .background(Color(0xFFE8EAF6), RoundedCornerShape(16.dp))
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

                Button(
                onClick = { 
                    viewModel.logout {
                        onNavigate("login")
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFFEBEE)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = null, tint = Color.Red)
                    Text(text = "Keluar", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.Red, modifier = Modifier.padding(start = 16.dp))
                }
            }
        }
    }
}