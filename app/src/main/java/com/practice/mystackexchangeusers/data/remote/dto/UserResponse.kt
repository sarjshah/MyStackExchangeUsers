package com.practice.mystackexchangeusers.data.remote.dto


import com.google.gson.annotations.SerializedName
import com.practice.mystackexchangeusers.domain.model.User

data class UserResponse(
    @SerializedName("items")
    val items: List<UserDto>
) {
    data class UserDto(
        @SerializedName("badge_counts")
        val badgeCountsDto: BadgeCountsDto,
        @SerializedName("creation_date")
        val creationDate: Int,
        @SerializedName("display_name")
        val displayName: String,
        @SerializedName("link")
        val link: String,
        @SerializedName("profile_image")
        val profileImage: String,
        @SerializedName("reputation")
        val reputation: Int,
        @SerializedName("user_id")
        val userId: Int,
        @SerializedName("website_url")
        val websiteUrl: String
    )

    data class BadgeCountsDto(
        @SerializedName("bronze")
        val bronze: Int,
        @SerializedName("gold")
        val gold: Int,
        @SerializedName("silver")
        val silver: Int
    )

    fun BadgeCountsDto.toBadgeCounts() = User.BadgeCounts(
        bronze = this.bronze,
        silver = this.silver,
        gold = this.gold
    )
}

fun UserResponse.toUser() = this.items.firstNotNullOf {
    User(
        userId = it.userId,
        userName = it.displayName,
        reputation = it.reputation,
        badges = it.badgeCountsDto.toBadgeCounts(),
        location = it.link,
        creationDate = it.creationDate,
        profileImage = it.profileImage
    )
}
