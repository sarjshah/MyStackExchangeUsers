package com.practice.mystackexchangeusers.domain.repository

import com.practice.mystackexchangeusers.data.remote.dto.UserResponse
import com.practice.mystackexchangeusers.data.remote.dto.UsersResponse

interface UserRepository {

    suspend fun getUsers(): UsersResponse

    suspend fun getUser(userId: String): UserResponse

}