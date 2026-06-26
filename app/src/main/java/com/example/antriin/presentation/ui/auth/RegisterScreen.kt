package com.example.antriin.presentation.ui.auth

import com.example.antriin.presentation.viewmodel.auth.AuthViewModel
import com.example.antriin.presentation.viewmodel.seller.DashboardViewModel
import com.example.antriin.presentation.viewmodel.seller.HistoryViewModel
import com.example.antriin.presentation.viewmodel.seller.MenuViewModel
import com.example.antriin.presentation.viewmodel.seller.SellerNotificationViewModel
import com.example.antriin.presentation.viewmodel.seller.SellerProfileViewModel
import com.example.antriin.presentation.viewmodel.student.CartViewModel
import com.example.antriin.presentation.viewmodel.student.HomeViewModel
import com.example.antriin.presentation.viewmodel.student.LiveTrackingViewModel
import com.example.antriin.presentation.viewmodel.student.StudentProfileViewModel

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.antriin.di.ViewModelFactory
import com.example.antriin.presentation.components.CustomTextField
import com.example.antriin.presentation.components.PasswordTextField
import com.example.antriin.presentation.components.PrimaryButton
import com.example.antriin.presentation.components.RoleSelector
import com.example.antriin.presentation.theme.BackgroundLight
import com.example.antriin.presentation.theme.PrimaryOrange
import com.example.antriin.presentation.theme.TextBlack
import com.example.antriin.presentation.theme.TextGray
import com.example.antriin.utils.UiState

