package com.gyub.mvi_sample.data

import com.gyub.mvi_sample.data.model.SearchUserResponse
import com.gyub.mvi_sample.data.model.UserDetailResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface GithubApiService {
    @GET("search/users")
    suspend fun searchUsers(
        @Query("q") query: String?,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): SearchUserResponse

    @GET("user/{id}")
    suspend fun getUserDetail(@Path("id") id: Int): UserDetailResponse
}