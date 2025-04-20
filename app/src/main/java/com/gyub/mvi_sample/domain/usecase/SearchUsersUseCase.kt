package com.gyub.mvi_sample.domain.usecase

import androidx.paging.PagingData
import com.gyub.mvi_sample.data.model.GithubUser
import com.gyub.mvi_sample.data.repository.UserRepositoryImpl
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject


class SearchUsersUseCase @Inject constructor(
    private val repository: UserRepositoryImpl
) {
    operator fun invoke(query: String): Flow<PagingData<GithubUser>> {
        return repository.searchUsers(query)
    }
}