@Composable
fun RegisterScreen(
    onNavigateToLogin: () -> Unit,
    onRegisterSuccess: (String) -> Unit,
    viewModel: AuthViewModel = viewModel(factory = ViewModelFactory.Factory)
) {
    var selectedRole by remember {
        mutableStateOf("Mahasiswa")
    }

    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }

    var phoneNumber by remember {
        mutableStateOf("")
    }

    var fullName by remember {
        mutableStateOf("")
    }

    var studentId by remember {
        mutableStateOf("")
    }

    var faculty by remember {
        mutableStateOf("")
    }

    var studyProgram by remember {
        mutableStateOf("")
    }

    var canteenName by remember {
        mutableStateOf("")
    }

    var location by remember {
        mutableStateOf("")
    }

    var isLocationDropdownExpanded by remember {
        mutableStateOf(false)
    }

    var secretCode by remember {
        mutableStateOf("")
    }

    var isFacultyDropdownExpanded by remember {
        mutableStateOf(false)
    }

    var isMajorDropdownExpanded by remember {
        mutableStateOf(false)
    }

    val campusList = listOf(
        "Fakultas Teknik (Banjarmasin)",
        "Fakultas Teknik (Banjarbaru)",
        "Fakultas Ekonomi dan Bisnis"
    )

    val majorList = when (faculty) {
        "Fakultas Teknik (Banjarmasin)" -> listOf("Teknologi Informasi")
        "Fakultas Teknik (Banjarbaru)" -> listOf("Teknik Sipil", "Teknik Mesin", "Teknik Kimia")
        else -> listOf("Belum ada data program studi")
    }

    val authState by viewModel.authState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(authState) {
        when (authState) {
            is UiState.Success -> {
                onRegisterSuccess(selectedRole)
                viewModel.resetState()
            }
            is UiState.Error -> {
                Toast.makeText(
                    context,
                    (authState as UiState.Error).message,
                    Toast.LENGTH_LONG
                ).show()
                viewModel.resetState()
            }
            else -> {}
        }
    }

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
            onRoleSelected = {
                if (selectedRole != it) {
                    selectedRole = it
                    email = ""
                    password = ""
                    phoneNumber = ""
                    fullName = ""
                    studentId = ""
                    faculty = ""
                    studyProgram = ""
                    canteenName = ""
                    location = ""
                    secretCode = ""
                }
            }
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
                onValueChange = { if (it.length <= 25) canteenName = it },
                label = "Nama Kantin",
                placeholder = "Masukkan nama kantin",
                leadingIcon = Icons.Default.Home,
                supportingText = {
                    Text(
                        text = "${canteenName.length}/25",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = androidx.compose.ui.text.style.TextAlign.End,
                        color = TextGray
                    )
                }
            )
        }

        Spacer(modifier = Modifier.height(if (selectedRole == "Mahasiswa") 16.dp else 4.dp))

        CustomTextField(
            value = email,
            onValueChange = { email = it },
            label = "Email",
            placeholder = "contoh@gmail.com",
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
            keyboardType = KeyboardType.Phone,
            imeAction = ImeAction.Done
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (selectedRole == "Mahasiswa") {
            Box {
                CustomTextField(
                    value = faculty,
                    onValueChange = { },
                    label = "Fakultas",
                    placeholder = "Pilih Fakultas",
                    imeAction = ImeAction.Done
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { isFacultyDropdownExpanded = true }
                )
                DropdownMenu(
                    expanded = isFacultyDropdownExpanded,
                    onDismissRequest = { isFacultyDropdownExpanded = false },
                    modifier = Modifier
                        .background(Color.White)
                        .heightIn(max = 220.dp)
                ) {
                    campusList.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(text = option, color = TextBlack)
                            },
                            onClick = {
                                faculty = option
                                studyProgram = ""
                                isFacultyDropdownExpanded = false
                            },
                            modifier = Modifier.background(Color.White)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Box {
                CustomTextField(
                    value = studyProgram,
                    onValueChange = { },
                    label = "Program Studi",
                    placeholder = if (faculty.isEmpty()) "Pilih fakultas terlebih dahulu" else "Pilih Program Studi",
                    imeAction = ImeAction.Done
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable {
                            if (faculty.isNotEmpty()) isMajorDropdownExpanded = true
                        }
                )
                DropdownMenu(
                    expanded = isMajorDropdownExpanded,
                    onDismissRequest = { isMajorDropdownExpanded = false },
                    modifier = Modifier
                        .background(Color.White)
                        .heightIn(max = 220.dp)
                ) {
                    majorList.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(text = option, color = TextBlack)
                            },
                            onClick = {
                                if (option != "Belum ada data program studi") studyProgram = option
                                isMajorDropdownExpanded = false
                            },
                            modifier = Modifier.background(Color.White)
                        )
                    }
                }
            }
        } else {
            Box {
                CustomTextField(
                    value = location,
                    onValueChange = { },
                    label = "Lokasi Kantin",
                    placeholder = "Pilih Lokasi Kampus",
                    leadingIcon = Icons.Default.LocationOn,
                    imeAction = ImeAction.Done
                )
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .clickable { isLocationDropdownExpanded = true }
                )
                DropdownMenu(
                    expanded = isLocationDropdownExpanded,
                    onDismissRequest = { isLocationDropdownExpanded = false },
                    modifier = Modifier
                        .background(Color.White)
                        .heightIn(max = 220.dp)
                ) {
                    campusList.forEach { option ->
                        DropdownMenuItem(
                            text = {
                                Text(text = option, color = TextBlack)
                            },
                            onClick = {
                                location = option
                                isLocationDropdownExpanded = false
                            },
                            modifier = Modifier.background(Color.White)
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(16.dp))

            PasswordTextField(
                value = secretCode,
                onValueChange = { secretCode = it },
                label = "Kode Akses Daftar",
                placeholder = "Masukkan kode khusus pendaftaran kantin",
                leadingIcon = Icons.Default.Lock,
                visibilityIcon = Icons.Default.Visibility,
                visibilityOffIcon = Icons.Default.VisibilityOff,
                imeAction = ImeAction.Done
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        if (authState is UiState.Loading) {
            CircularProgressIndicator(color = PrimaryOrange)
        } else {
            PrimaryButton(
                text = "DAFTAR SEKARANG",
                onClick = {
                    if (selectedRole == "Mahasiswa") {
                        viewModel.registerStudent(
                            fullName = fullName,
                            studentId = studentId,
                            email = email,
                            pass = password,
                            phoneNumber = phoneNumber,
                            faculty = faculty,
                            major = studyProgram
                        )
                    } else {
                        viewModel.registerSeller(
                            canteenName = canteenName,
                            email = email,
                            pass = password,
                            phoneNumber = phoneNumber,
                            location = location,
                            secretCode = secretCode
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

