package com.gyub.mvi_sample.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchUserResponse(
    @SerialName("total_count") val totalCount: Int,
    @SerialName("incomplete_results") val incompleteResults: Boolean,
    val items: List<GithubUser>
)

@Serializable
data class GithubUser(
    val login: String,
    val id: Int,
    @SerialName("avatar_url") val avatarUrl: String
)
