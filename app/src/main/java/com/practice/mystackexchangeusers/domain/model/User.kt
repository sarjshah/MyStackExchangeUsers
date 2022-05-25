package com.practice.mystackexchangeusers.domain.model

data class User(
    val userId: Int,
    val userName: String,
    val reputation: Int? = null,
    val badges: BadgeCounts? = null,
    val location: String? = null,
    val creationDate: Int? = null
) {


    data class BadgeCounts(
        val bronze: Int,
        val gold: Int,
        val silver: Int
    )
}
