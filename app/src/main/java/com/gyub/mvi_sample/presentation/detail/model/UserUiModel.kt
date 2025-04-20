package com.gyub.mvi_sample.presentation.detail.model

import com.gyub.mvi_sample.data.model.UserDetailResponse

data class UserUiModel(
    val id: Int = 0,
    val userName: String = "",
    val bio: String = "",
    val location: String = "",
    val avatarUrl: String = ""
)

fun UserDetailResponse.toUiModel(): UserUiModel {
    return UserUiModel(
        id = id,
        userName = login,
        bio = bio ?: "",
        location = location ?: "",
        avatarUrl = avatarUrl
    )
}