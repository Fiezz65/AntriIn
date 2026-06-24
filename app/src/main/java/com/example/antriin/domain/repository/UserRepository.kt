package com.example.antriin.domain.repository

import com.example.antriin.domain.model.User
import kotlinx.coroutines.flow.Flow

interface UserRepository {
    fun getSellersByLocation(location: String): Flow<List<User>>
}
