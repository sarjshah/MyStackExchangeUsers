package com.practice.mystackexchangeusers.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val userId: Int,
    val userName: String,
    val reputation: Int? = null,
    val badges: BadgeCounts? = null,
    val location: String? = null,
    val creationDate: Int? = null
) : Parcelable {


    @Parcelize
    data class BadgeCounts(
        val bronze: Int,
        val gold: Int,
        val silver: Int
    ) : Parcelable
}
