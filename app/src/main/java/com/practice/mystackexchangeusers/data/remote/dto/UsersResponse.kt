package com.practice.mystackexchangeusers.data.remote.dto


import com.google.gson.annotations.SerializedName
import com.practice.mystackexchangeusers.domain.model.User

data class UsersResponse(
    @SerializedName("items")
    val items: List<UserDto>
) {
    data class UserDto(
        @SerializedName("display_name")
        val displayName: String,
        @SerializedName("user_id")
        val userId: Int
    )
}

fun UsersResponse.toUsers() =
    items.map {
        User(userId = it.userId, userName = it.displayName)
    }