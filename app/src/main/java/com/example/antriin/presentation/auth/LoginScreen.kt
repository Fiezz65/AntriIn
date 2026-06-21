package com.example.antriin.presentation.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.antriin.presentation.components.CustomTextField
import com.example.antriin.presentation.components.PasswordTextField
import com.example.antriin.presentation.components.PrimaryButton
import com.example.antriin.presentation.components.RoleSelector
import com.example.antriin.presentation.theme.BackgroundLight
import com.example.antriin.presentation.theme.PrimaryOrange
import com.example.antriin.presentation.theme.TextBlack
import com.example.antriin.presentation.theme.TextGray

@Composable
fun LoginScreen(
    onNavigateToRegister: () -> Unit,
    onLoginClick: (String, String, String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Mahasiswa") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = "AntriIn",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = PrimaryOrange
        )
        Text(
            text = "Solusi antrean kantin efisien",
            fontSize = 14.sp,
            color = TextGray,
            modifier = Modifier.padding(top = 8.dp, bottom = 32.dp)
        )

        RoleSelector(
            selectedRole = selectedRole,
            onRoleSelected = { selectedRole = it }
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Masuk",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextBlack
                )
                Divider(
                    color = PrimaryOrange,
                    thickness = 2.dp,
                    modifier = Modifier
                        .width(60.dp)
                        .padding(top = 4.dp)
                )
            }
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onNavigateToRegister() }
            ) {
                Text(
                    text = "Daftar",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal,
                    color = TextGray
                )
                Divider(
                    color = Color.Transparent,
                    thickness = 2.dp,
                    modifier = Modifier
                        .width(60.dp)
                        .padding(top = 4.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        CustomTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            placeholder = "contoh@mahasiswa.ac.id",
            leadingIcon = Icons.Default.Email,
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordTextField(
            value = password,
            onValueChange = { password = it },
            label = "Kata Sandi",
            placeholder = "••••••••",
            leadingIcon = Icons.Default.Lock,
            visibilityIcon = Icons.Default.Visibility,
            visibilityOffIcon = Icons.Default.VisibilityOff
        )

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryButton(
            text = "MASUK",
            onClick = { onLoginClick(email, password, selectedRole) }
        )
    }
}