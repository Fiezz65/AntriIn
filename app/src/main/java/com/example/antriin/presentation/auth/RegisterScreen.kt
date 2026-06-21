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
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.text.input.ImeAction
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
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterStudentClick: (String, String, String, String, String, String, String) -> Unit,
    onRegisterSellerClick: (String, String, String, String, String) -> Unit
) {
    var selectedRole by remember { mutableStateOf("Mahasiswa") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var phoneNumber by remember { mutableStateOf("") }

    var fullName by remember { mutableStateOf("") }
    var studentId by remember { mutableStateOf("") }
    var faculty by remember { mutableStateOf("") }
    var studyProgram by remember { mutableStateOf("") }

    var canteenName by remember { mutableStateOf("") }
    var location by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(BackgroundLight)
            .padding(24.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(24.dp))

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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.clickable { onNavigateToLogin() }
            ) {
                Text(
                    text = "Masuk",
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
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "Daftar",
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
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (selectedRole == "Mahasiswa") {
            CustomTextField(
                value = fullName,
                onValueChange = { fullName = it },
                label = "Nama Lengkap",
                placeholder = "Masukkan nama lengkap",
                leadingIcon = Icons.Default.Person
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
                value = studentId,
                onValueChange = { studentId = it },
                label = "NIM",
                placeholder = "Masukkan NIM Anda",
                keyboardType = KeyboardType.Number
            )
        } else {
            CustomTextField(
                value = canteenName,
                onValueChange = { canteenName = it },
                label = "Nama Kantin",
                placeholder = "Masukkan nama kantin",
                leadingIcon = Icons.Default.Home
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            placeholder = "contoh@email.com",
            leadingIcon = Icons.Default.Email,
            keyboardType = KeyboardType.Email
        )

        Spacer(modifier = Modifier.height(16.dp))

        PasswordTextField(
            value = password,
            onValueChange = { password = it },
            label = "Kata Sandi",
            placeholder = "Buat kata sandi yang kuat",
            leadingIcon = Icons.Default.Lock,
            visibilityIcon = Icons.Default.Visibility,
            visibilityOffIcon = Icons.Default.VisibilityOff,
            imeAction = ImeAction.Next
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = phoneNumber,
            onValueChange = { phoneNumber = it },
            label = "Nomor Telepon",
            placeholder = "08xxxxxxxxxx",
            leadingIcon = Icons.Default.Phone,
            keyboardType = KeyboardType.Phone
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedRole == "Mahasiswa") {
            CustomTextField(
                value = faculty,
                onValueChange = { faculty = it },
                label = "Fakultas",
                placeholder = "Masukkan Fakultas"
            )
            Spacer(modifier = Modifier.height(16.dp))
            CustomTextField(
                value = studyProgram,
                onValueChange = { studyProgram = it },
                label = "Program Studi",
                placeholder = "Masukkan Program Studi",
                imeAction = ImeAction.Done
            )
        } else {
            CustomTextField(
                value = location,
                onValueChange = { location = it },
                label = "Lokasi Kantin",
                placeholder = "Contoh: Fakultas Teknik",
                leadingIcon = Icons.Default.LocationOn,
                imeAction = ImeAction.Done
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        PrimaryButton(
            text = "DAFTAR SEKARANG",
            onClick = {
                if (selectedRole == "Mahasiswa") {
                    onRegisterStudentClick(fullName, studentId, email, password, phoneNumber, faculty, studyProgram)
                } else {
                    onRegisterSellerClick(canteenName, email, password, phoneNumber, location)
                }
            }
        )

        Spacer(modifier = Modifier.height(32.dp))
    }
}