package com.gyub.mvi_sample.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class UserDetailResponse(
    val id: Int,
    val login: String,
    @SerialName("avatar_url") val avatarUrl: String,
    val name: String?,
    val bio: String?,
    val location: String?
)