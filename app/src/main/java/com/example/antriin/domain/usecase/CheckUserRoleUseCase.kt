package com.example.antriin.domain.usecase

import com.example.antriin.domain.repository.AuthRepository

class CheckUserRoleUseCase(private val authRepository: AuthRepository) {
    suspend operator fun invoke(): String? {
        return authRepository.checkUserRole()
    }
}
