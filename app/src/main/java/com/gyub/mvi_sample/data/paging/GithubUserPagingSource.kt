package com.gyub.mvi_sample.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.gyub.mvi_sample.data.GithubApiService
import com.gyub.mvi_sample.data.model.GithubUser
import com.gyub.mvi_sample.data.model.SearchUserResponse

class GithubUserPagingSource(
    private val service: GithubApiService,
    private val query: String?
) : PagingSource<Int, GithubUser>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, GithubUser> {
        val page = params.key ?: 1
        return try {
            val response = if (query.isNullOrBlank()) {
                SearchUserResponse(
                    totalCount = 0,
                    incompleteResults = false,
                    items = listOf()
                )
            } else {
                service.searchUsers(query, page, params.loadSize)
            }

            LoadResult.Page(
                data = response.items,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (response.items.isEmpty()) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, GithubUser>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }
}