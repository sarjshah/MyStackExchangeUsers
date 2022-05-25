package com.practice.mystackexchangeusers.data.repository

import com.practice.mystackexchangeusers.data.remote.StackExchangeApi
import com.practice.mystackexchangeusers.data.remote.dto.UserResponse
import com.practice.mystackexchangeusers.data.remote.dto.UsersResponse
import com.practice.mystackexchangeusers.domain.repository.UserRepository
import javax.inject.Inject

class UserRepositoryImpl @Inject constructor(
    private val apiService: StackExchangeApi
) : UserRepository {

    override suspend fun getUsers(): UsersResponse = apiService.getUsers()

    override suspend fun getUser(userId: String): UserResponse = apiService.getUser(userId)
}