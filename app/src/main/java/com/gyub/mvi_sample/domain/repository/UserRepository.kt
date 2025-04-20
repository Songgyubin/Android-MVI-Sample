package com.gyub.mvi_sample.domain.repository

import androidx.paging.PagingData
import com.gyub.mvi_sample.data.model.GithubUser
import com.gyub.mvi_sample.data.model.UserDetailResponse
import com.gyub.mvi_sample.data.util.Result
import kotlinx.coroutines.flow.Flow


interface UserRepository {
    fun searchUsers(query: String?): Flow<PagingData<GithubUser>>

    suspend fun getUserDetail(id: Int): Result<UserDetailResponse>
}