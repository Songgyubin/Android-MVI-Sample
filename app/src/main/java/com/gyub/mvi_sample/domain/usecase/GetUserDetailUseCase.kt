package com.gyub.mvi_sample.domain.usecase

import com.gyub.mvi_sample.data.model.UserDetailResponse
import com.gyub.mvi_sample.data.util.Result
import com.gyub.mvi_sample.data.util.convertIfSuccess
import com.gyub.mvi_sample.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import javax.inject.Inject


class GetUserDetailUseCase @Inject constructor(
    private val repository: UserRepository
) {
    operator fun invoke(id: Int): Flow<Result<UserDetailResponse>> = flow {
        val result = repository.getUserDetail(id = id).convertIfSuccess { it }

        emit(result)
    }.onStart {
        emit(Result.Loading)
    }.catch {
        emit(Result.Error)
    }
}
