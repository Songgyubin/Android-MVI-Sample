package com.gyub.mvi_sample.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.gyub.mvi_sample.data.GithubApiService
import com.gyub.mvi_sample.data.model.GithubUser
import com.gyub.mvi_sample.data.model.UserDetailResponse
import com.gyub.mvi_sample.data.paging.GithubUserPagingSource
import com.gyub.mvi_sample.data.util.Result
import com.gyub.mvi_sample.data.util.safeApiCall
import com.gyub.mvi_sample.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class UserRepositoryImpl @Inject constructor(
    private val apiService: GithubApiService
) : UserRepository {
    override fun searchUsers(query: String?): Flow<PagingData<GithubUser>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { GithubUserPagingSource(apiService, query) }
        ).flow
    }

    override suspend fun getUserDetail(id: Int): Result<UserDetailResponse> = safeApiCall {
        apiService.getUserDetail(id)
    }
}