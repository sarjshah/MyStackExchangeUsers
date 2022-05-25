package com.practice.mystackexchangeusers.data.remote

import com.practice.mystackexchangeusers.common.Constants
import com.practice.mystackexchangeusers.data.remote.dto.UserResponse
import com.practice.mystackexchangeusers.data.remote.dto.UsersResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StackExchangeApi {

    @GET("users")
    suspend fun getUsers(
        @Query("site")
        site: String = Constants.STACK_EXCHANGE_SITE): UsersResponse

    @GET("users/{userId}")
    suspend fun getUser(
        @Path("userId")
        id: String,
        @Query("site")
        site: String = Constants.STACK_EXCHANGE_SITE
    ): UserResponse
}