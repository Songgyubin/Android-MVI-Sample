package com.gyub.mvi_sample.presentation.main.state

import com.gyub.mvi_sample.presentation.base.Reducer


data class MainUiState(
    val searchQuery: String = "",
    val isLoading: Boolean = false,
    val isSuccess: Boolean = false,
    val isError: Boolean = false
): Reducer.State